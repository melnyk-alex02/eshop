import { on } from "@ngrx/store";
import * as AllItemActions from "../actions/all.items.actions"
import { createRehydrationReducer } from "./rehydration.reducer";
import { initialAllItemsState } from "../states/all.items.state";

export const allItemsReducers = createRehydrationReducer(
  'allItemsState',
  initialAllItemsState,
  on(AllItemActions.changingAllItemsPagination, (state, {pageIndex, pageSize}) => ({
    ...state,
    pagination: {
      pageIndex: pageIndex,
      pageSize: pageSize
    }
  }))
);
