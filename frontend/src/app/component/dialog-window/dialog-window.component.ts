import { Component } from '@angular/core';
import { MatDialogRef } from "@angular/material/dialog";

@Component({
  selector: 'app-dialog-window',
  templateUrl: './dialog-window.component.html',
  styleUrls: ['./dialog-window.component.css']
})
export class DialogWindowComponent {

  constructor(public dialogRef: MatDialogRef<DialogWindowComponent>) {
  }

  onCancel(): void {
    this.dialogRef.close({event: "cancel-option"});
  }

  onConfirm(): void {
    this.dialogRef.close({event: 'confirm-option'})
  }
}
