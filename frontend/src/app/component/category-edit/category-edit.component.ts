import { Component, OnInit } from '@angular/core';
import {  CategoryBackendService } from "../../services/category-backend.service";
import { ActivatedRoute, ParamMap, Router } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { DialogWindowComponent } from "../dialog-window/dialog-window.component";
import { Subject, switchMap, takeUntil } from "rxjs";
import { Category } from "../../models/category";

@Component({
  selector: 'app-category-edit',
  templateUrl: './category-edit.component.html',
  styleUrls: ['./category-edit.component.css']
})
export class CategoryEditComponent implements OnInit {
  category: Category;

  private unsubscribe: Subject<void> = new Subject();

  form: FormGroup;

  constructor(private categoryService: CategoryBackendService,
              private route: ActivatedRoute,
              private router: Router,
              private formBuilder: FormBuilder,
              private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.route.paramMap.pipe(
      takeUntil(this.unsubscribe),
      switchMap((params: ParamMap) => {
        const id = params.get('id');
        return this.categoryService.getCategoryById(Number(id));
      })
    )
      .subscribe((data) => {
        this.category = data;

        this.form = this.formBuilder.group({
          id: [this.category.id],
          name: [this.category.name, [Validators.required, Validators.minLength(5)]],
          description: [this.category.description, [Validators.required, Validators.minLength(10)]]
        })
      })
  }

  ngOnDestroy() {
    this.unsubscribe.complete();
    this.unsubscribe.next();
  }

  onSubmit() {
    const dialogRef = this.dialog.open(DialogWindowComponent);
    dialogRef.afterClosed().subscribe((res) => {
      switch (res.event) {
        case "confirm-option":
          this.categoryService.updateCategory(this.form.getRawValue()).subscribe(() => {
            JSON.stringify(this.form.value);
            this.router.navigate(['admin/categories'])
          });

          break;

        case "cancel-option":
          dialogRef.close();
          break;
      }
    });
  }
}
