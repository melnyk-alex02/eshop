import { Component, DestroyRef, OnInit } from '@angular/core';
import { UserBackendService } from "../../services/user-backend.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { SnackBarService } from "../../services/snack-bar.service";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";

@Component({
  selector: 'app-user-send-email-password-reset',
  templateUrl: './user-send-email-password-reset.component.html',
  styleUrls: ['./user-send-email-password-reset.component.css']
})
export class UserSendEmailPasswordResetComponent  implements OnInit{
  form: FormGroup;
  loading: boolean;

  constructor(private userService: UserBackendService,
              private formBuilder: FormBuilder,
              private snackBarService: SnackBarService,
              private destroyRef: DestroyRef) {

  }

  ngOnInit() {
    this.form = this.formBuilder.group({
      email:[null, [Validators.required, Validators.email]]
    })
  }

  public sendEmail() {
    const email = this.form.get("email")?.value;
    this.loading = true;
    this.userService.sendEmailForPasswordReset(email).pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: () => {
        console.log("Email sent");
        this.snackBarService.success("Email was sent! Check you inbox")
      },
      error: (error) => {
        this.loading = false
        this.snackBarService.error(error.error.message)
      },
      complete: () => {
        localStorage.setItem('email', email);
        this.loading = false;
      }
    })
  }
}
