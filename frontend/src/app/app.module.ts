import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CategoryListComponent } from './component/category-list/category-list.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CategoryBackendService } from "./services/category-backend.service";
import { HttpClientModule } from "@angular/common/http";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { MatTableModule } from "@angular/material/table";
import { MatSelectModule } from "@angular/material/select";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatButtonModule } from "@angular/material/button";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { KeycloakAngularModule, KeycloakService } from "keycloak-angular";
import { initializeKeycloak } from "../utils/init/keycloak-init.factory";
import { CategoryEditComponent } from './component/category-edit/category-edit.component';
import { ReactiveFormsModule } from "@angular/forms";
import { MatInputModule } from "@angular/material/input";
import { FirstPageComponent } from './pages/first-page/first-page.component';
import { CategoryViewComponent } from './component/category-view/category-view.component';
import { MatCommonModule } from "@angular/material/core";
import { MatDialogModule } from "@angular/material/dialog";
import { DialogWindowComponent } from "./component/dialog-window/dialog-window.component";
import { CategoryCreateComponent } from './component/category-create/category-create.component';
import { ItemEditComponent } from "./component/item-edit/item-edit.component";
import { ItemCreateComponent } from "./component/item-create/item-create.component";
import { ItemListComponent } from "./component/item-list/item-list.component";
import { ItemViewComponent } from "./component/item-view/item-view.component";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { AdminPageComponent } from "./pages/admin-page/admin-page.component";
import { AccessDeniedPageComponent } from "./pages/access-denied-page/access-denied-page.component";
import { UserRouteAccessService } from "./services/user-route-access.service";

@NgModule({
  declarations: [
    AppComponent,
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
    AccessDeniedPageComponent

  ],
  imports: [
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
    HttpClientModule,
    NgbModule,
    KeycloakAngularModule,
    AppRoutingModule,
    ReactiveFormsModule,
    MatToolbarModule,
    MatPaginatorModule,
    MatProgressSpinnerModule
  ],
  providers: [{
    provide: APP_INITIALIZER,
    useFactory: initializeKeycloak,
    useClass: UserRouteAccessService,
    multi: true,
    deps: [KeycloakService],
  },
    CategoryBackendService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
