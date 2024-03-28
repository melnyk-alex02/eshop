import { Item } from "../../models/item";
import { State } from "../../models/state";

export const initialAllItemsState: State<Item> = {
  pagination: {
    pageIndex: 0,
    pageSize: 12,
  },

  sorting: {
    sortField: 'id',
    sortDirection: 'desc'
  },

  filtering: {}
};
