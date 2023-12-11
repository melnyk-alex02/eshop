import {Component} from '@angular/core';
import {KeycloakService} from "keycloak-angular";
import {Role} from "./models/role";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'eshop-ui';

  isAdmin = this.keycloakService.isUserInRole(Role.Admin);

  constructor(private keycloakService: KeycloakService,
  ) {
  }
}
