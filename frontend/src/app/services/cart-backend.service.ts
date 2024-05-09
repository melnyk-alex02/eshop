import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { CartItem } from "../models/cartItem";
import { SERVER_API_URL } from "../constants/app.constants";
import { catchError, Observable, throwError } from "rxjs";
import { Order } from "../models/order";

@Injectable({
  providedIn: 'root'
})
export class CartBackendService {

  constructor(private http: HttpClient) { }

  public addItemToCart(id: number): Observable<CartItem> {
    return this.http.post<CartItem>(`${SERVER_API_URL}/cart?itemId=${id}`, {}).pipe(
    )
  }

  public getAllCartOfUser(): Observable<CartItem[]> {
    return this.http.get<CartItem[]>(`${SERVER_API_URL}/cart`).pipe(
    )
  }

  public updateItemCount(id: number, count: number): Observable<void> {
    return this.http.put<void>(`${SERVER_API_URL}/cart?itemId=${id}&count=${count}`, {}).pipe(
    )
  }

  public createOrder(cartItemArray: CartItem[], email: string) : Observable<Order> {
    return this.http.post<Order>(`${SERVER_API_URL}/cart/create-order/${email}` , cartItemArray).pipe();
  }

  public deleteItemFromCart(id: number): Observable<void> {
    return this.http.delete<void>(`${SERVER_API_URL}/cart/${id}`).pipe(
    )
  }

  public createOrderFromCart(): Observable<Order> {
    return this.http.post<Order>(`${SERVER_API_URL}/cart/create-order`, {}).pipe(
    )
  }
}
