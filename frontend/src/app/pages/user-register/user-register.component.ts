import { Component, DestroyRef, OnInit } from '@angular/core';
import { UserBackendService } from "../../services/user-backend.service";
import { SnackBarService } from "../../services/snack-bar.service";
import { Router } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";

@Component({
  selector: 'app-user-register',
  templateUrl: './user-register.component.html',
  styleUrls: ['./user-register.component.css']
})
export class UserRegisterComponent implements OnInit {

  form: FormGroup;
  loading: boolean;

  constructor(private userService: UserBackendService,
              private snackBarService: SnackBarService,
              private router: Router,
              private destroyRef: DestroyRef,
              private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.form = this.formBuilder.group({
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required, Validators.minLength(6)]],
      confirmPassword: [null, [Validators.required, Validators.minLength(6)]],
      firstName: [null, [Validators.required]],
      lastName: [null, [Validators.required, Validators.minLength(2)]],
      rememberMe: [false],
    })
  }

  public register() {
    this.loading = true;
    this.userService.register(this.form.getRawValue(), this.form.get("rememberMe")?.value).pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: () => {
        this.loading = false;
        this.snackBarService.success("User registered successfully. Check your inbox for verification email");
      },
      error: (error) => {
        this.loading = false;
        this.snackBarService.error(`${error.message}`)
      },
      complete: () => {
        this.loading = false;
        this.sendEmailVerification();
        this.userService.checkAuthenticationAndGetUser().pipe(
          takeUntilDestroyed(this.destroyRef)
        ).subscribe({
          next: () => {
            this.router.navigate(['/profile']);
          },
          error: (error) => {
            this.snackBarService.error(`${error.message}`)
          }
        })
      }
    });
  }

  public sendEmailVerification() {
    this.loading = true;
    this.userService.sendEmailForVerification().pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      error: (error) => {
        this.loading = false;
        this.snackBarService.error(`${error.message}`)
      },
      complete: () => {
        this.loading = false;
      }
    })
  }
}
