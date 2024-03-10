import { initialItemState, } from "./item.state";
import { Item } from "../../models/item";
import { State } from "../../models/state";
import { initialCategoryState } from "./category.state";
import { Category } from "../../models/category";
import { initialAllItemsState } from "./all.items.state";

export interface GlobalState {
  item: State<Item>;

  category: State<Category>;

  allItems: State<Item>;
}

export const initialGlobalState: GlobalState = {
  item: initialItemState,

  category: initialCategoryState,

  allItems: initialAllItemsState,
};
