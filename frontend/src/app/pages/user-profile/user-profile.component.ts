import { Component, DestroyRef, OnInit } from '@angular/core';
import { UserBackendService } from "../../services/user-backend.service";
import { User } from "../../models/user";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";
import { SnackBarService } from "../../services/snack-bar.service";
import { Router } from "@angular/router";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  user: User;

  loading: boolean;

  constructor(private destroyRef: DestroyRef,
              private userService: UserBackendService,
              private snackBarService: SnackBarService,
              private router: Router) {
  }

  ngOnInit() {
    this.getUserProfile();
  }


  public getUserProfile() {
    this.loading = true;
    this.userService.getCurrentUser().pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: (data) => {
        this.user = data;
      },
      error: (error) => {
        this.loading = false;
        this.snackBarService.error(`${error.message}`)
      },
      complete: () => {
        this.loading = false;
      }
    })
  }

  public sendEmailVerification() {
    this.loading = true;
    this.userService.sendEmailForVerification().pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      error: (error) => {
        this.loading = false;
        this.snackBarService.error(`${error.message}`);
      },
      complete: () => {
        this.loading = false;
        this.snackBarService.success("Email sent! Check your inbox");
      }
    })
  }

  public logout() {
    this.loading = true;
    this.userService.logout().pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: () => {
        this.snackBarService.success("Logout successful");
        this.router.navigate(['/all-items']);
      },
      error: (error) => {
        console.log(`${error.message}`);
        this.loading = false;
        this.snackBarService.error("Logout failed. Reload the page please")
      },
      complete: () => {
        this.loading = false;
      }
    })
  }
}
