import { Component, DestroyRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CategoryBackendService } from "../../services/category-backend.service";
import { MatTable, MatTableDataSource } from "@angular/material/table";
import { Router } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";
import { DialogWindowComponent } from "../../component/dialog-window/dialog-window.component";
import { MatPaginator, PageEvent } from "@angular/material/paginator";
import { Category } from "../../models/category";
import { debounceTime, distinctUntilChanged, Subject, takeUntil } from "rxjs";
import { GlobalState } from "../../store/states/global.state";
import { Store } from "@ngrx/store";
import { MatSort, Sort } from "@angular/material/sort";
import {
  changingCategoryFiltering,
  changingCategoryPagination,
  changingCategorySorting
} from "../../store/actions/category.actions";
import { SnackBarService } from "../../services/snack-bar.service";
import {
  selectCategoryFiltering,
  selectCategoryPagination,
  selectCategorySorting
} from "../../store/selectors/category.selectors";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";

@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.css']
})

export class CategoryListComponent implements OnInit {
  totalElements: number;

  loading: boolean;

  dataSource = new MatTableDataSource<Category>();
  displayedColumns: string[] = ['id', 'name', 'description', 'actions']

  sorting$;

  pagination$;

  filtering$;

  currentPage: number;
  currentSize: number;

  currentSortField: string;
  currentDirection: string | any;

  filterName: string;
  filterPage: number;

  @ViewChild(MatTable) table: any;
  @ViewChild(MatPaginator) matPaginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) matSort: MatSort;

  constructor(private categoryService: CategoryBackendService,
              private snackBarService: SnackBarService,
              public readonly router: Router,
              public dialog: MatDialog,
              private destroyRef: DestroyRef,
              private store: Store<GlobalState>) {
    this.pagination$ = this.store.select(selectCategoryPagination);

    this.sorting$ = this.store.select(selectCategorySorting);

    this.filtering$ = this.store.select(selectCategoryFiltering)
  }

  ngOnInit() {
    this.dataSource.paginator = this.matPaginator;
    this.dataSource.sort = this.matSort;

    this.pagination$.pipe(
      takeUntilDestroyed(this.destroyRef)
    )
      .subscribe({
        next: (pagination) => {
          this.currentPage = pagination.pageIndex;
          this.currentSize = pagination.pageSize;
        }
      });

    this.sorting$.pipe(
      takeUntilDestroyed(this.destroyRef)
    )
      .subscribe({
        next: (sorting) => {
          this.currentSortField = sorting.sortField;
          this.currentDirection = sorting.sortDirection;
        }
      });

    this.filtering$.pipe(
      takeUntilDestroyed(this.destroyRef)
    )
      .subscribe({
        next: (filtering: any) => {
          this.filterName = filtering.name;
          this.filterPage = filtering.filterPag;
        }
      });

    if (this.filterName && this.filterName.length >= 3) {
      this.searchCategories(this.filterPage);
    } else {
      this.filterName = '';
      this.filterPage = 0;

      this.getAllCategories();
    }
  }

  deleteCategory(id: number) {
    const dialogRef = this.dialog.open(DialogWindowComponent);

    dialogRef.afterClosed().subscribe((res) => {
      switch (res.event) {
        case "confirm-option":
          this.categoryService.deleteCategory(id)
            .pipe(
              takeUntilDestroyed(this.destroyRef)
            )
            .subscribe({
              next: () => {
                this.getAllCategories();

                this.snackBarService.success("Category was successfully deleted!");
              },
              error: (error) => {
                this.snackBarService.error(`${error.message}`);
              }
            });
          break;

        case "cancel-option":
          break;
      }
    });
  }

  pageChanged(event: PageEvent) {
    this.currentPage = event.pageIndex;
    this.currentSize = event.pageSize;

    this.store.dispatch(changingCategoryPagination({
      pageIndex: this.currentPage,
      pageSize: this.currentSize
    }));

    if (this.filterName.length >= 3) {
      this.filterPage = event.pageIndex;

      this.searchCategories(this.filterPage);
    } else {
      this.getAllCategories();
    }
  }

  sortChanged(event: Sort) {
    this.currentSortField = event.active;
    this.currentDirection = event.direction;

    this.store.dispatch(changingCategorySorting({
      sortField: this.currentSortField,
      sortDirection: this.currentDirection
    }));

    if (this.filterName && this.filterName.length >= 3) {
      this.searchCategories(this.filterPage);
    } else {
      this.filterName = '';
      this.getAllCategories();
    }
  }

  clearFilters() {
    this.filterName = '';

    this.getAllCategories();

    if (this.matPaginator) {
      this.matPaginator.pageIndex = this.currentPage;
    }

    this.store.dispatch(changingCategoryFiltering({
      name: this.filterName,
      filterPage: this.filterPage
    }));
  }

  getAllCategories() {
    this.loading = true;

    this.categoryService.getAllCategories(
      this.currentPage,
      this.currentSize,
      this.currentSortField,
      this.currentDirection
    )
      .pipe(
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({
        next: (data) => {
          this.dataSource.data = data.content;

          this.totalElements = data.totalElements;
        },
        error: (error) => {
          this.snackBarService.error(error.message);

          this.dataSource.data = [];
        }
        , complete: () => {
          this.loading = false;
        }
      });
  }

  searchCategories(filterPage: number) {
    if (this.filterName && this.filterName.length >= 3) {
      if (this.matPaginator) {
        this.matPaginator.pageIndex = 0;
      }
      this.loading = true;

      this.categoryService.searchCategories(
        filterPage,
        this.currentSize,
        this.currentSortField,
        this.currentDirection,
        this.filterName)
        .pipe(
          debounceTime(500),
          distinctUntilChanged(),
          takeUntilDestroyed(this.destroyRef)
        )
        .subscribe({
          next: (data) => {
            this.dataSource.data = data.content;

            this.totalElements = data.totalElements;
          },
          complete: () => {
            if (this.dataSource.data.length === 0) {
              this.snackBarService.error("There are no categories by your search preferences");
            }

            this.loading = false;
          }
        });
    } else if (!this.filterName) {
      if (this.matPaginator) {
        this.matPaginator.pageIndex = this.currentPage;
      }
      this.filterName = '';
      this.filterPage = 0;

      this.getAllCategories();
    }

    this.store.dispatch(changingCategoryFiltering({
      name: this.filterName,
      filterPage: this.filterPage
    }));
  }
}
