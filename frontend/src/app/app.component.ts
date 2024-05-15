import { Component } from '@angular/core';
import { of, Subject } from "rxjs";
import { UserBackendService } from "./services/user-backend.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'eshop-ui';

  countOfItemsInCart: number;

  constructor(private userService: UserBackendService) {
    console.log(this.isUserLoggedIn())
  }

  getCountOfItemsInCart() {
    return this.countOfItemsInCart = Number(localStorage.getItem("countOfItemsInCart"));
  }

  isAdmin() : boolean {
    return this.userService.isAdmin();
  }

  isUserLoggedIn() : boolean {
    return this.userService.isLoggedIn();
  }
}
