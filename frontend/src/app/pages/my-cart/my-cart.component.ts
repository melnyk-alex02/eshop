import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatTable, MatTableDataSource } from "@angular/material/table";
import { CartItem } from "../../models/cartItem";
import { Subject, takeUntil } from "rxjs";
import { CartBackendService } from "../../services/cart-backend.service";
import { SnackBarService } from "../../services/snack-bar.service";
import { Order } from "../../models/order";
import { Router } from "@angular/router";
import { OrderBackendService } from "../../services/order-backend.service";
import { Item } from 'src/app/models/item';

@Component({
  selector: 'app-my-cart',
  templateUrl: './my-cart.component.html',
  styleUrls: ['./my-cart.component.css']
})
export class MyCartComponent implements OnInit, OnDestroy {
  loading: boolean;

  order: Order;

  cart: CartItem[];

  dataSource = new MatTableDataSource<CartItem>();
  displayedColumns: string[] = ["itemName", "itemPrice", "count", "actions"];

  @ViewChild(MatTable) table: any;
  private unsubscribe: Subject<void> = new Subject();

  constructor(private cartService: CartBackendService,
              private orderService: OrderBackendService,
              private snackBarService: SnackBarService,
              private readonly router: Router) {
  }

  ngOnInit() {
    this.getAllCartOfUser();
  }

  ngOnDestroy() {
    this.unsubscribe.complete();
    this.unsubscribe.next();
  }

  getAllCartOfUser() {
    this.loading = true;
    this.cartService.getAllCartOfUser().pipe(
      takeUntil(this.unsubscribe)
    )
      .subscribe(
        {
          next: (data) => {
            this.cart = data;
            console.log(data);
            this.dataSource.data = data;
          },
          error: (error) => {
            this.snackBarService.error("There are no items in cart :(");

            this.dataSource.data = []
            this.loading = false
          },
          complete: () => {
            this.loading = false;
            console.log(this.countItemsInCart())
          }
        }
      )
  }

  changeCountOfItems(id: number, count: number) {
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
    this.loading = true;
    this.cartService.deleteItemFromCart(itemId).pipe(
      takeUntil(this.unsubscribe)
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

  countItemsInCart() {
    return this.cart.reduce((acc, item) => acc + item.count, 0);
  }

  createOrderFromCart() {
    this.loading = true;
    this.cartService.createOrderFromCart().subscribe(
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
          this.snackBarService.success("Order was successfully created. It will be auto-cancelled if you don't confirm it")
          this.loading = false;
        }
      }
    );
  }
}
