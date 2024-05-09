import { Component, DestroyRef, OnInit } from '@angular/core';
import { CategoryBackendService } from "../../services/category-backend.service";
import { Router } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { DialogWindowComponent } from "../../component/dialog-window/dialog-window.component";
import { Category } from "../../models/category";
import { SnackBarService } from "../../services/snack-bar.service";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";

@Component({
  selector: 'app-category-create',
  templateUrl: './category-create.component.html',
  styleUrls: ['./category-create.component.css']
})
export class CategoryCreateComponent implements OnInit {

  category: Category;

  form: FormGroup;

  fileName = '';

  constructor(private categoryService: CategoryBackendService,
              private snackBarService: SnackBarService,
              private router: Router,
              private formBuilder: FormBuilder,
              private destroyRef: DestroyRef,
              private dialog: MatDialog
  ) {
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      name: [null, [Validators.required, Validators.minLength(5)]],
      description: [null, [Validators.required, Validators.minLength(10)]]
    })
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];

    if (file) {
      this.fileName = file.name;

      this.categoryService.uploadCategories(file).pipe(
        takeUntilDestroyed(this.destroyRef)
      )
        .subscribe({
          next: () => {
            this.router.navigate(['admin/categories']);
            this.snackBarService.success("Categories was loaded successfully");
          },
          error: (error) => {
            this.fileName = '';
            this.snackBarService.error(error.message);
          }
        });
    }
  }

  onSubmit() {
    const dialogRef = this.dialog.open(DialogWindowComponent);
    dialogRef.afterClosed().subscribe((res) => {
      switch (res.event) {
        case "confirm-option":
          this.categoryService.createCategory(this.form.getRawValue()).pipe(
            takeUntilDestroyed(this.destroyRef)
          )
            .subscribe({
              next: () => {
                this.router.navigate(['admin/categories']);
              }
            });

          this.snackBarService.success('Category was successfully created')
          break;

        case "cancel-option":
          dialogRef.close();
          break;
      }
    });
  }
}
