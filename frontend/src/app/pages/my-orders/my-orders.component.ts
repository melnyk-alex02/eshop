import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatTable, MatTableDataSource } from "@angular/material/table";
import { Order } from "../../models/order";
import { Subject, takeUntil } from "rxjs";
import { OrderBackendService } from "../../services/order-backend.service";
import { ActivatedRoute } from "@angular/router";
import { DialogWindowComponent } from "../../component/dialog-window/dialog-window.component";
import { SnackBarService } from "../../services/snack-bar.service";
import { MatDialog } from "@angular/material/dialog";

@Component({
  selector: 'app-my-orders',
  templateUrl: './my-orders.component.html',
  styleUrls: ['./my-orders.component.css']
})
export class MyOrdersComponent implements OnInit, OnDestroy {
  loading: boolean;
  dataSource = new MatTableDataSource<Order>();
  displayedColumns: string[] = ['number', 'status', 'createdAt', 'price', 'actions']
  @ViewChild(MatTable) table: any;
  private unsubscribe: Subject<void> = new Subject();

  constructor(private orderService: OrderBackendService,
              private route: ActivatedRoute,
              private snackBarService: SnackBarService,
              public dialog: MatDialog) {
  }

  ngOnInit() {
    this.getOrders();
  }

  ngOnDestroy() {
    this.unsubscribe.complete();
    this.unsubscribe.next();
  }

  getOrders() {
    this.loading = true;
    this.orderService.getAllOrdersByUserId().pipe(
      takeUntil(this.unsubscribe)
    ).subscribe({
      next: (data) => {
        this.dataSource.data = data.content;
        console.log(data);
      }, error: (error) => {
        this.loading = false;
        this.dataSource.data = [];
        this.snackBarService.error("There is no order for current user :(");
      },
      complete: () => {
        this.loading = false;
      }
    });
  }

  cancelOrder(orderNumber: string) {
    const dialogRef = this.dialog.open(DialogWindowComponent);

    dialogRef.afterClosed().subscribe({
      next: (res) => {
        switch (res.event) {
          case "confirm-option": {
            this.orderService.cancelOrder(orderNumber)
              .pipe(
                takeUntil(this.unsubscribe)
              )
              .subscribe({
                next: () => {
                  this.getOrders();
                },
                error: (error) => {
                  this.snackBarService.error(`${error.message}`);
                }
              });

            this.snackBarService.success('Order was cancelled successfully!');
            break;
          }
          case "cancel-option": {
            break;
          }
        }
      }
    });
  }
}
