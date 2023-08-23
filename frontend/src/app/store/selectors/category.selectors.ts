import { createFeatureSelector, createSelector } from "@ngrx/store";
import { Category } from "../../models/category";
import { State } from "../../models/state";

export const selectCategoryState = createFeatureSelector<State<Category>>("category")

export const selectCategoryPagination = createSelector(
  selectCategoryState,
  (state) => state.pagination
);

export const selectCategorySorting = createSelector(
  selectCategoryState,
  (state) => state.sorting
);

export const selectCategoryFiltering = createSelector(
  selectCategoryState,
  (state) => state.filtering
);
