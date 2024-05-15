import { Component, DestroyRef, OnInit, ViewChild } from '@angular/core';
import { Item } from "../../models/item";
import { ItemBackendService } from "../../services/item-backend.service";
import { MatTableDataSource } from "@angular/material/table";
import { CartBackendService } from "../../services/cart-backend.service";
import { SnackBarService } from "../../services/snack-bar.service";
import { MatPaginator, PageEvent } from "@angular/material/paginator";
import { GlobalState } from "../../store/states/global.state";
import { Store } from "@ngrx/store";
import { selectAllItemsPagination } from "../../store/selectors/all.items.selectors";
import { changingAllItemsPagination } from "../../store/actions/all.items.actions";
import { CartItem } from "../../models/cartItem";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";

@Component({
  selector: 'app-all-items-page',
  templateUrl: './all-items-page.component.html',
  styleUrls: ['./all-items-page.component.css']
})

export class AllItemsPageComponent implements OnInit {
  items: Item[];

  cartItemList: CartItem[] = [];


  countOfItemsInCart: number = 0;

  dataSource = new MatTableDataSource<Item>();

  pagination$;

  loading: boolean;
  currentPage: number;
  currentSize: number;
  totalElements: number;

  @ViewChild(MatPaginator) matPaginator: MatPaginator;


  constructor(private itemService: ItemBackendService,
              private cartService: CartBackendService,
              private snackBarService: SnackBarService,
              private destroyRef: DestroyRef,
              public store: Store<GlobalState>) {
    this.pagination$ = this.store.select(selectAllItemsPagination);
  }

  ngOnInit(): void {
    this.dataSource.paginator = this.matPaginator;
    this.pagination$.pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: (pagination) => {
        this.currentSize = pagination.pageSize;
        this.currentPage = pagination.pageIndex;
      }
    });

    this.getAllCart();

    this.getAllItems(this.currentPage, this.currentSize);
  }

  getAllItems(pageNumber: number, pageSize: number) {
    this.loading = true;
    this.itemService.getAllItems(pageNumber, pageSize, "id", "desc").pipe(
      takeUntilDestroyed(this.destroyRef)
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

  getAllCart() {
    this.loading = true;
    if (localStorage.getItem("user")) {
      this.cartService.getAllCartOfUser().pipe(
        takeUntilDestroyed(this.destroyRef)
      )
        .subscribe({
          next: (data) => {
            this.cartItemList = data;
            this.countOfItemsInCart = data.length;
            localStorage.setItem("cartItemList", JSON.stringify(data));

          },
          error: (error) => {
            if (error.error.status != 401) {
              this.snackBarService.error(error.error.message)
            }
            this.loading = false;
          },
          complete: () => {
            localStorage.setItem("countOfItemsInCart", this.countOfItemsInCart.toString());
            this.loading = false;
          }
        });
    } else {
      this.cartItemList = JSON.parse(localStorage.getItem("cartItemList")!);
      if (this.cartItemList) {
        this.countOfItemsInCart = this.cartItemList.length;
        localStorage.setItem("countOfItemsInCart", this.countOfItemsInCart.toString());
      } else {
        localStorage.setItem("countOfItemsInCart", "0");
      }
    }
  }

  addItemToCart(item: Item) {
    if (!localStorage.getItem("user")) {
      item.inCart = true;
      if (!this.cartItemList) {
        this.cartItemList = [];
      }
      if (this.cartItemList.some(cartItem => cartItem.itemId == item.id)) {
        this.snackBarService.error("Item is already in cart");
        return;
      }
      this.cartItemList.push({itemId: item.id, itemName: item.name, itemPrice: item.price, count: 1})
      localStorage.setItem("countOfItemsInCart", (this.cartItemList.length).toString());
      localStorage.setItem("cartItemList", JSON.stringify(this.cartItemList));
    } else {
      this.cartService.addItemToCart(item.id).pipe(
        takeUntilDestroyed(this.destroyRef)
      )
        .subscribe({
          next: () => {
            if (!this.cartItemList) {
              this.cartItemList = [];
            }

            this.cartItemList.push({itemId: item.id, itemName: item.name, itemPrice: item.price, count: 1})
            localStorage.setItem("cartItemList", JSON.stringify(this.cartItemList));

          },
          error: (error) => {
            this.snackBarService.error(error.message)
          },
          complete: () => {
            item.inCart = true;
            this.snackBarService.success("Item was successfully added to cart");
            localStorage.setItem("countOfItemsInCart", (this.cartItemList.length).toString());
          },
        });
    }
  }

  isItemInCart(item: Item): any {
    this.cartItemList = JSON.parse(localStorage.getItem("cartItemList")!);
    if (!this.cartItemList) {
      return false;
    }

    return this.cartItemList.some(cartItem => cartItem.itemId == item.id);
  }

  pageChanged(event: PageEvent) {
    this.currentPage = event.pageIndex;
    this.currentSize = event.pageSize;

    this.store.dispatch(changingAllItemsPagination({
      pageIndex: this.currentPage,
      pageSize: this.currentSize
    }));

    this.getAllItems(this.currentPage, this.currentSize);
  }
}
