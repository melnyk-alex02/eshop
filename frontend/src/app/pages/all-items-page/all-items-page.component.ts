import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Item} from "../../models/item";
import {Subject, takeUntil} from "rxjs";
import {ItemBackendService} from "../../services/item-backend.service";
import {MatTableDataSource} from "@angular/material/table";
import {CartBackendService} from "../../services/cart-backend.service";
import {SnackBarService} from "../../services/snack-bar.service";
import {MatPaginator, PageEvent} from "@angular/material/paginator";

@Component({
  selector: 'app-all-items-page',
  templateUrl: './all-items-page.component.html',
  styleUrls: ['./all-items-page.component.css']
})
export class AllItemsPageComponent implements OnInit, OnDestroy {
  items: Item[];

  dataSource = new MatTableDataSource<Item>();

  loading: boolean;
  currentPage: number;
  currentSize: number;
  totalElements: number;
  @ViewChild(MatPaginator) matPaginator: MatPaginator;
  private unsubscribe: Subject<void> = new Subject<void>()

  constructor(private itemService: ItemBackendService,
              private cartService: CartBackendService,
              private snackBarService: SnackBarService) {
  }

  ngOnInit(): void {
    this.getAllItems(0, 15);
  }

  ngOnDestroy() {
    this.unsubscribe.next();
    this.unsubscribe.complete();
  }


  getAllItems(pageNumber: number, pageSize: number) {
    this.loading = true;
    this.itemService.getAllItems(pageNumber, pageSize, "id", "desc").pipe(
      takeUntil(this.unsubscribe)
    )
      .subscribe({

        next: (data) => {
          this.items = data.content;
          this.totalElements = data.totalElements;

        },
        error: (error) => {
          this.snackBarService.error(error.message)
          this.loading = false;
        },
        complete: () => {
          this.loading = false;
        }
      });
  }

  addItemToCart(id: number) {
    this.cartService.addItemToCart(id).pipe(
      takeUntil(this.unsubscribe)
    )
      .subscribe({
      error: (error) => {
        this.snackBarService.error(error.message)
      },
      complete: () => {
        this.snackBarService.success("Item was successfully added to cart")
      }
    });
  }

  pageChanged(event: PageEvent) {
    this.currentPage = event.pageIndex;
    this.currentSize = event.pageSize;

    this.getAllItems(event.pageIndex, event.pageSize);
  }
}
