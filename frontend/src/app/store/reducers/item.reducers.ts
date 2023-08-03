import { on } from "@ngrx/store";
import { initialItemState } from "../states/item.state";
import * as ItemActions from "../actions/item.actions"
import { createRehydrationReducer } from "./rehydration.reducer";

export const itemReducer = createRehydrationReducer(
  'itemState',
  initialItemState,
  on(ItemActions.changingItemPagination, (state, {pageIndex, pageSize}) => ({
    ...state,
    pagination: {
      pageIndex: pageIndex,
      pageSize: pageSize
    }
  })),
  on(ItemActions.changingItemSorting, (state, {sortField, sortDirection}) => ({
    ...state,
    sorting: {
      sortField: sortField,
      sortDirection: sortDirection
    }
  })),
  on(ItemActions.changingItemFiltering, (state, {name, hasImage, categoryId, filteringPage}) => ({
    ...state,
    filtering: {
      name: name,
      hasImage: hasImage,
      categoryId: categoryId,
      filteringPage: filteringPage
    }
  }))
);
