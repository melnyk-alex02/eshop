import { initialItemState, } from "./item.state";
import { Item } from "../../models/item";
import { State } from "../../models/state";
import { initialCategoryState } from "./category.state";
import { Category } from "../../models/category";

export interface GlobalState {
  item: State<Item>;

  category: State<Category>;
}
export  const initialGlobalState: GlobalState = {
  item: initialItemState,

  category: initialCategoryState,
};
