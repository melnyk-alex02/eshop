import { Component, OnInit } from '@angular/core';
import { CategoryBackendService } from "../../services/category-backend.service";
import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { DialogWindowComponent } from "../../component/dialog-window/dialog-window.component";
import { Subject } from "rxjs";
import { Category } from "../../models/category";

@Component({
  selector: 'app-category-create',
  templateUrl: './category-create.component.html',
  styleUrls: ['./category-create.component.css']
})
export class CategoryCreateComponent implements OnInit {

  category: Category;

  form: FormGroup;

  private unsubscribe: Subject<void> = new Subject();

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

  ngOnDestroy() {
    this.unsubscribe.next();
    this.unsubscribe.complete();
  }

  onSubmit() {
    const dialogRef = this.dialog.open(DialogWindowComponent);
    dialogRef.afterClosed().subscribe((res) => {
      switch (res.event) {
        case "confirm-option":
          this.categoryService.createCategory(this.form.getRawValue()).subscribe(() => {
            JSON.stringify(this.form.value);
            this.router.navigate(['admin/categories']);
          });

          break;

        case "cancel-option":
          dialogRef.close();
          break;
      }
    });
  }
}
