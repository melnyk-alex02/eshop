import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { KeycloakAuthGuard, KeycloakService } from 'keycloak-angular';
import { MatSnackBar } from "@angular/material/snack-bar";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard extends KeycloakAuthGuard {


  constructor(
    protected override readonly router: Router,
    protected readonly keycloak: KeycloakService,
    private snackBar: MatSnackBar
  ) {
    super(router, keycloak);
  }

  async isAccessAllowed(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Promise<boolean | UrlTree> {

    if (!this.authenticated) {
      await this.keycloak.login({
        redirectUri: window.location.origin + state.url,
      });
    }

    let roles = route.data['roles'];

    if (!roles || roles.length === 0) {
      return this.authenticated
    }

    for (let role of roles) {
      if (this.keycloak.isUserInRole(role)) {
        return this.authenticated;
      }
    }
    this.snackBar.open("Access denied", '', {
      duration: 5000,
    })
    return false;
  }
}
