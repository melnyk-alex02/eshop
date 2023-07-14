import { ActionReducerMap } from "@ngrx/store";
import { GlobalState } from "./states/global.state";
import { itemReducer } from "./reducers/item.reducers";
import { categoryReducer } from "./reducers/category.reducers";

export const reducers: ActionReducerMap<GlobalState> = {
  item: itemReducer,
  category: categoryReducer
};
