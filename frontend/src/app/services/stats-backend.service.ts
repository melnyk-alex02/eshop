import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from "@angular/common/http";
import { catchError, Observable, throwError } from "rxjs";
import { Stats } from "../models/stats";
import { SERVER_API_URL } from "../constants/app.constants";
import { Page } from "../models/page";

@Injectable({
  providedIn: 'root'
})
export class StatsBackendService {

  constructor(private httpClient: HttpClient) { }

  public getStats(pageIndex: number, pageSize: number): Observable<Page<Stats>> {
    let params = new HttpParams()
      .set('page', pageIndex)
      .set('size', pageSize);

    return this.httpClient.get<Page<Stats>>(`${SERVER_API_URL}/stats`, {params}).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    if(error.status == 0){
      console.error('An error occurred: ', error.error);
    } else {
      console.error(`Backend returned code ${error.status}, body was: `, error.error);
    }
    return throwError(() => new Error(error.error.message));
  }
}
