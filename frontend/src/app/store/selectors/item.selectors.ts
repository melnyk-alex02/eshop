import { Item } from "../../models/item";
import { State } from "../../models/state";
import { createFeatureSelector, createSelector } from "@ngrx/store";

export const selectItemState = createFeatureSelector<State<Item>>("item");

export const selectItemPagination = createSelector(
  selectItemState,
  (state) => state.pagination
);

export const selectItemSorting = createSelector(
  selectItemState,
  (state) => state.sorting
);

export const selectItemFiltering = createSelector(
  selectItemState,
  (state) => state.filtering
);
