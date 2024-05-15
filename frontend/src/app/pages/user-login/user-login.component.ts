import { Component, DestroyRef, OnInit } from '@angular/core';
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { ActivatedRoute, Router } from "@angular/router";
import { SnackBarService } from "../../services/snack-bar.service";
import { UserBackendService } from "../../services/user-backend.service";
import { User } from "../../models/user";

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent implements OnInit {
  form: FormGroup;
  loading: boolean;
  user: User | null;
  returnUrl: string;

  constructor(private userBackendService: UserBackendService,
              private snackBarService: SnackBarService,
              public readonly router: Router,
              private formBuilder: FormBuilder,
              public dialog: MatDialog,
              private route: ActivatedRoute,
              private destroyRef: DestroyRef) {
  }

  ngOnInit() {
    this.form = this.formBuilder.group({
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required, Validators.minLength(5)]],
      rememberMe: [false]
    })

    this.returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') || '/';
  }


  public login() {
    const email = this.form.get("email")?.value;
    const password = this.form.get("password")?.value;
    const rememberMe = this.form.get("rememberMe")?.value;

    this.loading = true;
    this.userBackendService.getToken(email, password, rememberMe).pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
        next: () => {
          this.snackBarService.success("Logged in successfully");
          this.getCurrentUser();
          localStorage.removeItem("email")
        },
        error: () => {
          this.loading = false
          this.snackBarService.error("Invalid email or password");
        },
        complete: () => {
          this.loading = false;
          if (this.returnUrl) {
            this.router.navigate([this.returnUrl]);
          } else {
            this.router.navigate(["/"]);
          }
        }
      }
    )

  }

  public getCurrentUser() {
    this.userBackendService.checkAuthenticationAndGetUser().subscribe();
  }
}
