import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Order} from "../../models/order";
import {OrderBackendService} from "../../services/order-backend.service";
import {Subject, switchMap, takeUntil} from "rxjs";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {MatTable, MatTableDataSource} from "@angular/material/table";
import {OrderItem} from "../../models/orderItem";
import {SnackBarService} from "../../services/snack-bar.service";

@Component({
  selector: 'app-my-order-page',
  templateUrl: './my-order-page.component.html',
  styleUrls: ['./my-order-page.component.css']
})
export class MyOrderPageComponent implements OnInit, OnDestroy {
  order: Order;

  loading: boolean;

  date: Date;

  private unsubscribe: Subject<void> = new Subject();

  dataSource = new MatTableDataSource<OrderItem>();
  displayedColumns: string[] = ['itemName', 'itemPrice', "itemCount"]
  @ViewChild(MatTable) table: any;

  constructor(private orderService: OrderBackendService,
              private snackBarServe: SnackBarService,
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
          return this.orderService.getOrder(String(id));
        })
      ).subscribe({
      next: (data) => {
        this.dataSource.data = data.orderItemDTOList;
        console.log(this.dataSource.data)
        this.order = data;
        console.log(data.createdDate.toLocaleDateString)
      },
      error: () => {

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
        error:(error) => {
          console.error(error.message)
        },
        complete:() => {
          this.snackBarServe.success("Your order was successfully confirmed!")
          this.router.navigate(["my-orders"])
    }
      });
  }

  isOrderStatusNew(): boolean {
    return this.order.status == "NEW"
  }
}
