import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import { catchError, Observable, throwError } from "rxjs";
import { SERVER_API_URL } from "../app.constants";

//@ts-ignore
@Injectable()
export interface Category {
  id: number;
  name: string;
  description: string;
}

@Injectable({
  providedIn:'root'
})
export class CategoryBackendService {



constructor(private http: HttpClient) {
  }

  public getAllCategories(page?:number, size?:number): Observable<Category[]> {
    let params = new HttpParams()
      .set('page', String(page))
      .set('size', String(size))

    return this.http.get<Category[]>(`${SERVER_API_URL}/categories`, {params});
  }

  public getCategoryById(id: number): Observable<Category> {
    return this.http.get<Category>(`${SERVER_API_URL}/categories/` + id.valueOf());
  }

  public createCategory(data: Category): Observable<Category> {
    return this.http.post<Category>(`${SERVER_API_URL}/categories`, data);
  }

  public updateCategory(data: Category): Observable<Category> {
    return this.http.put<Category>(`${SERVER_API_URL}/categories`, data);
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
