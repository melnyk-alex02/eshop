import { createReducer, on } from "@ngrx/store";
import { initialItemState } from "../states/item.state";
import * as ItemActions from "../actions/item.actions"

export const itemReducer = createReducer(
  initialItemState,
  on(ItemActions.loadingItems, state => ({
    ...state,
    loading: true,
    error: ''
  })),
  on(ItemActions.loadingItemsSuccessful, (state, {page}) => ({
    ...state,
    data: {content: [...page.content], totalElements: page.totalElements},
    loading: false,
    error: ''
  })),
  on(ItemActions.loadingItemsFailure, (state, {error}) => ({
    ...state,
    loading: false,
    error: error,
    data: {
      content: [],
      totalElements: 0
    }
  })),
  on(ItemActions.changingItemPagination, (state, {pageIndex, pageSize}) => ({
    ...state,
    pagination: {
      pageIndex: pageIndex,
      pageSize: pageSize
    }
  })),
  on(ItemActions.changingItemSorting, (state, {sortField, sortDirection}) => ({
    ...state,
    sorting:{
      sortField: sortField,
      sortDirection: sortDirection
    }
  }))
);
