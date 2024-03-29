import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from "@angular/common/http";
import { catchError, Observable, throwError } from "rxjs";
import { SERVER_API_URL } from "../constants/app.constants";
import { Category } from "../models/category";
import { Page } from "../models/page";


@Injectable({
  providedIn: 'root'
})
export class CategoryBackendService {


  constructor(private http: HttpClient) {
  }

  public getAllCategories(pageIndex: number,
                          pageSize: number,
                          sortField: string,
                          sortDirection: 'asc' | 'desc' | string): Observable<Page<Category>> {
    let params = new HttpParams()
      .set('page', String(pageIndex))
      .set('size', String(pageSize))
      .set('sort', sortField + ',' + sortDirection)

    return this.http.get<Page<Category>>(`${SERVER_API_URL}/categories`, {params});
  }

  public searchCategories(pageIndex: number,
                          pageSize: number,
                          sortField: string,
                          sortDirection: 'asc' | 'desc' | string,
                          name: string): Observable<Page<Category>> {
    let params = new HttpParams()
      .set('page', pageIndex)
      .set('size', pageSize)
      .set('sort', sortField + ',' + sortDirection)
      .set('name', name);

    return this.http.get<Page<Category>>(`${SERVER_API_URL}/categories/search`, {params});
  }

  public getCategoryById(id: number): Observable<Category> {
    return this.http.get<Category>(`${SERVER_API_URL}/categories/` + id.valueOf());
  }

  public uploadCategories(file: File): Observable<Category[]> {
    const formData: FormData = new FormData();

    formData.append("file", file);

    return this.http.post<Category[]>(`${SERVER_API_URL}/upload-categories`, formData).pipe(
      catchError(this.handleError)
    );
  }

  public createCategory(data: Category): Observable<Category> {
    return this.http.post<Category>(`${SERVER_API_URL}/categories`, data).pipe(
      catchError(this.handleError)
    );
  }

  public updateCategory(data: Category): Observable<Category> {
    return this.http.put<Category>(`${SERVER_API_URL}/categories`, data).pipe(
      catchError(this.handleError)
    );
  }

  public deleteCategory(id: number): Observable<number> {

    return this.http.delete<number>(`${SERVER_API_URL}/categories/` + id.valueOf()).pipe(
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
