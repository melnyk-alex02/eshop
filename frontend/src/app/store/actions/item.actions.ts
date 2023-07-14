import { createAction, props } from "@ngrx/store";
import { Item } from "../../models/item";
import { Page } from "../../models/page";

enum ItemAction {
  LoadingItems = "[Item] Loading data",
  LoadingItemsSuccessful = "[Item] Loading successful",
  LoadingItemsFailure = "[Item] Loading failure",
  ChangingItemPagination = "[Item] Changing pagination",
  ChangingItemSorting = "[Item] Changing sorting"
}

export const loadingItems = createAction(
  ItemAction.LoadingItems,
  props<{ pageIndex: number, pageSize: number, sortField: string, sortDirection: string }>()
);

export const loadingItemsSuccessful = createAction(
  ItemAction.LoadingItemsSuccessful,
  props<{ page: Page<Item> }>()
);

export const loadingItemsFailure = createAction(
  ItemAction.LoadingItemsFailure,
  props<{ error: any }>()
);

export const changingItemPagination = createAction(
  ItemAction.ChangingItemPagination,
  props<{ pageIndex: number, pageSize: number }>()
);

export const changingItemSorting = createAction(
  ItemAction.ChangingItemSorting,
  props<{ sortField: string, sortDirection: string }>()
);
