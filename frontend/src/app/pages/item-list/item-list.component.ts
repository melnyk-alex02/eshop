import { Component, OnInit, ViewChild } from '@angular/core';
import { ItemBackendService } from "../../services/item-backend.service";
import { MatTable } from "@angular/material/table";
import { KeycloakService } from "keycloak-angular";
import { ActivatedRoute, Router } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";
import { MatSnackBar } from "@angular/material/snack-bar";
import { DialogWindowComponent } from "../../component/dialog-window/dialog-window.component";
import { Subject } from "rxjs";
import { MatPaginator, PageEvent } from "@angular/material/paginator";
import { Item } from "../../models/item";

@Component({
  selector: 'app-item-list',
  templateUrl: './item-list.component.html',
  styleUrls: ['./item-list.component.css']
})

export class ItemListComponent implements OnInit {

  items: Item[] = [];

  role: boolean;
  totalElements = 0;
  pageSize: number = 5;
  currentPage: number = 0;

  private unsubscribe: Subject<void> = new Subject();

  displayedColumns: string[] = ['id', "name", "description", "categoryId", "imageSrc", "actions"]

  @ViewChild(MatTable) table: any;

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private itemService: ItemBackendService,
              private keycloak: KeycloakService,
              public readonly router: Router,
              private route: ActivatedRoute,
              public dialog: MatDialog,
              private snackBar: MatSnackBar) {
  }

  ngOnInit() {
    this.role = this.keycloak.isUserInRole('ROLE_ADMIN')
    this.getAllItems(this.currentPage, this.pageSize);
  }

  ngOnDestroy() {
    this.unsubscribe.next();
    this.unsubscribe.complete();
  }

  getAllItems(page?: number, size?: number) {

    this.itemService.getAllItems(page, size).subscribe(data => {
      this.items = data.content;

      this.paginator.pageIndex = this.currentPage;
      this.totalElements = data.totalElements;

      this.table.renderRows();
    })
  }

  deleteItem(id: number) {
    const dialogRef = this.dialog.open(DialogWindowComponent);

    dialogRef.afterClosed().subscribe((res) => {
      switch (res.event) {
        case "confirm-option":
          this.itemService.deleteItem(id).subscribe(() => {
              this.getAllItems(this.currentPage, this.pageSize);
            },
            (error) => {
              console.log(error);

              this.snackBar.open(`${error.message}`, 'OK', {
                duration: 5000
              })
            })
          this.snackBar.open('Item was deleted successfully!', 'OK', {
            duration: 5000
          })
          break;

        case "cancel-option":
          break;
      }

    })
  }

  pageChanged(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.currentPage = event.pageIndex;
    this.getAllItems(this.currentPage, this.pageSize)
  }
}
