import { Component, OnInit } from '@angular/core';
import { ItemBackendService } from "../../services/item-backend.service";
import { Subject, switchMap, takeUntil } from "rxjs";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, ParamMap, Router } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";
import { DialogWindowComponent } from "../dialog-window/dialog-window.component";
import { MatSnackBar } from "@angular/material/snack-bar";
import { CategoryBackendService } from "../../services/category-backend.service";
import { Item } from "../../models/item";

@Component({
  selector: 'app-item-edit',
  templateUrl: './item-edit.component.html',
  styleUrls: ['./item-edit.component.css']
})
export class ItemEditComponent implements OnInit {

  item: Item;
  private unsubscribe: Subject<void> = new Subject();

  collection: any[];

  //@ts-ignore
  form: FormGroup = new FormGroup<Item>({});

  constructor(private itemService: ItemBackendService,
              private categoryService: CategoryBackendService,
              private route: ActivatedRoute,
              private router: Router,
              private formBuilder: FormBuilder,
              private dialog: MatDialog,
              private snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.route.paramMap.pipe(
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
          name: [this.item.name, [Validators.required, Validators.minLength(5)]],
          description: [this.item.description, [Validators.required, Validators.minLength(10)]],
          categoryId: [this.item.categoryId, this.getCollection()],
          imageSrc: [this.item.imageSrc, [Validators.required, Validators.minLength(10)]]
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
          this.itemService.updateItem(this.form.getRawValue()).subscribe(() => {
            JSON.stringify(this.form.value)
            this.router.navigate(['admin/items'])
          })

          this.snackBar.open("Item was successfully updated!", 'OK', {
            duration: 5000
          })

          break;

        case "cancel-option":
          dialogRef.close();
          break;
      }
    });
  }

  getCollection() {
    return this.categoryService.getAllCategories().subscribe((res) => {
      this.collection = res.content;
    })
  }
}
