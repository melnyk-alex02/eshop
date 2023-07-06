import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ItemBackendService } from "../../services/item-backend.service";
import { MatTable, MatTableDataSource } from "@angular/material/table";
import { Router } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";
import { DialogWindowComponent } from "../../component/dialog-window/dialog-window.component";
import { Subject, takeUntil } from "rxjs";
import { MatPaginator, PageEvent } from "@angular/material/paginator";
import { Item } from "../../models/item";
import { MatSort, Sort } from "@angular/material/sort";
import { GlobalState } from "../../store/states/global.state";
import { Store } from "@ngrx/store";
import { changingItemPagination, changingItemSorting, loadingItems } from "../../store/actions/item.actions"
import { SnackBarService } from "../../services/snack-bar.service";

@Component({
  selector: 'app-item-list',
  templateUrl: './item-list.component.html',
  styleUrls: ['./item-list.component.css']
})

export class ItemListComponent implements OnInit, OnDestroy {
  totalElements: number;
  loading: boolean;

  dataSource = new MatTableDataSource<Item>();
  displayedColumns: string[] = ['id', "name", "description", "categoryName", "imageSrc", "actions"];

  currentPage: number;
  currentSize: number;

  currentSortField: string;
  currentDirection: string | any;

  private unsubscribe: Subject<void> = new Subject();

  @ViewChild(MatTable) table: any;

  @ViewChild(MatPaginator) matPaginator: MatPaginator;

  @ViewChild(MatSort, {static: true}) matSort: MatSort;

  constructor(private itemService: ItemBackendService,
              private snackBarService: SnackBarService,
              public readonly router: Router,
              public dialog: MatDialog,
              public store: Store<GlobalState>) {
  }

  ngOnInit() {
    this.dataSource.paginator = this.matPaginator;
    this.dataSource.sort = this.matSort;

    this.store.select('item').pipe(
      takeUntil(this.unsubscribe)
    )
      .subscribe((data) => {
        this.currentSize = data.pagination.pageSize;
        this.currentPage = data.pagination.pageIndex

        this.currentSortField = data.sorting.sortField;
        this.currentDirection = data.sorting.sortDirection;

      })

    this.store.dispatch(loadingItems({
        pageIndex: this.currentPage,
        pageSize: this.currentSize,
        sortField: this.currentSortField,
        sortDirection: this.currentDirection
      })
    );

    this.store.select('item').pipe(
      takeUntil(this.unsubscribe)
    )
      .subscribe(
        (data) => {
          this.dataSource = new MatTableDataSource(data.data.content)

          this.loading = data.loading;

          this.totalElements = data.data.totalElements;
        }
      );
  }

  ngOnDestroy() {
    this.unsubscribe.next();
    this.unsubscribe.complete();
  }

  deleteItem(id: number) {
    const dialogRef = this.dialog.open(DialogWindowComponent);

    dialogRef.afterClosed().pipe(
      takeUntil(this.unsubscribe)
    )
      .subscribe((res) => {
        switch (res.event) {
          case "confirm-option": {
            this.itemService.deleteItem(id)
              .pipe(
                takeUntil(this.unsubscribe)
              )
              .subscribe(() => {
                  this.store.dispatch(loadingItems({
                    pageIndex: this.currentPage,
                    pageSize: this.currentSize,
                    sortField: this.currentSortField,
                    sortDirection: this.currentDirection
                  }))
                },
                (error) => {
                  this.snackBarService.error(`${error.message}`);
                });

            this.snackBarService.success('Item was deleted successfully!');
            break;
          }
          case "cancel-option": {
            break;
          }
        }
      });
  }

  pageChanged(event: PageEvent) {
    this.currentPage = event.pageIndex;
    this.currentSize = event.pageSize;

    this.store.dispatch(changingItemPagination({
      pageIndex: this.currentPage,
      pageSize: this.currentSize
    }));

    this.store.dispatch(loadingItems({
      pageIndex: this.currentPage,
      pageSize: this.currentSize,
      sortField: this.currentSortField,
      sortDirection: this.currentDirection
    }));
  }

  sortChanged(event: Sort) {
    this.currentSortField = event.active;
    this.currentDirection = event.direction;

    this.store.dispatch(changingItemSorting({
      sortField: this.currentSortField,
      sortDirection: this.currentDirection
    }));

    this.itemService.getAllItems(
      this.currentPage,
      this.currentSize,
      this.currentSortField,
      this.currentDirection
    ).pipe(
      takeUntil(this.unsubscribe)
    )
      .subscribe(
        (data) => {
          this.dataSource.data = data.content;
        }
      );
  }
}
