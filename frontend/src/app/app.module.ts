import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HTTP_INTERCEPTORS, HttpClientModule } from "@angular/common/http";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { MatTableModule } from "@angular/material/table";
import { MatSelectModule } from "@angular/material/select";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatButtonModule } from "@angular/material/button";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { CategoryEditComponent } from './pages/category-edit/category-edit.component';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatInputModule } from "@angular/material/input";
import { FirstPageComponent } from './pages/first-page/first-page.component';
import { CategoryViewComponent } from './pages/category-view/category-view.component';
import { MatCommonModule } from "@angular/material/core";
import { MatDialogModule } from "@angular/material/dialog";
import { DialogWindowComponent } from "./component/dialog-window/dialog-window.component";
import { CategoryCreateComponent } from './pages/category-create/category-create.component';
import { ItemEditComponent } from "./pages/item-edit/item-edit.component";
import { ItemCreateComponent } from "./pages/item-create/item-create.component";
import { ItemListComponent } from "./pages/item-list/item-list.component";
import { ItemViewComponent } from "./pages/item-view/item-view.component";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { AdminPageComponent } from "./pages/admin-page/admin-page.component";
import { AccessDeniedPageComponent } from "./pages/access-denied-page/access-denied-page.component";
import { CategoryListComponent } from "./pages/category-list/category-list.component";
import { MatSortModule } from "@angular/material/sort";
import { StorageModule } from "./store/storage.module";
import { MatIconModule } from "@angular/material/icon";
import { MatCardModule } from "@angular/material/card";
import { AllItemsPageComponent } from './pages/all-items-page/all-items-page.component';
import { MyCartComponent } from './pages/my-cart/my-cart.component';
import { OrderViewComponent } from './pages/order-view/order-view.component';
import { MyOrdersComponent } from './pages/my-orders/my-orders.component';
import { MatGridListModule } from "@angular/material/grid-list";
import { RouterModule } from "@angular/router";
import { OrdersListComponent } from './pages/orders-list/orders-list.component';
import { MatExpansionModule } from "@angular/material/expansion";
import { MatListModule } from "@angular/material/list";
import { MatMenuModule } from "@angular/material/menu";
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { MatBadgeModule } from "@angular/material/badge";
import { Interceptor } from "./inteceptor/interceptor";
import { UserBackendService } from "./services/user-backend.service";
import { first } from "rxjs";
import { UserLoginComponent } from "./pages/user-login/user-login.component";
import { UserRegisterComponent } from "./pages/user-register/user-register.component";
import { UserResetPasswordComponent } from "./pages/user-reset-password/user-reset-password.component";
import { UserVerifyEmailComponent } from "./pages/user-verify-email/user-verify-email.component";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { UserSendEmailPasswordResetComponent } from './pages/user-send-email-password-reset/user-send-email-password-reset.component';
import { UserProfileComponent } from './pages/user-profile/user-profile.component';
import { NgOptimizedImage } from "@angular/common";


@NgModule({
  declarations: [
    CategoryListComponent,
    CategoryEditComponent,
    FirstPageComponent,
    CategoryViewComponent,
    DialogWindowComponent,
    CategoryCreateComponent,
    ItemEditComponent,
    ItemCreateComponent,
    ItemListComponent,
    ItemViewComponent,
    AdminPageComponent,
    AccessDeniedPageComponent,
    AllItemsPageComponent,
    MyCartComponent,
    OrderViewComponent,
    MyOrdersComponent,
    AppComponent,
    OrdersListComponent,
    NotFoundComponent,
    UserLoginComponent,
    UserRegisterComponent,
    UserResetPasswordComponent,
    UserVerifyEmailComponent,
    UserSendEmailPasswordResetComponent,
    UserProfileComponent,
  ],
  imports: [
    RouterModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatSelectModule,
    MatTableModule,
    MatSnackBarModule,
    MatInputModule,
    MatDialogModule,
    MatCommonModule,
    MatSortModule,
    HttpClientModule,
    NgbModule,
    AppRoutingModule,
    ReactiveFormsModule,
    MatToolbarModule,
    MatPaginatorModule,
    MatIconModule,
    MatProgressSpinnerModule,
    StorageModule,
    FormsModule,
    MatCardModule,
    MatGridListModule,
    MatToolbarModule,
    MatExpansionModule,
    MatListModule,
    MatMenuModule,
    MatBadgeModule,
    MatCheckboxModule,
    NgOptimizedImage,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: Interceptor,
      multi: true
    },
    {
      provide: APP_INITIALIZER,
      useFactory: (userService: UserBackendService) => () => {
        userService.checkAuthenticationAndGetUser().pipe(first()).subscribe();
      },
      deps: [UserBackendService],
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
