import {Component, OnDestroy, OnInit} from '@angular/core';
import {Order} from "../../models/order";
import {OrderBackendService} from "../../services/order-backend.service";
import {MatTableDataSource} from "@angular/material/table";
import {Subject, takeUntil} from "rxjs";
import {SnackBarService} from "../../services/snack-bar.service";
import {PageEvent} from "@angular/material/paginator";

@Component({
  selector: 'app-orders-list',
  templateUrl: './orders-list.component.html',
  styleUrls: ['./orders-list.component.css']
})
export class OrdersListComponent implements OnInit, OnDestroy{
  dataSource = new MatTableDataSource<Order>();

  loading: boolean;

  currentPage: number;
  currentSize: number;

  totalElements: number;

  displayedColumns: string[] = ["number", "status", "createdDate", "count", "price", "purchasedDate"];

  private unsubscribe: Subject<void> = new Subject();

  constructor(private orderService: OrderBackendService,
              private snackBarService: SnackBarService) {
  }

  ngOnInit() {
    this.getOrders(0, 5);
  }

  ngOnDestroy() {
    this.unsubscribe.complete();
    this.unsubscribe.next();
  }

  getOrders(pageNumber: number, pageSize:number) {
    this.loading = true;
    this.orderService.getOrders(pageNumber, pageSize).pipe(
      takeUntil(this.unsubscribe)
    ).subscribe({
      next: (data) => {
        this.dataSource.data = data.content;
        this.totalElements = data.totalElements;
        console.log(data.content)
    },
      error: (error) => {
        this.snackBarService.error(error.message);
        this.loading = false;
      },
      complete:() => {
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
