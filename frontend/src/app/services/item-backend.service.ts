import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { SERVER_API_URL } from "../app.constants";

// @ts-ignore
@Injectable()
export interface Item {

  id: number;
  name: string;
  description: string;
  categoryId: number;
  imageSrc: string;
}

@Injectable({
  providedIn: 'root'
})
export class ItemBackendService {

  constructor(private http: HttpClient) {
  }

  public getItemById(id: number): Observable<Item>{
    return this.http.get<Item>( `${SERVER_API_URL}/items/` + id.valueOf());
  }

  public getItemsByCategoryId(id: number): Observable<Item[]> {

    let queryParams = new HttpParams();
    queryParams = queryParams.append("categoryId", id.valueOf())

    return this.http.get<Item[]>(`${SERVER_API_URL}/items`, {params: queryParams});
  }

  public updateItem(data: Item) {
    return this.http.put<Item>(`${SERVER_API_URL}/items`, data);
  }

  public createItem(data: Item): Observable<Item>{
    return this.http.post<Item>(`${SERVER_API_URL}/items`, data);
  }

  public deleteItem(id: number){
    return this.http.delete<Item>(`${SERVER_API_URL}/items/` + id.valueOf())
  }

  public getAllItems(page?:number, size?:number): Observable<Item[]>{
    let params = new HttpParams()
      .set('page', String(page))
      .set('size', String(size))
    return this.http.get<Item[]>(`${SERVER_API_URL}/items`, {params});
  }
}


