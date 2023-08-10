import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ItemBackendService } from "../../services/item-backend.service";
import { StatsBackendService } from "../../services/stats-backend.service";
import { Item } from "../../models/item";
import { Stats } from "../../models/stats";
import { Subject, takeUntil } from "rxjs";
import { MatTable, MatTableDataSource } from "@angular/material/table";
import { MatPaginator, PageEvent } from "@angular/material/paginator";
import { SnackBarService } from "../../services/snack-bar.service";

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent implements OnInit, OnDestroy {

  lastFiveItemsList: Item[];

  dataSource = new MatTableDataSource<Stats>();
  displayedColumns: string[] = ['category', 'itemsCount'];

  totalElements: number;

  currentPage: number = 0;
  currentSize: number = 5;

  loading: boolean;

  @ViewChild(MatTable) table: any;
  @ViewChild(MatPaginator) matPaginator: MatPaginator;

  private unsubscribe: Subject<void> = new Subject();

  constructor(private itemService: ItemBackendService,
              private statsService: StatsBackendService,
              private snackBarService: SnackBarService) {
  }

  ngOnInit() {
    this.dataSource.paginator = this.matPaginator;

    this.getAllStats();

    this.itemService.getLastFiveAddedItems().pipe(
      takeUntil(this.unsubscribe)
    )
      .subscribe((data) => {
          this.loading = true;

          this.lastFiveItemsList = data;
        },
        (error) => {
          this.snackBarService.error(error.message);
          this.loading = false;
        },
        () => {
          this.loading = false;
        })
  }

  ngOnDestroy() {
    this.unsubscribe.next();
    this.unsubscribe.complete();
  }

  pageChanged(event: PageEvent) {
    this.currentPage = event.pageIndex;
    this.currentSize = event.pageSize;

    this.getAllStats();
  }

  getAllStats() {
    this.statsService.getStats(
      this.currentPage,
      this.currentSize
    ).pipe(
        takeUntil(this.unsubscribe)
      )
      .subscribe((data) => {
          console.log(data.content);

          this.loading = true;

          this.dataSource.data = data.content;

          this.totalElements = data.totalElements;
        },
        (error) => {
          this.loading = false;

          this.snackBarService.error(error.message);
        },
        () => {
          this.loading = false;
        })
  }
}
