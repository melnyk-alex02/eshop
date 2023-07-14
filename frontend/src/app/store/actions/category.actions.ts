import { createAction, props } from "@ngrx/store";
import { Category } from "../../models/category";
import { Page } from "../../models/page";

enum CategoryActions {
  LoadingCategories = '[Category] Loading Categories',
  LoadingCategoriesSuccessful = '[Category] Loading successful',
  LoadingCategoriesFailure = '[Category] Loading failure',
  ChangingCategoryPagination = '[Category] Changing pagination',
  ChangingCategorySorting = '[Category] Changing sorting'
}

export const loadingCategories = createAction(
  CategoryActions.LoadingCategories,
  props<{ pageIndex: number, pageSize:number, sortField: string, sortDirection: string}>()
);

export const loadingCategoriesSuccessful = createAction(
  CategoryActions.LoadingCategoriesSuccessful,
  props<{page: Page<Category>}>()
);

export const loadingCategoriesFailure = createAction(
  CategoryActions.LoadingCategoriesFailure,
  props<{error: any}>()
);

export const changingCategoryPagination = createAction(
  CategoryActions.ChangingCategoryPagination,
  props<{pageIndex: number, pageSize: number}>()
);

export const changingCategorySorting = createAction(
  CategoryActions.ChangingCategorySorting,
  props<{sortField: string, sortDirection: string}>()
);
