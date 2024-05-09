import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from "@angular/common/http";
import { SERVER_API_URL } from "../constants/app.constants";
import { catchError, Observable, throwError } from "rxjs";
import { Order } from "../models/order";
import { Page } from "../models/page";

@Injectable({
  providedIn: 'root'
})
export class OrderBackendService {

  constructor(private http: HttpClient) {
  }

  public getOrderForCurrentUser(orderNumber: string): Observable<Order> {
    return this.http.get<Order>(`${SERVER_API_URL}/order/` + orderNumber);
  }

  public getOrderByEmailAndOrderForUnauthenticatedUsers(email: string, orderNumber: string) {
    return this.http.get<Order>(`${SERVER_API_URL}/order/${email}/${orderNumber}`);
  }

  public getOrders(pageNumber: number, pageSize: number): Observable<Page<Order>> {
    let params = new HttpParams()
      .set("page", pageNumber)
      .set("size", pageSize)
      .set("sort", "status,desc")
    return this.http.get<Page<Order>>(`${SERVER_API_URL}/orders/all`, {params});

  }

  public getAllOrdersByUserId(): Observable<Page<Order>> {
    let params = new HttpParams()
      .set("page", 0)
      .set("size", 0)
      .set("sort", "status,desc")
    return this.http.get<Page<Order>>(`${SERVER_API_URL}/orders`, {params});
  }

  public cancelOrder(orderNumber: string): Observable<void> {
    return this.http.delete<void>(`${SERVER_API_URL}/order/` + orderNumber);
  }

  public confirmOrder(orderNumber: string): Observable<void> {
    return this.http.put<void>(`${SERVER_API_URL}/order/confirm/` + orderNumber, {});
  }
}
