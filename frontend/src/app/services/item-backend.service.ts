import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from "@angular/common/http";
import { catchError, Observable, throwError } from "rxjs";
import { SERVER_API_URL } from "../constants/app.constants";
import { Page } from "../models/page";
import { Item } from "../models/item";

@Injectable({
  providedIn: 'root'
})
export class ItemBackendService {

  constructor(private http: HttpClient) {
  }

  public getItemById(id: number): Observable<Item> {
    return this.http.get<Item>(`${SERVER_API_URL}/items/` + id.valueOf());
  }

  public getItemsByCategoryId(id: number): Observable<Item[]> {

    let queryParams = new HttpParams();
    queryParams = queryParams.append("categoryId", id.valueOf())

    return this.http.get<Item[]>(`${SERVER_API_URL}/items`, {params: queryParams}).pipe(
      catchError(this.handleError)
    );
  }

  public updateItem(data: Item) {
    return this.http.put<Item>(`${SERVER_API_URL}/items`, data).pipe(
      catchError(this.handleError)
    );
  }

  public createItem(data: Item): Observable<Item> {
    return this.http.post<Item>(`${SERVER_API_URL}/items`, data).pipe(
      catchError(this.handleError)
    );
  }

  public deleteItem(id: number) {
    return this.http.delete<Item>(`${SERVER_API_URL}/items/` + id.valueOf()).pipe(
      catchError(this.handleError)
    );
  }

  public getAllItems(page?: number, size?: number): Observable<Page<Item>> {
    let params = new HttpParams()
      .set('page', String(page))
      .set('size', String(size))
    return this.http.get<Page<Item>>(`${SERVER_API_URL}/items`, {params}).pipe(
      catchError(this.handleError)
    );
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
