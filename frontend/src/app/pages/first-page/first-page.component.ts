import { Component } from '@angular/core';
import { KeycloakService } from "keycloak-angular";
import { Role } from "../../models/role";

@Component({
  selector: 'app-first-page',
  templateUrl: './first-page.component.html',
  styleUrls: ['./first-page.component.css']
})
export class FirstPageComponent {
  isUser = this.keycloak.isUserInRole(Role.User);
  isAdmin = this.keycloak.isUserInRole(Role.Admin);

  constructor(private keycloak: KeycloakService) {
  }
}
