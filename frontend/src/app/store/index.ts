import { ActionReducerMap } from "@ngrx/store";
import { GlobalState } from "./states/global.state";
import { itemReducer } from "./reducers/item.reducers";
import { categoryReducer } from "./reducers/category.reducers";
import { allItemsReducers } from "./reducers/all.items.reducers";

export const reducers: ActionReducerMap<GlobalState> = {
  item: itemReducer,
  category: categoryReducer,
  allItems: allItemsReducers
};
