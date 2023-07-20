import { Component, OnInit } from '@angular/core';
import { CategoryBackendService } from "../../services/category-backend.service";
import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { DialogWindowComponent } from "../../component/dialog-window/dialog-window.component";
import { Subject, takeUntil } from "rxjs";
import { Category } from "../../models/category";
import { SnackBarService } from "../../services/snack-bar.service";

@Component({
  selector: 'app-category-create',
  templateUrl: './category-create.component.html',
  styleUrls: ['./category-create.component.css']
})
export class CategoryCreateComponent implements OnInit {

  category: Category;

  form: FormGroup;

  fileName = '';

  private unsubscribe: Subject<void> = new Subject();

  constructor(private categoryService: CategoryBackendService,
              private snackBarService: SnackBarService,
              private route: ActivatedRoute,
              private router: Router,
              private formBuilder: FormBuilder,
              private dialog: MatDialog
  ) {
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      name: [null, [Validators.required, Validators.minLength(5)]],
      description: [null, [Validators.required, Validators.minLength(10)]]
    })
  }

  ngOnDestroy() {
    this.unsubscribe.next();
    this.unsubscribe.complete();
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];

    if (file) {
      this.fileName = file.name;

      this.categoryService.uploadCategories(file).pipe(
        takeUntil(this.unsubscribe)
      )
        .subscribe(() => {
            this.router.navigate(['admin/categories']);
            this.snackBarService.success("Categories was loaded successfully");
          },
          (error) => {
            this.fileName = '';
            this.snackBarService.error(error.message);
          });
    }
  }

  onSubmit() {
    const dialogRef = this.dialog.open(DialogWindowComponent);
    dialogRef.afterClosed().subscribe((res) => {
      switch (res.event) {
        case "confirm-option":
          this.categoryService.createCategory(this.form.getRawValue()).pipe(
            takeUntil(this.unsubscribe)
          )
            .subscribe(() => {
              this.router.navigate(['admin/categories']);
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
