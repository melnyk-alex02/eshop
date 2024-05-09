import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { SERVER_API_URL } from "../constants/app.constants";
import { catchError, map, Observable, throwError } from "rxjs";
import { User } from "../models/user";
import { Router } from "@angular/router";
import { UserRegister } from "../models/user-register";

@Injectable({
  providedIn: 'root'
})
export class UserBackendService {

  constructor(private http: HttpClient,
              private router: Router) {
  }

  public getToken(email: string, password: string, rememberMe: boolean) {
    const headers = new HttpHeaders({'Content-Type': 'application/x-www-form-urlencoded'});
    const body = new URLSearchParams();
    body.set('email', email);
    body.set('password', password);
    body.set('rememberMe', rememberMe.toString());

    return this.http.post(`${SERVER_API_URL}/users/token`, body.toString(), {headers});
  }

  public register(userRegister: UserRegister, rememberMe: boolean) {
    return this.http.post(`${SERVER_API_URL}/users/register?rememberMe=${rememberMe}`, userRegister);
  }

  public getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${SERVER_API_URL}/users/current`, {});
  }

  public refreshToken() {
    return this.http.post(`${SERVER_API_URL}/users/refresh-token`, {});
  }

  public sendEmailForPasswordReset(email: string) {
    return this.http.post(`${SERVER_API_URL}/users/send-reset-password-email?email=${email}`, {});
  }

  public resetPassword(email: string | undefined, otp: string | undefined, newPassword: string, confirmPassword: string) {
    return this.http.post(`${SERVER_API_URL}/users/reset-password?&email=${email}&code=${otp}&password=${newPassword}&confirmPassword=${confirmPassword}`, {});
  }

  public sendEmailForVerification() {
    return this.http.post(`${SERVER_API_URL}/users/send-email-verification`, {});
  }

  public verifyEmail(otp: string | undefined) {
    return this.http.post(`${SERVER_API_URL}/users/verify-email?code=${otp}`, {});
  }

  public checkAuthenticationAndGetUser() {
    return this.http.get<User>(`${SERVER_API_URL}/users/current`).pipe(
      map(response => {
        if (response) {
          localStorage.setItem("user", JSON.stringify(response));
          console.log(localStorage.getItem("user"))
          return response;
        } else {
          this.router.createUrlTree(['/login'], {queryParams: {returnUrl: this.router.routerState.root.url}})
          localStorage.removeItem("user");
          return null;
        }
      }),
      catchError(error => {
        if (error) {
          this.router.createUrlTree(['/login'], {queryParams: {returnUrl: this.router.url}});
        }
        return throwError(error);
      })
    );
  }

  public isLoggedIn() {
    return localStorage.getItem("user") !== null;
  }

  public isAdmin() {
    if (localStorage.getItem("user") === null && !this.isLoggedIn()) {
      return false;
    }

    const user = localStorage.getItem("user");
    const userObj = JSON.parse(user ? user : '');
    return userObj?.roles.includes("ROLE_ADMIN");
  }

  public logout() {
    localStorage.removeItem("user")
    return this.http.post(`${SERVER_API_URL}/users/logout`, {});
  }
}
