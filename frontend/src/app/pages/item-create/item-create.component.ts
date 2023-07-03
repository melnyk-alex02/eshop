import { Component, OnInit } from '@angular/core';
import { Subject } from "rxjs";
import { ItemBackendService } from "../../services/item-backend.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";
import { DialogWindowComponent } from "../../component/dialog-window/dialog-window.component";
import { CategoryBackendService } from "../../services/category-backend.service";
import { MatSnackBar } from "@angular/material/snack-bar";
import { Item } from "../../models/item";
import { Category } from "../../models/category";

@Component({
  selector: 'app-item-create',
  templateUrl: './item-create.component.html',
  styleUrls: ['./item-create.component.css']
})
export class ItemCreateComponent implements OnInit {
  private unsubscribe: Subject<void> = new Subject();

  item: Item;

  form: FormGroup;

  selected: string;
  categories: Category[];

  constructor(private itemService: ItemBackendService,
              private categoryService: CategoryBackendService,
              private route: ActivatedRoute,
              private readonly router: Router,
              private formBuilder: FormBuilder,
              private dialog: MatDialog,
              private snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      name: [null, [Validators.required, Validators.minLength(5)]],
      description: [null, [Validators.required, Validators.minLength(10)]],
      categoryId: [null, this.getCategories()],
      imageSrc: [null],
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
        case 'confirm-option':
          this.itemService.createItem(this.form.getRawValue()).subscribe(() => {
            JSON.stringify(this.form.value);
            this.router.navigate(['admin/items'])
          })

          this.snackBar.open('Item was successfully created!', 'OK', {
            duration: 5000
          });
          break;

        case "cancel-option":
          dialogRef.close();
          break;
      }
    })
  }

  getCategories() {
    return this.categoryService.getAllCategories(0, 0, '', '').subscribe((res) => {
      this.categories = res.content;
    })
  }
}
