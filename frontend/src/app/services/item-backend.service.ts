import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from "@angular/common/http";
import { catchError, Observable, throwError } from "rxjs";
import { SERVER_API_URL } from "../constants/app.constants";
import { Item } from "../models/item";
import { Page } from "../models/page";

@Injectable({
  providedIn: 'root'
})
export class ItemBackendService {

  constructor(private http: HttpClient) {
  }

  public getItemById(id: number): Observable<Item> {
    return this.http.get<Item>(`${SERVER_API_URL}/items/` + id.valueOf());
  }

  public updateItem(data: Item) {
    return this.http.put<Item>(`${SERVER_API_URL}/items`, data).pipe(
      catchError(this.handleError)
    );
  }

  public uploadItems(file: File): Observable<Item[]>{

    const formData: FormData = new FormData();

    formData.append("file", file);
    return this.http.post<Item[]>(`${SERVER_API_URL}/upload-items`, formData).pipe(
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

  public getAllItems(pageIndex: number,
                     pageSize: number,
                     sortField: string,
                     sortDirection: 'asc' | 'desc' | string): Observable<Page<Item>> {

    let params = new HttpParams()
      .set('page', pageIndex)
      .set('size', pageSize)
      .set('sort', sortField + ',' + sortDirection);

    return this.http.get<Page<Item>>(`${SERVER_API_URL}/items`, {params});
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
