import { createReducer, on } from "@ngrx/store";
import { initialCategoryState } from "../states/category.state";
import * as CategoryActions from "../actions/category.actions"

export const categoryReducer = createReducer(
  initialCategoryState,
  on(CategoryActions.loadingCategories, state => ({
    ...state,
    loading: true,
    error: ''
  })),
  on(CategoryActions.loadingCategoriesSuccessful, (state, {page}) => ({
    ...state,
    data: {content: [...page.content], totalElements: page.totalElements},
    loading: false,
    error: ''
  })),
  on(CategoryActions.loadingCategoriesFailure, (state, {error}) => ({
    ...state,
    loading: false,
    error: error,
    data: {
      content: [],
      totalElements: 0
    }
  })),
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
