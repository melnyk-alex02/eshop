import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from "@angular/router";
import { KeycloakAngularModule, KeycloakService } from "keycloak-angular";

@Injectable({
  providedIn: 'root'
})
export class UserRouteAccessService implements CanActivate {
  constructor(protected router: Router, protected keycloakService: KeycloakService, protected keycloakAngular: KeycloakAngularModule) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | Promise<boolean> {
    const role = this.keycloakService.getUserRoles();
    console.log(route.data['roles'].includes(role.at(-1)))
    if (role) {
      if (route.data['roles'].includes(role.at(-1))) {
        return true;
      } else {
        this.router.navigate(['access-denied'])
        return false;
      }
    }
    this.router.navigate(['/'])
    return true;
  }
}
