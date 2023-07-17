import { createAction, props } from "@ngrx/store";

enum ItemAction {
  ChangingItemPagination = "[Item] Changing pagination",
  ChangingItemSorting = "[Item] Changing sorting"
}

export const changingItemPagination = createAction(
  ItemAction.ChangingItemPagination,
  props<{ pageIndex: number, pageSize: number }>()
);

export const changingItemSorting = createAction(
  ItemAction.ChangingItemSorting,
  props<{ sortField: string, sortDirection: string }>()
);
