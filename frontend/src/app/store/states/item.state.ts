import { Item } from "../../models/item";
import { State } from "../../models/state";

export const initialItemState: State<Item> = {
  data: {
    content: [],
    totalElements: 0,
  },
  loading: false,
  error: '',

  pagination: {
    pageIndex: 0,
    pageSize: 5,
  },

  sorting:{
    sortField: 'id',
    sortDirection: 'asc'
  }
};
