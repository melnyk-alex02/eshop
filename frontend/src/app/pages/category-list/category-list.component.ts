import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CategoryBackendService } from "../../services/category-backend.service";
import { MatTable, MatTableDataSource } from "@angular/material/table";
import { KeycloakService } from "keycloak-angular";
import { Router } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";
import { DialogWindowComponent } from "../../component/dialog-window/dialog-window.component";
import { MatPaginator, PageEvent } from "@angular/material/paginator";
import { Category } from "../../models/category";
import { Subject, takeUntil } from "rxjs";
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

@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.css']
})
export class CategoryListComponent implements OnInit, OnDestroy {
  totalElements: number;
  loading: boolean;

  dataSource = new MatTableDataSource<Category>();
  displayedColumns: string[] = ['id', 'name', 'description', 'actions']

  sorting$;

  pagination$;

  filtering$;

  filterName: string;

  filteringPage: number;

  currentPage: number;
  currentSize: number;

  currentSortField: string;
  currentDirection: string | any;
  @ViewChild(MatTable) table: any;
  @ViewChild(MatPaginator) matPaginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) matSort: MatSort;
  private unsubscribe: Subject<void> = new Subject()

  constructor(private categoryService: CategoryBackendService,
              private snackBarService: SnackBarService,
              private keycloak: KeycloakService,
              public readonly router: Router,
              public dialog: MatDialog,
              private store: Store<GlobalState>) {
    this.pagination$ = this.store.select(selectCategoryPagination);

    this.sorting$ = this.store.select(selectCategorySorting);

    this.filtering$ = this.store.select(selectCategoryFiltering)
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
      })

    if (this.filterName && this.filterName.length >= 3) {
      this.searchCategories();
    } else {
      this.filterName = '';
      this.getAllCategories();
    }
  }

  ngOnDestroy() {
    this.unsubscribe.next();
    this.unsubscribe.complete();
  }

  deleteCategory(id: number) {
    const dialogRef = this.dialog.open(DialogWindowComponent);

    dialogRef.afterClosed().subscribe((res) => {
      switch (res.event) {
        case "confirm-option":
          this.categoryService.deleteCategory(id)
            .pipe(
              takeUntil(this.unsubscribe)
            )
            .subscribe(() => {
                this.getAllCategories();

                this.snackBarService.success("Category was successfully deleted!");
              },
              (error) => {
                this.snackBarService.error(`${error.message}`);
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

    if (this.filterName && this.filterName.length >= 3) {
      this.searchCategories();
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
      this.searchCategories();
    } else {
      this.getAllCategories();
    }
  }

  getAllCategories() {
    this.categoryService.getAllCategories(
      this.currentPage,
      this.currentSize,
      this.currentSortField,
      this.currentDirection
    )
      .pipe(
        takeUntil(this.unsubscribe)
      )
      .subscribe((data) => {
          this.loading = true;

          this.dataSource.data = data.content;

          this.totalElements = data.totalElements;
        },
        (error) => {
          this.snackBarService.error(error.message);

          this.dataSource.data = [];
        }
        , () => {
          this.loading = false;
        });
  }

  searchCategories() {
    setTimeout(() => {
      this.categoryService.searchCategories(
        this.filteringPage,
        this.currentSize,
        this.currentSortField,
        this.currentDirection,
        this.filterName)
        .pipe(
          takeUntil(this.unsubscribe)
        )
        .subscribe((data) => {
            this.loading = true;

            this.dataSource.data = data.content;

            this.totalElements = data.totalElements;
          },
          (error) => {
            this.snackBarService.error(error.message);

            this.dataSource.data = [];
          },
          () => {
            this.loading = false;
          });
    }, 500);

    if (this.matPaginator) {
      this.matPaginator.pageIndex = this.filteringPage;
    }

    this.store.dispatch(changingCategoryFiltering({
      name: this.filterName,
      filteringPage: this.filteringPage
    }))
  }
}
