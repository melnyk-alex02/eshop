import { Component } from '@angular/core';
import { KeycloakService } from "keycloak-angular";

@Component({
  selector: 'app-first-page',
  templateUrl: './first-page.component.html',
  styleUrls: ['./first-page.component.css']
})
export class FirstPageComponent {
  constructor(private keycloak:KeycloakService) {}

  isAdmin = this.keycloak.isUserInRole('ROLE_ADMIN')
}
