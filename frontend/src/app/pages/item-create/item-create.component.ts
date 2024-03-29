import { Component, OnInit } from '@angular/core';
import { Subject, takeUntil } from "rxjs";
import { ItemBackendService } from "../../services/item-backend.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";
import { DialogWindowComponent } from "../../component/dialog-window/dialog-window.component";
import { CategoryBackendService } from "../../services/category-backend.service";
import { Item } from "../../models/item";
import { Category } from "../../models/category";
import { SnackBarService } from "../../services/snack-bar.service";

@Component({
  selector: 'app-item-create',
  templateUrl: './item-create.component.html',
  styleUrls: ['./item-create.component.css']
})
export class ItemCreateComponent implements OnInit {
  item: Item;

  form: FormGroup;

  categories: Category[];

  fileName = '';

  private unsubscribe: Subject<void> = new Subject();

  constructor(private itemService: ItemBackendService,
              private categoryService: CategoryBackendService,
              private snackBarService: SnackBarService,
              private route: ActivatedRoute,
              private readonly router: Router,
              private formBuilder: FormBuilder,
              private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      name: [null, [Validators.required, Validators.minLength(5)]],
      description: [null, [Validators.required, Validators.minLength(10)]],
      categoryId: [null, this.getCategories()],
      price:[null, [Validators.required, Validators.min(1), Validators.pattern('^\\d*\\.?\\d*$')]],
      imageSrc: [''],
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

      this.itemService.uploadItems(file).pipe(
        takeUntil(this.unsubscribe)
      )
        .subscribe({
          next: () => {
            this.router.navigate(["admin/items"]);
            this.snackBarService.success("Items uploaded successfully");
          },
          error: (error) => {
            this.fileName = '';
            this.snackBarService.error(error.message);
          }
        });
    }
  }

  getCategories() {
    return this.categoryService.getAllCategories(0, 0, '', '')
      .pipe(
        takeUntil(this.unsubscribe)
      )
      .subscribe({
        next: (res) => {
          this.categories = res.content;
        }
      });
  }

  onSubmit() {
    const dialogRef = this.dialog.open(DialogWindowComponent);
    dialogRef.afterClosed().subscribe({
      next: (res) => {
        switch (res.event) {
          case 'confirm-option':
            this.itemService.createItem(this.form.getRawValue())
              .pipe(
                takeUntil(this.unsubscribe)
              )
              .subscribe({
                next: () => {
                  this.router.navigate(['admin/items'])
                }
              });

            this.snackBarService.success('Item was successfully created!')
            break;

          case "cancel-option":
            dialogRef.close();
            break;
        }
      }
    });
  }
}
