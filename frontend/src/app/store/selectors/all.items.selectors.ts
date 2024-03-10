import { createFeatureSelector, createSelector } from "@ngrx/store";
import { Item } from "src/app/models/item";
import { State } from "../../models/state";


export const selectAllItemsState = createFeatureSelector<any>("allItems")

export const selectAllItemsPagination = createSelector(
  selectAllItemsState,
  (state) => state.pagination
);
