import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { BehaviorSubject, filter, Observable, switchMap, take, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { UserBackendService } from "../services/user-backend.service";
import { Router } from "@angular/router";

@Injectable()
export class Interceptor implements HttpInterceptor {
  private isRefreshingToken = false;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

  constructor(private userService: UserBackendService,
              private router: Router
  ) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError(error => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          if (localStorage.getItem("user")) {
            return this.handleUnauthorizedError(req, next);
          }
          next.handle(req);
        }
        return throwError(error);
      })
    );
  }

  private handleUnauthorizedError(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!this.isRefreshingToken) {
      this.isRefreshingToken = true;
      this.refreshTokenSubject.next(null);

      return this.userService.refreshToken().pipe(
        switchMap(() => {
          this.isRefreshingToken = false;
          this.refreshTokenSubject.next(true);
          return next.handle(req.clone());
        }),
        catchError((error) => {
          this.isRefreshingToken = false;
          localStorage.removeItem("user");
          this.router.createUrlTree(['/login'], {queryParams: {returnUrl: this.router.url}});

          return next.handle(req.clone());

        })
      );
    } else {
      return this.refreshTokenSubject.pipe(
        filter(isRefreshed => isRefreshed !== null),
        take(1),
        switchMap(() => {
          return next.handle(req.clone());
        })
      );
    }
  }
}
