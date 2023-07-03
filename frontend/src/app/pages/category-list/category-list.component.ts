import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CategoryBackendService } from "../../services/category-backend.service";
import { MatTable, MatTableDataSource } from "@angular/material/table";
import { KeycloakService } from "keycloak-angular";
import { Router } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";
import { MatSnackBar } from "@angular/material/snack-bar";
import { DialogWindowComponent } from "../../component/dialog-window/dialog-window.component";
import { MatPaginator, PageEvent } from "@angular/material/paginator";
import { Category } from "../../models/category";
import { Subject } from "rxjs";
import { GlobalState } from "../../store/states/global.state";
import { Store } from "@ngrx/store";
import { MatSort, Sort } from "@angular/material/sort";
import {
  changingCategoryPagination,
  changingCategorySorting,
  loadingCategories
} from "../../store/actions/category.actions";


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

  currentPage: number;
  currentSize: number;

  currentSortField: string;
  currentDirection: string;

  private unsubscribe: Subject<void> = new Subject()

  @ViewChild(MatTable) table: any;

  @ViewChild(MatPaginator) matPaginator: MatPaginator;

  @ViewChild(MatSort, {static: true}) matSort: MatSort;

  constructor(private categoryService: CategoryBackendService,
              private keycloak: KeycloakService,
              public readonly router: Router,
              public dialog: MatDialog,
              private snackBar: MatSnackBar,
              private store: Store<GlobalState>) {
  }

  ngOnInit() {
    this.dataSource.paginator = this.matPaginator;
    this.dataSource.sort = this.matSort;

    this.store.select('category').subscribe((data) => {
      this.currentPage = data.pagination.pageIndex;
      this.currentSize = data.pagination.pageSize;

      this.currentSortField = data.sorting.sortField;
      this.currentDirection = data.sorting.sortDirection;
    })

    this.store.dispatch(loadingCategories({
        pageIndex: this.currentPage,
        pageSize: this.currentSize,
        sortField: this.currentSortField,
        sortDirection: this.currentDirection
      })
    );

    this.store.select('category').subscribe((data) => {
        this.dataSource = new MatTableDataSource<Category>(data.data.content);

        this.loading = data.loading;

        this.totalElements = data.data.totalElements;
      }
    );
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
          this.categoryService.deleteCategory(id).subscribe(() => {
              this.categoryService.getAllCategories(
                this.currentPage,
                this.currentSize,
                this.currentSortField,
                this.currentDirection
              );
              this.snackBar.open("Category was successfully deleted!", 'OK', {
                duration: 5000
              })
            },
            (error) => {
              this.snackBar.open(`${error.message}`, 'OK', {
                duration: 3000
              });
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

    this.store.dispatch(loadingCategories({
      pageIndex: this.currentPage,
      pageSize: this.currentSize,
      sortField: this.currentSortField,
      sortDirection: this.currentDirection
    }));
  }

  sortChanged(event: Sort) {
    this.currentSortField = event.active;
    this.currentDirection = event.direction === 'asc' ? 'desc' : 'asc';

    this.store.dispatch(changingCategorySorting({
      sortField: this.currentSortField,
      sortDirection: this.currentDirection
    }));

    this.categoryService.getAllCategories(
      this.currentPage,
      this.currentSize,
      this.currentSortField,
      this.currentDirection
    ).subscribe(
      (data) => {
        this.dataSource.data = data.content;
      }
    );
  }
}
