import { Component, DestroyRef, OnInit, ViewChild } from '@angular/core';
import { Order } from "../../models/order";
import { OrderBackendService } from "../../services/order-backend.service";
import { switchMap } from "rxjs";
import { ActivatedRoute, ParamMap, Router } from "@angular/router";
import { MatTable, MatTableDataSource } from "@angular/material/table";
import { OrderItem } from "../../models/orderItem";
import { SnackBarService } from "../../services/snack-bar.service";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";

@Component({
  selector: 'app-my-order-page',
  templateUrl: './order-view.component.html',
  styleUrls: ['./order-view.component.css']
})
export class OrderViewComponent implements OnInit {
  order: Order;

  loading: boolean;

  date: Date;
  dataSource = new MatTableDataSource<OrderItem>();
  displayedColumns: string[] = ['itemName', 'itemPrice', "itemCount"]
  @ViewChild(MatTable) table: any;

  constructor(private orderService: OrderBackendService,
              private snackBarService: SnackBarService,
              private destroyRef: DestroyRef,
              private route: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit(): void {
    this.getOrder();
  }

  getOrder() {
    if (localStorage.getItem("email")) {
      console.log("getOrderByEmailAndOrderNumber")
      this.getOrderByEmailAndOrderNumber();
    } else {
      this.loading = true;
      this.route.paramMap
        .pipe(
          takeUntilDestroyed(this.destroyRef),
          switchMap((params: ParamMap) => {
            const orderNumber = params.get('orderNumber');
            return this.orderService.getOrderForCurrentUser(String(orderNumber));
          })
        ).subscribe({
        next: (data) => {
          this.dataSource.data = data.orderItemDTOList;
          this.order = data;
        },
        error: (error) => {
          if (error.status == 401) {
            this.snackBarService.error("You must login or register to view your order");
          } else {
            this.snackBarService.error(`${error.error.message}`)
          }
        },
        complete: () => {
          this.loading = false;
        }
      });
    }
  }

  getOrderByEmailAndOrderNumber() {
    this.loading = true;
    this.route.paramMap
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        switchMap((params: ParamMap) => {
          const orderNumber = params.get('orderNumber');
          return this.orderService.getOrderByEmailAndOrderForUnauthenticatedUsers(localStorage.getItem("email")!, String(orderNumber));
        })
      ).subscribe({
      next: (data) => {
        this.dataSource.data = data.orderItemDTOList;
        this.order = data;
      },
      error: (error) => {
        this.snackBarService.error(`${error.message}`)
      },
      complete: () => {
        this.loading = false;
      }
    });
  }

  confirmOrder() {
    this.orderService.confirmOrder(this.order.number).pipe(
      takeUntilDestroyed(this.destroyRef)
    )
      .subscribe({
        error: (error) => {
          if (error.status == 401) {
            this.snackBarService.error("You must login or register to confirm your order")
            this.router.navigate(["login"], {queryParams: {returnUrl: this.router.url}});
          } else {
            this.snackBarService.error(`${error.message}`)
          }
        },
        complete: () => {
          this.snackBarService.success("Your order was successfully confirmed!")
          this.router.navigate(["my-orders"])
        }
      });
  }

  isOrderStatusNew(): boolean {
    return this.order.status == "NEW"
  }
}
