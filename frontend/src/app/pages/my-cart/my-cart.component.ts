import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MatTable, MatTableDataSource} from "@angular/material/table";
import {Cart} from "../../models/cart";
import {Subject, takeUntil} from "rxjs";
import {CartBackendService} from "../../services/cart-backend.service";
import {SnackBarService} from "../../services/snack-bar.service";
import {Order} from "../../models/order";
import {Router} from "@angular/router";
import {OrderBackendService} from "../../services/order-backend.service";

@Component({
  selector: 'app-my-cart',
  templateUrl: './my-cart.component.html',
  styleUrls: ['./my-cart.component.css']
})
export class MyCartComponent implements OnInit, OnDestroy {
  loading: boolean;

  order: Order;

  cart: Cart[];

  dataSource = new MatTableDataSource<Cart>();
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
          console.log(`${error.message}`)
          this.snackBarService.error(`${error.message}`)

          this.dataSource.data = []
          this.loading = false
        },
        complete: () => {
          this.loading = false;
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
      error:(error) => {
        this.snackBarService.error(error.message);
        this.loading = false;
      },
      complete: () => {
        this.snackBarService.success("Item was successfully deleted from cart!")
        this.loading = false;
        this.getAllCartOfUser();
      }
    })
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
          this.snackBarService.success("Order was successfully created. It will be auto-cancelled if you don't confirm it")
          this.loading = false;
          this.orderService.startAutoCancelCheck(this.order);
        }
      }
    );
  }
}
