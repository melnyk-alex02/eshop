import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {SERVER_API_URL} from "../constants/app.constants";
import {catchError, Observable, throwError} from "rxjs";
import {Order} from "../models/order";
import {Page} from "../models/page";

@Injectable({
  providedIn: 'root'
})
export class OrderBackendService {

  constructor(private http: HttpClient) {
  }

  cancelUnconfirmedOrders(order: Order): void {
    const twentyFourHoursInMs = 24 * 60 * 60 * 1000;


    const creationTime = order.createdDate.getTime();
    const currentTime = new Date().getTime();

    if (currentTime - creationTime > twentyFourHoursInMs && !(order.status =="DONE")) {
      this.cancelOrder(order.number);
    }
  }

  public getOrder(orderNumber: string): Observable<Order> {
    return this.http.get<Order>(`${SERVER_API_URL}/order/` + orderNumber).pipe(
      catchError(this.handleError)
    )
  }

  public getOrders(pageNumber: number, pageSize: number): Observable<Page<Order>> {
    let params = new HttpParams()
      .set("page", pageNumber)
      .set("size", pageSize)
      .set("sort", "status,desc")
    return this.http.get<Page<Order>>(`${SERVER_API_URL}/orders/all`, {params}).pipe(
      catchError(this.handleError)
    )
  }

  public getAllOrdersByUserId(): Observable<Page<Order>> {
    let params= new HttpParams()
      .set("page", 0)
      .set("size", 0)
      .set("sort", "status,desc")
    return this.http.get<Page<Order>>(`${SERVER_API_URL}/orders`, {params}).pipe(
      catchError(this.handleError)
    );
  }

  public cancelOrder(orderNumber: string): Observable<void> {
    return this.http.delete<void>(`${SERVER_API_URL}/order/` + orderNumber).pipe(
      catchError(this.handleError)
    );
  }

  public confirmOrder(orderNumber: string): Observable<void> {
     return this.http.put<void>(`${SERVER_API_URL}/order/confirm/` +  orderNumber, {}).pipe(
      catchError(this.handleError)
    );
  }

  public startAutoCancelCheck(order: Order): void {
    setInterval(() => {
      this.cancelUnconfirmedOrders(order);
    }, 3600000);
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status == 0) {
      console.error('An error occurred: ', error.error);
    } else {
      console.error(`Backend returned code ${error.status}, body was `, error.error)
    }
    return throwError(() => new Error(error.error.message));
  }
}
