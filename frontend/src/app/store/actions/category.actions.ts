import { createAction, props } from "@ngrx/store";

enum CategoryActions {
  ChangingCategoryPagination = '[Category] Changing pagination',
  ChangingCategorySorting = '[Category] Changing sorting',
  ChangingCategoryFiltering = '[Category] Changing filtering'
}

export const changingCategoryPagination = createAction(
  CategoryActions.ChangingCategoryPagination,
  props<{ pageIndex: number, pageSize: number }>()
);

export const changingCategorySorting = createAction(
  CategoryActions.ChangingCategorySorting,
  props<{ sortField: string, sortDirection: string }>()
);

export const changingCategoryFiltering = createAction(
  CategoryActions.ChangingCategoryFiltering,
  props<{ name: string, filterPage: number }>()
);
