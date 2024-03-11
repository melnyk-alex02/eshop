import { Component } from '@angular/core';
import { KeycloakService } from "keycloak-angular";
import { Role } from "./models/role";
import { Subject } from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'eshop-ui';

  countOfItemsInCart: number;

  isAdmin = this.keycloakService.isUserInRole(Role.Admin);

  private unsubscribe: Subject<void> = new Subject();

  constructor(private keycloakService: KeycloakService) {
  }

  getCountOfItemsInCart() {
    return this.countOfItemsInCart = Number(localStorage.getItem("countOfItemsInCart"));
  }
}
