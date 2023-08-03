import { createAction, props } from "@ngrx/store";

enum ItemAction {
  ChangingItemPagination = "[Item] Changing pagination",
  ChangingItemSorting = "[Item] Changing sorting",
  ChangingItemFiltering = "[Item] Changing filtering"
}

export const changingItemPagination = createAction(
  ItemAction.ChangingItemPagination,
  props<{ pageIndex: number, pageSize: number }>()
);

export const changingItemSorting = createAction(
  ItemAction.ChangingItemSorting,
  props<{ sortField: string, sortDirection: string }>()
);

export const changingItemFiltering = createAction(
  ItemAction.ChangingItemFiltering,
  props<{ name: string, hasImage: boolean | string, categoryId: number | string, filteringPage: number }>()
);
