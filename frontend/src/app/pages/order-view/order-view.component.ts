import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Order } from "../../models/order";
import { OrderBackendService } from "../../services/order-backend.service";
import { Subject, switchMap, takeUntil } from "rxjs";
import { ActivatedRoute, ParamMap, Router } from "@angular/router";
import { MatTable, MatTableDataSource } from "@angular/material/table";
import { OrderItem } from "../../models/orderItem";
import { SnackBarService } from "../../services/snack-bar.service";
import { KeycloakService } from "keycloak-angular";

@Component({
  selector: 'app-my-order-page',
  templateUrl: './order-view.component.html',
  styleUrls: ['./order-view.component.css']
})
export class OrderViewComponent implements OnInit, OnDestroy {
  order: Order;

  loading: boolean;

  date: Date;
  dataSource = new MatTableDataSource<OrderItem>();
  displayedColumns: string[] = ['itemName', 'itemPrice', "itemCount"]
  @ViewChild(MatTable) table: any;
  private unsubscribe: Subject<void> = new Subject();

  constructor(private orderService: OrderBackendService,
              private snackBarService: SnackBarService,
              private keycloakService: KeycloakService,
              private route: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit(): void {
    this.getOrder();
  }

  ngOnDestroy() {
    this.unsubscribe.complete();
    this.unsubscribe.next();
  }

  getOrder() {
    this.loading = true;
    this.route.paramMap
      .pipe(
        takeUntil(this.unsubscribe),
        switchMap((params: ParamMap) => {
          const id = params.get('orderNumber');
          return this.orderService.getOrderForCurrentUser(String(id));
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
      takeUntil(this.unsubscribe)
    )
      .subscribe({
        error: (error) => {
          this.snackBarService.error(`${error.message}`)
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
