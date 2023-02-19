import { Component, OnInit, ViewChild } from '@angular/core';
import { Category, CategoryBackendService } from "../services/category-backend.service";
import { MatTable } from "@angular/material/table";
import { KeycloakService } from "keycloak-angular";
import { Router } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";

import { MatSnackBar } from "@angular/material/snack-bar";
import { DialogWindowComponent } from "../dialog-window/dialog-window.component";

@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.css']
})
export class CategoryListComponent implements OnInit {

  categories: Category[] = [];

  category: Category;

  displayedColumns: string[] = ['id', 'name', 'description', 'actions']

  public role: boolean;

  @ViewChild(MatTable) table: any;

  constructor(private categoryService: CategoryBackendService,
              private keycloak: KeycloakService,
              public readonly router: Router,
              public dialog: MatDialog,
              private snackBar: MatSnackBar) {
  }

  ngOnInit() {
    this.getAllCategories();
  }

  getAllCategories() {
    this.role = this.keycloak.isUserInRole('ROLE_USER');

    this.categoryService.getAllCategories().subscribe((data) => {
      this.categories = data;

      this.table.renderRows();
    })
  }

  getCategoryById(id: number) {
    this.role = this.keycloak.isUserInRole('ROLE_ADMIN');

    console.log(id);
  }

  createCategory() {
    this.role = this.keycloak.isUserInRole('ROLE_ADMIN');
  }

  updateCategory(id: number) {
    this.role = this.keycloak.isUserInRole('ROLE_ADMIN');

    console.log(id);
  }

  deleteCategory(id: number) {
    const dialogRef = this.dialog.open(DialogWindowComponent);

    dialogRef.afterClosed().subscribe((res) => {
      switch (res.event) {
        case "confirm-option":
          this.categoryService.deleteCategory(id).subscribe(() => {
            },
            (error) => {
              this.snackBar.open(`${error.message}`, 'OK', {
                duration: 3000
              });
            });

          window.location.reload();

          break;

        case "cancel-option":
          break;
      }
    });
  }
}
