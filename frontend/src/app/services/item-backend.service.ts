import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
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

  public updateItem(data: Item): Observable<Item> {
    return this.http.put<Item>(`${SERVER_API_URL}/items`, data)
  }

  public uploadItems(file: File): Observable<Item[]> {

    const formData: FormData = new FormData();

    formData.append("file", file);
    return this.http.post<Item[]>(`${SERVER_API_URL}/upload-items`, formData);
  }

  public createItem(data: Item): Observable<Item> {
    return this.http.post<Item>(`${SERVER_API_URL}/items`, data);
  }

  public deleteItem(id: number): Observable<Item> {
    return this.http.delete<Item>(`${SERVER_API_URL}/items/` + id.valueOf());
  }

  public getLastFiveAddedItems(): Observable<Item[]> {
    return this.http.get<Item[]>(`${SERVER_API_URL}/items/last`);
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

  public searchItems(pageIndex: number,
                     pageSize: number,
                     sortField: string,
                     sortDirection: 'asc' | 'desc' | string,
                     name: string,
                     hasImage: boolean | string,
                     categoryId: number | string): Observable<Page<Item>> {
    let params = new HttpParams()
      .set('page', pageIndex)
      .set('size', pageSize)
      .set('sort', sortField + ',' + sortDirection)
      .set('name', name)
      .set('hasImage', hasImage)
      .set('categoryId', categoryId);

    return this.http.get<Page<Item>>(`${SERVER_API_URL}/items/search`, {params});
  }
}
