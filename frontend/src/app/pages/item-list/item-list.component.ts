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
import { changingItemFiltering, changingItemPagination, changingItemSorting } from "../../store/actions/item.actions"
import { SnackBarService } from "../../services/snack-bar.service";
import { selectItemFiltering, selectItemPagination, selectItemSorting } from "../../store/selectors/item.selectors";
import { CategoryBackendService } from "../../services/category-backend.service";
import { Category } from "../../models/category";

@Component({
  selector: 'app-item-list',
  templateUrl: './item-list.component.html',
  styleUrls: ['./item-list.component.css']
})

export class ItemListComponent implements OnInit, OnDestroy {
  categories: Category[];

  totalElements: number;

  loading: boolean;

  dataSource = new MatTableDataSource<Item>();
  displayedColumns: string[] = ['id', "name", "description", "categoryName", "imageSrc", "actions"];

  sorting$;

  pagination$;

  filtering$;

  currentPage: number;
  currentSize: number;

  currentSortField: string;
  currentDirection: string | any;

  filterName: string;
  filterHasImage: boolean | string = '';
  filterCategoryId: number | string = '';
  filterPage: number;

  @ViewChild(MatTable) table: any;
  @ViewChild(MatPaginator) matPaginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) matSort: MatSort;
  private unsubscribe: Subject<void> = new Subject();

  constructor(private itemService: ItemBackendService,
              private categoryService: CategoryBackendService,
              private snackBarService: SnackBarService,
              public readonly router: Router,
              public dialog: MatDialog,
              public store: Store<GlobalState>) {
    this.pagination$ = this.store.select(selectItemPagination);

    this.sorting$ = this.store.select(selectItemSorting);

    this.filtering$ = this.store.select(selectItemFiltering);
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

    this.filtering$.pipe(
      takeUntil(this.unsubscribe)
    )
      .subscribe((filtering: any) => {
        this.filterName = filtering.name;
        this.filterHasImage = filtering.hasImage;
        this.filterCategoryId = filtering.categoryId;
        this.filterPage = filtering.filterPage
      });

    if (this.filterName && this.filterName.length >= 3) {
      this.searchItems(this.filterPage);
    } else {
      this.filterName = '';
      this.filterCategoryId = '';
      this.filterHasImage = '';
      this.filterPage = 0;

      this.getAllItems();
    }

    this.categoryService.getAllCategories(0, 0, '', '').pipe(
      takeUntil(this.unsubscribe)
    )
      .subscribe((data) => {
        this.categories = data.content;
      })
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
                this.getAllItems();
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

    if (this.filterName.length >= 3) {
      this.filterPage = event.pageIndex;

      this.searchItems(this.filterPage);
    } else {
      this.matPaginator.pageIndex = event.pageIndex;

      this.getAllItems()
    }
  }

  sortChanged(event: Sort) {
    this.currentSortField = event.active;
    this.currentDirection = event.direction;

    this.store.dispatch(changingItemSorting({
      sortField: this.currentSortField,
      sortDirection: this.currentDirection
    }));

    if (this.filterName && this.filterName.length >= 3) {
      this.searchItems(this.filterPage);
    } else {
      this.filterName = '';
      this.filterPage = 0;
      this.getAllItems()
    }
  }

  getAllItems() {
    this.itemService.getAllItems(
      this.currentPage,
      this.currentSize,
      this.currentSortField,
      this.currentDirection
    ).pipe(
      takeUntil(this.unsubscribe)
    )
      .subscribe((data) => {
          this.loading = true;

          this.dataSource.data = data.content;

          this.totalElements = data.totalElements;
        },
        (error) => {
          this.snackBarService.error(error.message);
        },
        () => {
          this.loading = false;
        });
  }

  searchItems(filterPage: number) {
    if (this.filterName && this.filterName.length >= 3) {
      if (this.matPaginator) {
        this.matPaginator.pageIndex = 0;
      }
      setTimeout(() => {
        this.itemService.searchItems(
          filterPage,
          this.currentSize,
          this.currentSortField,
          this.currentDirection,
          this.filterName,
          this.filterHasImage,
          this.filterCategoryId
        ).pipe(
          takeUntil(this.unsubscribe)
        )
          .subscribe((data) => {
              this.loading = true;

              this.dataSource.data = data.content;

              this.totalElements = data.totalElements;

              this.matPaginator.length = data.totalElements;
            },
            (error) => {
              this.snackBarService.error(error.message);

              this.dataSource.data = [];
            },
            () => {
              this.loading = false;
            })
      }, 500);
    } else if (!this.filterName) {
      if (this.matPaginator) {
        this.matPaginator.pageIndex = this.currentPage;
      }
      this.filterName = '';
      this.filterPage = 0;

      this.getAllItems();
    }

    this.store.dispatch(changingItemFiltering({
      name: this.filterName,
      hasImage: this.filterHasImage,
      categoryId: this.filterCategoryId,
      filterPage: this.filterPage
    }));
  }
}
