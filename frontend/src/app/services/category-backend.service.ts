import { Injectable } from '@angular/core';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class Category{
  id:string;
  name:string;
  description:string;
}

@Injectable()
export class CategoryBackendService {
  private categoryUrl: string;
  constructor(private http: HttpClient) {
    this.categoryUrl = 'http://localhost:7979/eshop/api/categories';
  }

  public getAllCategories(): Observable<Category[]>{
    return this.http.get<Category[]>(this.categoryUrl);
  }
}
