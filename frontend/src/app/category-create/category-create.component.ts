import { Component, OnInit } from '@angular/core';
import { Category, CategoryBackendService } from "../services/category-backend.service";
import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { DialogWindowComponent } from "../dialog-window/dialog-window.component";

@Component({
  selector: 'app-category-create',
  templateUrl: './category-create.component.html',
  styleUrls: ['./category-create.component.css']
})
export class CategoryCreateComponent implements OnInit {

  category: Category = new Category();

  // @ts-ignore
  form: FormGroup = new FormGroup<Category>({});

  constructor(private categoryService: CategoryBackendService,
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

  onSubmit() {
    const dialogRef = this.dialog.open(DialogWindowComponent);
    dialogRef.afterClosed().subscribe((res) => {
      switch (res.event) {
        case "confirm-option":
          this.categoryService.createCategory(this.form.getRawValue()).subscribe(() => {
            JSON.stringify(this.form.value);
          });

          this.router.navigateByUrl('/admin/categories').then(r => window.location.reload());

          break;

        case "cancel-option":
          dialogRef.close();
          break;
      }
    });
  }
}
