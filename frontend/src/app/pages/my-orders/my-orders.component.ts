import { Component, DestroyRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatTable, MatTableDataSource } from "@angular/material/table";
import { Order } from "../../models/order";
import { Subject, takeUntil } from "rxjs";
import { OrderBackendService } from "../../services/order-backend.service";
import { DialogWindowComponent } from "../../component/dialog-window/dialog-window.component";
import { SnackBarService } from "../../services/snack-bar.service";
import { MatDialog } from "@angular/material/dialog";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";

@Component({
  selector: 'app-my-orders',
  templateUrl: './my-orders.component.html',
  styleUrls: ['./my-orders.component.css']
})
export class MyOrdersComponent implements OnInit {
  loading: boolean;
  dataSource = new MatTableDataSource<Order>();
  displayedColumns: string[] = ['number', 'status', 'createdAt', 'purchasedAt', 'price', 'actions']
  @ViewChild(MatTable) table: any;

  constructor(private orderService: OrderBackendService,
              private snackBarService: SnackBarService,
              public dialog: MatDialog,
              private destroyRef: DestroyRef) {
  }

  ngOnInit() {
    this.getOrders();
  }

  getOrders() {
    this.loading = true;
    this.orderService.getAllOrdersByUserId().pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: (data) => {
        this.dataSource.data = data.content;
      },
      error: (error) => {
        this.loading=false;
        console.log(error)
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
                takeUntilDestroyed(this.destroyRef)
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
