import { createAction, props } from "@ngrx/store";

enum AllItemsActions {
  ChangingAllItemsPagination = '[All Items] Changing All Items Pagination'
}

export const changingAllItemsPagination = createAction(
  AllItemsActions.ChangingAllItemsPagination,
  props<{ pageIndex: number, pageSize: number }>()
);
