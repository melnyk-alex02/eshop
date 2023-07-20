import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from "@angular/common/http";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { MatTableModule } from "@angular/material/table";
import { MatSelectModule } from "@angular/material/select";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatButtonModule } from "@angular/material/button";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { KeycloakAngularModule, KeycloakService } from "keycloak-angular";
import { initializeKeycloak } from "./utils/init/keycloak-init.factory";
import { CategoryEditComponent } from './pages/category-edit/category-edit.component';
import { ReactiveFormsModule } from "@angular/forms";
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
    AccessDeniedPageComponent,
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
    MatSortModule,
    HttpClientModule,
    NgbModule,
    KeycloakAngularModule,
    AppRoutingModule,
    ReactiveFormsModule,
    MatToolbarModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    StorageModule
  ],
  providers: [{
    provide: APP_INITIALIZER,
    useFactory: initializeKeycloak,
    multi: true,
    deps: [KeycloakService],
  }],
  bootstrap: [AppComponent]
})
export class AppModule {
}
