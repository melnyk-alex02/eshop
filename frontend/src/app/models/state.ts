import { Page } from "./page";

export interface State<T>{
  data: Page<T>;
  loading: boolean;
  error: string;

  pagination:{
    pageIndex: number;
    pageSize: number;
  }
  sorting:{
    sortField: string;
    sortDirection: string;
  }
}
