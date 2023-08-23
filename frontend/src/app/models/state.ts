export interface State<T> {
  pagination: {
    pageIndex: number;
    pageSize: number;
  };
  sorting: {
    sortField: string;
    sortDirection: string;
  };
  filtering: any;
}
