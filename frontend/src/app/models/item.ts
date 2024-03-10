export interface Item {
  id: number;
  name: string;
  description: string;
  categoryId: number;
  price: number;
  categoryName: string;
  imageSrc: string;
  inCart: boolean;
}
