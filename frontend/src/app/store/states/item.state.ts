import { Item } from "../../models/item";
import { State } from "../../models/state";

export const initialItemState: State<Item> = {
  pagination: {
    pageIndex: 0,
    pageSize: 5,
  },

  sorting: {
    sortField: 'id',
    sortDirection: 'asc'
  },

  filtering:{
    name: '',
    hasImage:'',
    categoryId:'',
    filteringPage: 0
  }
};
