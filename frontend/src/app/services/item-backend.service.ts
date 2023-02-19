import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { SERVER_API_URL } from "../app.constants";

@Injectable()
export class Item {

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

  public getItemsByCategoryId(id: number): Observable<Item[]> {

    let queryParams = new HttpParams();
    queryParams = queryParams.append("categoryId", id.valueOf())

    return this.http.get<Item[]>(`${SERVER_API_URL}/items`, {params: queryParams});
  }

  public updateItem(data: Item) {
    return this.http.put<Item>(`${SERVER_API_URL}/items`, data);
  }
}
