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
      catchError(this.handleError)
    )
  }

  public getAllCartOfUser(): Observable<CartItem[]> {
    return this.http.get<CartItem[]>(`${SERVER_API_URL}/cart`).pipe(
      catchError(this.handleError)
    )
  }

  public updateItemCount(id: number, count: number): Observable<void> {
    return this.http.put<void>(`${SERVER_API_URL}/cart?itemId=${id}&count=${count}`, {}).pipe(
      catchError(this.handleError)
    )
  }

  public deleteItemFromCart(id: number): Observable<void> {
    return this.http.delete<void>(`${SERVER_API_URL}/cart/${id}`).pipe(
      catchError(this.handleError)
    )
  }

  public createOrderFromCart(): Observable<Order> {
    return this.http.post<Order>(`${SERVER_API_URL}/cart/create-order`, {}).pipe(
      catchError(this.handleError)
    )
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status == 0) {
      console.error('An error occurred', error.error);
    } else {
      console.error(`Backend returned code ${error.status}, body was`, error.error)
    }
    return throwError(() => new Error(error.error.message));
  }
}
