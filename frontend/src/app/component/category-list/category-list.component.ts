import {Component, OnInit, ViewChild} from '@angular/core';
import {CategoryBackendService} from "../../services/category-backend.service";
import {MatTable} from "@angular/material/table";
import {KeycloakService} from "keycloak-angular";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";

import {MatSnackBar} from "@angular/material/snack-bar";
import {DialogWindowComponent} from "../dialog-window/dialog-window.component";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import { Category } from "../../models/category";

@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.css']
})
export class CategoryListComponent implements OnInit {

  categories: Category[] = [];

  category: Category;

  totalElements: number;

  pageSize: number = 5;
  currentPage: number = 0;

  displayedColumns: string[] = ['id', 'name', 'description', 'actions']

  public role: boolean;

  @ViewChild(MatTable) table: any;

  @ViewChild(MatPaginator) paginator: MatPaginator

  constructor(private categoryService: CategoryBackendService,
              private keycloak: KeycloakService,
              public readonly router: Router,
              public dialog: MatDialog,
              private snackBar: MatSnackBar) {
  }

  ngOnInit() {
    this.getAllCategories(this.currentPage, this.pageSize);
  }

  getAllCategories(page?: number, size?: number) {
    this.role = this.keycloak.isUserInRole('ROLE_USER');

    this.categoryService.getAllCategories(page, size).subscribe((data) => {
      this.categories = data.content;

      this.paginator.pageIndex = this.currentPage;
      this.totalElements = data.totalElements;

      this.table.renderRows();
    })
  }

  deleteCategory(id: number) {
    const dialogRef = this.dialog.open(DialogWindowComponent);

    dialogRef.afterClosed().subscribe((res) => {
      switch (res.event) {
        case "confirm-option":
          this.categoryService.deleteCategory(id).subscribe(() => {
              this.getAllCategories(this.currentPage, this.pageSize);
              this.snackBar.open("Category was successfully deleted!", 'OK', {
                duration:5000
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
    console.log({event})
    this.pageSize = event.pageSize;
    this.currentPage = event.pageIndex;
    this.getAllCategories(this.currentPage, this.pageSize)
  }
}
