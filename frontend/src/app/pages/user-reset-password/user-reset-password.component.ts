import { Component, DestroyRef, OnInit } from '@angular/core';
import { switchMap } from "rxjs";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";
import { ActivatedRoute, ParamMap, Router } from "@angular/router";
import { UserBackendService } from "../../services/user-backend.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { SnackBarService } from "../../services/snack-bar.service";

@Component({
  selector: 'app-user-reset-password',
  templateUrl: './user-reset-password.component.html',
  styleUrls: ['./user-reset-password.component.css']
})
export class UserResetPasswordComponent implements OnInit {
  form: FormGroup;
  email: string | undefined;
  loading: boolean;
  otp: string | undefined;

  constructor(private destroyRef: DestroyRef,
              private route: ActivatedRoute,
              private router: Router,
              private userService: UserBackendService,
              private snackBarService: SnackBarService,
              private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.form = this.formBuilder.group({
      password: [null, [Validators.required, Validators.minLength(6)]],
      confirmPassword: [null, [Validators.required, Validators.minLength(6)]]
    })

    this.route.paramMap.pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: (params: ParamMap) => {
        this.otp = params.get('oneTimePassword')?.valueOf();
        this.email = localStorage.getItem("email")?.valueOf();
      }
    })
  }

  submitPasswordReset() {
    this.loading = true;
    this.userService.resetPassword(this.email, this.otp, this.form.get("password")?.value, this.form.get("confirmPassword")?.value).pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next:() => {
        this.snackBarService.success("Password reset successful");
      }, error: (error) => {
        this.loading = false;
        this.snackBarService.error(`${error.error.message}`);
      },
      complete: () => {
        this.loading = false;
        localStorage.removeItem("email")
        this.router.navigate(["/login"]);
      }
    })
  }
}
