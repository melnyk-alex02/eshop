import { Component, DestroyRef, OnInit, ViewChild } from '@angular/core';
import { ItemBackendService } from "../../services/item-backend.service";
import { StatsBackendService } from "../../services/stats-backend.service";
import { Item } from "../../models/item";
import { Stats } from "../../models/stats";
import { MatTable, MatTableDataSource } from "@angular/material/table";
import { MatPaginator, PageEvent } from "@angular/material/paginator";
import { SnackBarService } from "../../services/snack-bar.service";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent implements OnInit {

  lastFiveItemsList: Item[];

  dataSource = new MatTableDataSource<Stats>();
  displayedColumns: string[] = ['category', 'itemsCount'];

  totalElements: number;

  currentPage: number = 0;
  currentSize: number = 5;

  @ViewChild(MatTable) table: any;
  @ViewChild(MatPaginator) matPaginator: MatPaginator;

  constructor(private itemService: ItemBackendService,
              private statsService: StatsBackendService,
              private destroyRef: DestroyRef,
              private snackBarService: SnackBarService) {
  }

  ngOnInit() {
    this.dataSource.paginator = this.matPaginator;

    this.getAllStats();

    this.itemService.getLastFiveAddedItems().pipe(
      takeUntilDestroyed(this.destroyRef)
    )
      .subscribe({
        next: (data) => {
          this.lastFiveItemsList = data;
        },
        error: (error) => {
          this.snackBarService.error(error.message);
        }
      })
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
      takeUntilDestroyed(this.destroyRef)
    )
      .subscribe({
        next: (data) => {
          this.dataSource.data = data.content;

          this.totalElements = data.totalElements;
        },
        error: (error) => {
          this.snackBarService.error(error.message);
        }
      })
  }
}
