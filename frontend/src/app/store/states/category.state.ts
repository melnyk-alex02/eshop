import { Category } from "../../models/category";
import { State } from "../../models/state";

export const initialCategoryState: State<Category> = {
  pagination: {
    pageIndex: 0,
    pageSize: 5,
  },

  sorting: {
    sortField: 'id',
    sortDirection: 'asc'
  },

  filtering: {
    name: '',
    filterPage: 0
  }
};
