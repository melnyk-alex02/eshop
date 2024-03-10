import { ChangeDetectorRef, Component, EventEmitter, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Item } from "../../models/item";
import { Subject, takeUntil } from "rxjs";
import { ItemBackendService } from "../../services/item-backend.service";
import { MatTableDataSource } from "@angular/material/table";
import { CartBackendService } from "../../services/cart-backend.service";
import { SnackBarService } from "../../services/snack-bar.service";
import { MatPaginator, PageEvent } from "@angular/material/paginator";
import { GlobalState } from "../../store/states/global.state";
import { Store } from "@ngrx/store";
import { selectAllItemsPagination } from "../../store/selectors/all.items.selectors";
import { changingAllItemsPagination } from "../../store/actions/all.items.actions";

@Component({
  selector: 'app-all-items-page',
  templateUrl: './all-items-page.component.html',
  styleUrls: ['./all-items-page.component.css']
})
export class AllItemsPageComponent implements OnInit, OnDestroy {
  items: Item[];

  itemsInCart: any[];

  countOfItemsInCart: number = 0;

  dataSource = new MatTableDataSource<Item>();

  pagination$;

  loading: boolean;
  currentPage: number;
  currentSize: number;
  totalElements: number;

  @ViewChild(MatPaginator) matPaginator: MatPaginator;

  private unsubscribe: Subject<void> = new Subject<void>()

  constructor(private itemService: ItemBackendService,
              private cartService: CartBackendService,
              private snackBarService: SnackBarService,
              private changeDetectorRef: ChangeDetectorRef,
              public store: Store<GlobalState>) {
    this.pagination$ = this.store.select(selectAllItemsPagination);
  }

  ngOnInit(): void {
    this.dataSource.paginator = this.matPaginator;
    this.pagination$.pipe(
      takeUntil(this.unsubscribe)
    ).subscribe({
      next: (pagination) => {
        this.currentSize = pagination.pageSize;
        this.currentPage = pagination.pageIndex;
      }
    });

    this.getAllItems(this.currentPage, this.currentSize);

    this.getAllCart();
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

  getAllCart() {
    this.loading = true;
    this.cartService.getAllCartOfUser().pipe(
      takeUntil(this.unsubscribe)
    )
      .subscribe({
        next: (data) => {
          this.itemsInCart = data;
          console.log(data.length);
          this.countOfItemsInCart = data.length
          localStorage.setItem("countOfItemsInCart", this.countOfItemsInCart.toString());
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

  addItemToCart(item: Item) {
    this.cartService.addItemToCart(item.id).pipe(
      takeUntil(this.unsubscribe)
    )
      .subscribe({
        error: (error) => {
          this.snackBarService.error(error.message)
        },
        complete: () => {
          item.inCart = true;
          this.updateCartItems(item, true);
          this.snackBarService.success("Item was successfully added to cart");
          localStorage.setItem("countOfItemsInCart", (this.itemsInCart.length).toString());
        },
      });
  }

  isItemInCart(item: Item): boolean {
    return this.itemsInCart.some(cartItem => cartItem.itemId == item.id);
  }


  updateCartItems(item: Item, isAdded: boolean) {
    const cartItem = this.itemsInCart.find(ci => ci.itemId === item.id);
    if (isAdded) {
      if (!cartItem) {
        this.itemsInCart.push({itemId: item.id, quantity: 1});
      }
    } else {
      if (cartItem) {
        this.itemsInCart = this.itemsInCart.filter(ci => ci.itemId !== item.id);
      }
    }
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
