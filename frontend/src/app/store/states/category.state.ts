import { Category } from "../../models/category";
import { State } from "../../models/state";

export const initialCategoryState: State<Category> = {
  data: {
    content: [],
    totalElements: 0
  },
  loading: false,
  error: '',

  pagination: {
    pageIndex: 0,
    pageSize: 5,
  },

  sorting: {
    sortField: 'id',
    sortDirection: 'asc'
  }
};
