import { Component, DestroyRef, OnInit } from '@angular/core';
import { UserBackendService } from "../../services/user-backend.service";
import { SnackBarService } from "../../services/snack-bar.service";
import { ActivatedRoute, ParamMap, Router } from "@angular/router";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";

@Component({
  selector: 'app-user-verify-email',
  templateUrl: './user-verify-email.component.html',
  styleUrls: ['./user-verify-email.component.css']
})
export class UserVerifyEmailComponent implements OnInit {
  loading: boolean;

  constructor(private userService: UserBackendService,
              private destroyRef: DestroyRef,
              private snackBarService: SnackBarService,
              private route: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit() {
    this.loading = true;
    this.route.paramMap.pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe((params: ParamMap) => {
      const otp = params.get('oneTimePassword')?.valueOf();
      this.userService.verifyEmail(otp).pipe(
        takeUntilDestroyed(this.destroyRef)
      ).subscribe({
        next: () => {
          this.snackBarService.success("Email verified successfully");
          this.router.navigate(["/"]);
        },
        error: (error) => {
          this.snackBarService.error(`${error.error.message}`);
          this.loading = false;
        },
        complete: () => {
          this.loading = false;
        }
      })
    })
  }
}
