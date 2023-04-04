import { Component } from '@angular/core';
import { KeycloakService } from "keycloak-angular";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent{
  title = 'eshop-ui';

  constructor(private keycloakService: KeycloakService) {
  }
   isAdmin = this.keycloakService.isUserInRole('ROLE_USER')
}
