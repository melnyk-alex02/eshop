import { Injectable } from '@angular/core';
import { MatSnackBar } from "@angular/material/snack-bar";

@Injectable({
  providedIn: 'root'
})
export class SnackBarService {

  constructor(private snackBar: MatSnackBar) {}

  error(message: string) {
    this.snackBar.open(message, 'OK', {
      duration: 5000
    });
  }

  success(message: string) {
    this.snackBar.open(message, 'OK', {
      duration: 5000
    });
  }
}
