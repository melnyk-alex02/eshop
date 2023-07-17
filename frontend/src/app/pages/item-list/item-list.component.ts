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
import { changingItemPagination, changingItemSorting } from "../../store/actions/item.actions"
import { SnackBarService } from "../../services/snack-bar.service";
import { selectItemPagination, selectItemSorting } from "../../store/selectors/item.selectors";

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

  sorting$;

  pagination$;

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
    this.pagination$ = this.store.select(selectItemPagination);

    this.sorting$ = this.store.select(selectItemSorting);
  }

  ngOnInit() {
    this.dataSource.paginator = this.matPaginator;
    this.dataSource.sort = this.matSort;

    this.pagination$.pipe(
      takeUntil(this.unsubscribe)
    )
      .subscribe((pagination) => {
        this.currentPage = pagination.pageIndex;
        this.currentSize = pagination.pageSize;
      });

    this.sorting$.pipe(
      takeUntil(this.unsubscribe)
    )
      .subscribe((sorting) => {
      this.currentSortField = sorting.sortField;
      this.currentDirection = sorting.sortDirection;
    });

    this.itemService.getAllItems(
      this.currentPage,
      this.currentSize,
      this.currentSortField,
      this.currentDirection
    ).pipe(
      takeUntil(this.unsubscribe)
    )
      .subscribe((data) => {
        this.dataSource.data = data.content;

        this.totalElements = data.totalElements;
      });
  }

  ngOnDestroy() {
    this.unsubscribe.next();
    this.unsubscribe.complete();
  }

  deleteItem(id: number) {
    const dialogRef = this.dialog.open(DialogWindowComponent);

    dialogRef.afterClosed().subscribe((res) => {
      switch (res.event) {
        case "confirm-option": {
          this.itemService.deleteItem(id)
            .pipe(
              takeUntil(this.unsubscribe)
            )
            .subscribe(() => {
                this.itemService.getAllItems(
                  this.currentPage,
                  this.currentSize,
                  this.currentSortField,
                  this.currentDirection
                )
                  .pipe(
                    takeUntil(this.unsubscribe)
                  )
                  .subscribe((data) => {
                    this.dataSource.data = data.content;
                  });
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

    this.itemService.getAllItems(
      this.currentPage,
      this.currentSize,
      this.currentSortField,
      this.currentDirection
    ).pipe(
      takeUntil(this.unsubscribe)
    )
      .subscribe((data) => {
        this.dataSource.data = data.content;
      })
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
    )
      .pipe(
        takeUntil(this.unsubscribe)
      )
      .subscribe(
        (data) => {
          this.dataSource.data = data.content;
        }
      );
  }
}
