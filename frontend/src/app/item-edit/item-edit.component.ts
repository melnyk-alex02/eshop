import {Component, OnInit} from '@angular/core';
import {Item, ItemBackendService} from "../services/item-backend.service";
import {Subject, switchMap, takeUntil} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {DialogWindowComponent} from "../dialog-window/dialog-window.component";
import {MatSnackBar} from "@angular/material/snack-bar";
import {CategoryBackendService} from "../services/category-backend.service";

@Component({
  selector: 'app-item-edit',
  templateUrl: './item-edit.component.html',
  styleUrls: ['./item-edit.component.css']
})
export class ItemEditComponent implements OnInit {

  item: Item;
  private unsubscribe: Subject<void> = new Subject();

  loading: boolean;

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
    this.loading = true;
    this.route.paramMap.pipe(
      takeUntil(this.unsubscribe),
      switchMap((params: ParamMap) => {
        const id = params.get('id');

        this.item.id = Number(id);

        return this.itemService.getItemById(Number(id));
      })
    )
      .subscribe((data) => {
        this.item = data;

        console.log(this.item);
      })
      .add(() => {
        this.loading = false;
      })

    this.form = this.formBuilder.group({
      id: [this.item.id],
      name: [null, [Validators.required, Validators.minLength(5)]],
      description: [null, [Validators.required, Validators.minLength(10)]],
      categoryId: [null, this.getCollection()],
      imageSrc: [null, [Validators.required, Validators.minLength(10)]]
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
      this.collection = res;
    })
  }
}
