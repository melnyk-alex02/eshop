import { Component, OnInit } from '@angular/core';
import { ItemBackendService } from "../../services/item-backend.service";
import { Subject, switchMap, takeUntil } from "rxjs";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, ParamMap, Router } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";
import { DialogWindowComponent } from "../../component/dialog-window/dialog-window.component";
import { CategoryBackendService } from "../../services/category-backend.service";
import { Item } from "../../models/item";
import { Category } from "../../models/category";
import { SnackBarService } from "../../services/snack-bar.service";

@Component({
  selector: 'app-item-edit',
  templateUrl: './item-edit.component.html',
  styleUrls: ['./item-edit.component.css']
})
export class ItemEditComponent implements OnInit {

  item: Item;
  private unsubscribe: Subject<void> = new Subject();

  categories: Category[];

  form: FormGroup = new FormGroup<any>({});

  constructor(private itemService: ItemBackendService,
              private categoryService: CategoryBackendService,
              private snackBarService: SnackBarService,
              private route: ActivatedRoute,
              private router: Router,
              private formBuilder: FormBuilder,
              private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.route.paramMap
      .pipe(
        takeUntil(this.unsubscribe),
        switchMap((params: ParamMap) => {
          const id = params.get('id');
          return this.itemService.getItemById(Number(id));
        })
      )
      .subscribe((data) => {
        this.item = data;

        this.form = this.formBuilder.group({
          id: [this.item.id],
          name: [this.item.name, [Validators.required, Validators.minLength(5), Validators.maxLength(255)]],
          description: [this.item.description, [Validators.required, Validators.minLength(10)]],
          categoryId: [this.item.categoryId, this.getCategories()],
          imageSrc: [this.item.imageSrc]
        })
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
          this.itemService.updateItem(this.form.getRawValue())
            .pipe(
              takeUntil(this.unsubscribe)
            )
            .subscribe(() => {
              JSON.stringify(this.form.value)
              this.router.navigate(['admin/items'])
            })

          this.snackBarService.success("Item was successfully updated!")

          break;

        case "cancel-option":
          dialogRef.close();
          break;
      }
    });
  }

  getCategories() {
    return this.categoryService.getAllCategories(0, 0, '', '')
      .pipe(
        takeUntil(this.unsubscribe)
      )
      .subscribe((res) => {
        this.categories = res.content;
      })
  }
}
