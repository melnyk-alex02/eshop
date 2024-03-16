import { OrderItem } from "./orderItem";

export interface Order {
  number: string;
  status: string;
  createdDate: Date;
  price: number;
  count: number;
  orderItemDTOList: OrderItem[];
  purchasedDate: Date;
}
