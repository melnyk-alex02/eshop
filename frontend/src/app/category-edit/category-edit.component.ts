import { Component, OnInit } from '@angular/core';
import { Category, CategoryBackendService } from "../services/category-backend.service";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { DialogWindowComponent } from "../dialog-window/dialog-window.component";
import {Subject, switchMap, takeUntil} from "rxjs";

@Component({
  selector: 'app-category-edit',
  templateUrl: './category-edit.component.html',
  styleUrls: ['./category-edit.component.css']
})
export class CategoryEditComponent implements OnInit {
  category: Category;

  private unsubscribe: Subject<void> = new Subject();

  loading: boolean;

  // @ts-ignore
  form: FormGroup = new FormGroup<Category>({});

  constructor(private categoryService: CategoryBackendService,
              private route: ActivatedRoute,
              private router: Router,
              private formBuilder: FormBuilder,
              private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.route.paramMap.pipe(
      takeUntil(this.unsubscribe),
        switchMap((params: ParamMap) =>{
        const  id = params.get('id');

        this.category.id = Number(id);

        return this.categoryService.getCategoryById(Number(id));
      })
    )
      .subscribe((data) =>{
        this.category = data;
      })
      .add(()=>{
        this.loading = true;
      })

    this.form = this.formBuilder.group({
      id: [this.category.id],
      name: [null, [Validators.required, Validators.minLength(5)]],
      description: [null, [Validators.required, Validators.minLength(10)]]
    })
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
