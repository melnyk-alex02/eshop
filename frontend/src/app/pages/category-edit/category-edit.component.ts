import { Component, DestroyRef, OnInit } from '@angular/core';
import { CategoryBackendService } from "../../services/category-backend.service";
import { ActivatedRoute, ParamMap, Router } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { DialogWindowComponent } from "../../component/dialog-window/dialog-window.component";
import { switchMap } from "rxjs";
import { Category } from "../../models/category";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";

@Component({
  selector: 'app-category-edit',
  templateUrl: './category-edit.component.html',
  styleUrls: ['./category-edit.component.css']
})
export class CategoryEditComponent implements OnInit {
  category: Category;

  form: FormGroup;

  constructor(private categoryService: CategoryBackendService,
              private route: ActivatedRoute,
              private router: Router,
              private formBuilder: FormBuilder,
              private destroyRef: DestroyRef,
              private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.route.paramMap.pipe(
      takeUntilDestroyed(this.destroyRef),
      switchMap((params: ParamMap) => {
        const id = params.get('id');
        return this.categoryService.getCategoryById(Number(id));
      })
    )
      .subscribe({
        next: (data) => {
          this.category = data;

          this.form = this.formBuilder.group({
            id: [this.category.id],
            name: [this.category.name, [Validators.required, Validators.minLength(5)]],
            description: [this.category.description, [Validators.required, Validators.minLength(10)]]
          })
        }
      });
  }

  onSubmit() {
    const dialogRef = this.dialog.open(DialogWindowComponent);
    dialogRef.afterClosed().subscribe({
      next: (res) => {
        switch (res.event) {
          case "confirm-option":
            this.categoryService.updateCategory(this.form.getRawValue())
              .pipe(
                takeUntilDestroyed(this.destroyRef)
              )
              .subscribe({
                next: () => {
                  this.router.navigate(['admin/categories'])
                }
              });

            break;

          case "cancel-option":
            dialogRef.close();
            break;
        }
      }
    });
  }
}
