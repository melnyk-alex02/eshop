import { Component, DestroyRef, OnInit } from '@angular/core';
import { Order } from "../../models/order";
import { OrderBackendService } from "../../services/order-backend.service";
import { MatTableDataSource } from "@angular/material/table";
import { SnackBarService } from "../../services/snack-bar.service";
import { PageEvent } from "@angular/material/paginator";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";

@Component({
  selector: 'app-orders-list',
  templateUrl: './orders-list.component.html',
  styleUrls: ['./orders-list.component.css']
})
export class OrdersListComponent implements OnInit {
  dataSource = new MatTableDataSource<Order>();

  loading: boolean;

  currentPage: number;
  currentSize: number;

  totalElements: number;

  displayedColumns: string[] = ["number", "status", "createdDate", "price", "items", "purchasedDate"];

  constructor(private orderService: OrderBackendService,
              private snackBarService: SnackBarService,
              private destroyRef: DestroyRef) {
  }

  ngOnInit() {
    this.getOrders(0, 5);
  }


  getOrders(pageNumber: number, pageSize: number) {
    this.loading = true;
    this.orderService.getOrders(pageNumber, pageSize).pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: (data) => {
        this.dataSource.data = data.content;
        this.totalElements = data.totalElements;
      },
      error: (error) => {
        this.snackBarService.error(error.message);
        this.loading = false;
      },
      complete: () => {
        this.loading = false;
      }
    })
  }

  pageChanged(event: PageEvent) {
    this.currentPage = event.pageIndex;
    this.currentSize = event.pageSize;

    this.getOrders(this.currentPage, this.currentSize);
  }
}
