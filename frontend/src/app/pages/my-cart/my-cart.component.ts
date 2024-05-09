import { Component, DestroyRef, OnInit, ViewChild } from '@angular/core';
import { MatTable, MatTableDataSource } from "@angular/material/table";
import { CartItem } from "../../models/cartItem";
import { CartBackendService } from "../../services/cart-backend.service";
import { SnackBarService } from "../../services/snack-bar.service";
import { Order } from "../../models/order";
import { Router } from "@angular/router";
import { User } from "../../models/user";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { create } from "lodash";

@Component({
  selector: 'app-my-cart',
  templateUrl: './my-cart.component.html',
  styleUrls: ['./my-cart.component.css']
})

export class MyCartComponent implements OnInit {
  loading: boolean;

  order: Order;

  cart: CartItem[];

  user: User;

  email: string;

  form: FormGroup;

  cartItemList: CartItem[];

  dataSource = new MatTableDataSource<CartItem>();
  displayedColumns: string[] = ["itemName", "itemPrice", "count", "actions"];

  @ViewChild(MatTable) table: any;
  protected readonly localStorage = localStorage;
  protected readonly create = create;

  constructor(private cartService: CartBackendService,
              private snackBarService: SnackBarService,
              private formBuilder: FormBuilder,
              private readonly router: Router,
              private destroyRef: DestroyRef,) {
  }

  ngOnInit() {
    this.getAllCartOfUser();

    this.form = this.formBuilder.group({
      email: [null, [Validators.required, Validators.email]],
    })
  }


  getAllCartOfUser() {
    if (localStorage.getItem("user")) {
      this.loading = true;
      this.cartService.getAllCartOfUser().pipe(
        takeUntilDestroyed(this.destroyRef)
      )
        .subscribe(
          {
            next: (data) => {
              this.cart = data;
              this.dataSource.data = data;
              localStorage.setItem("countOfItemsInCart", data.length.toString());
            },
            complete: () => {
              this.loading = false;
            }
          }
        )
    } else {
      this.dataSource.data = this.getCartItemFromLocalStorage();
    }
  }

  changeCountOfItems(id: number, count: number) {
    if (!localStorage.getItem("user")) {
      this.cartItemList = this.getCartItemFromLocalStorage();
      if (this.cartItemList) {
        this.cartItemList.forEach((item: CartItem) => {
          if (item.itemId == id) {
            item.count = count;
          }
        });
        localStorage.setItem("cartItemList", JSON.stringify(this.cartItemList));
      }

      this.getAllCartOfUser();
      return;
    }
    this.loading = true;
    this.cartService.updateItemCount(id, count).subscribe({
      error: (error) => {
        this.snackBarService.error(error.message)
        this.loading = false;
        this.getAllCartOfUser();
      },
      complete: () => {
        this.loading = false;
      }
    })
  }

  deleteItemFromCart(itemId: number) {

    if (!localStorage.getItem("user")) {
      this.cartItemList = this.getCartItemFromLocalStorage();
      if (this.cartItemList) {
        this.cartItemList = this.cartItemList.filter((item: CartItem) => item.itemId != itemId);
        localStorage.setItem("cartItemList", JSON.stringify(this.cartItemList));
        localStorage.setItem("countOfItemsInCart", this.cartItemList.length.toString());
        this.getAllCartOfUser();
      }
    } else {
      this.loading = true;

      this.cartService.deleteItemFromCart(itemId).pipe(
        takeUntilDestroyed(this.destroyRef)
      ).subscribe({
        next: () => {
          this.snackBarService.success("Item was successfully deleted from cart!")
        },
        error: (error) => {
          this.snackBarService.error(error.message);
          this.loading = false;
        },
        complete: () => {
          localStorage.setItem("countOfItemsInCart", (this.cart.length - 1).toString());
          this.loading = false;

          this.getAllCartOfUser();

        }
      })
    }
  }

  getCartItemFromLocalStorage() {
    return this.cartItemList = JSON.parse(localStorage.getItem("cartItemList")!);
  }

  countItemsInCart() {
    if (localStorage.getItem("user")) {
      return this.cart.reduce((acc, item) => acc + item.count, 0);
    }
    return this.getCartItemFromLocalStorage().length;

  }

  createOrder() {
    this.cartItemList = this.getCartItemFromLocalStorage();

    const email = this.form.get("email")?.value;

    this.cartService.createOrder(this.cartItemList, email)
      .pipe(
        takeUntilDestroyed(this.destroyRef)
      ).subscribe({
      next: (data) => {
        this.order = data;
      },
      error: (error) => {
        this.snackBarService.error(error.message);
      },
      complete: () => {
        localStorage.setItem("email", email)
        this.router.navigate(["my-order/" + this.order.number])
        localStorage.setItem("countOfItemsInCart", "0");
        this.snackBarService.success("Order was successfully created. It will be auto-cancelled if you don't confirm it");
        this.localStorage.removeItem("cartItemList");
      }
    });
  }

  createOrderFromCart() {
    this.loading = true;
    this.cartService.createOrderFromCart().pipe(
      takeUntilDestroyed(this.destroyRef),
    )
      .subscribe(
        {
          next: (data) => {
            this.order = data;
          },
          error: (error) => {
            this.snackBarService.error(error.message);
            this.loading = false;
          },
          complete: () => {
            this.router.navigate(["my-order/" + this.order.number])
            localStorage.setItem("countOfItemsInCart", "0");
            localStorage.removeItem("cartItemList");
            this.snackBarService.success("Order was successfully created. It will be auto-cancelled if you don't confirm it")
            this.loading = false;
          }
        }
      );
  }
}
