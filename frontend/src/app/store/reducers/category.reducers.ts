import { on } from "@ngrx/store";
import { initialCategoryState } from "../states/category.state";
import * as CategoryActions from "../actions/category.actions"
import { createRehydrationReducer } from "./rehydration.reducer";

export const categoryReducer = createRehydrationReducer(
  'categoryState',
  initialCategoryState,
  on(CategoryActions.changingCategoryPagination, (state, {pageIndex, pageSize}) => ({
    ...state,
    pagination: {
      pageIndex: pageIndex,
      pageSize: pageSize
    }
  })),
  on(CategoryActions.changingCategorySorting, (state, {sortField, sortDirection}) => ({
    ...state,
    sorting: {
      sortField: sortField,
      sortDirection: sortDirection
    }
  }))
);
