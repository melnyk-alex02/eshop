import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CategoryListComponent } from './category-list/category-list.component';
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
import { initializeKeycloak } from "./init/keycloak-init.factory";
import { CategoryEditComponent } from './category-edit/category-edit.component';
import { ReactiveFormsModule } from "@angular/forms";
import { MatInputModule } from "@angular/material/input";
import { FirstPageComponent } from './first-page/first-page.component';
import { CategoryViewComponent } from './category-view/category-view.component';
import { MatCommonModule } from "@angular/material/core";
import { MatDialogModule } from "@angular/material/dialog";
import { DialogWindowComponent } from "./dialog-window/dialog-window.component";
import { CategoryCreateComponent } from './category-create/category-create.component';
import {ItemEditComponent} from "./item-edit/item-edit.component";
import {ItemCreateComponent} from "./item-create/item-create.component";
import {ItemListComponent} from "./item-list/item-list.component";
import {ItemViewComponent} from "./item-view/item-view.component";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatPaginatorModule} from "@angular/material/paginator";

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
    ItemViewComponent

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
    MatPaginatorModule
  ],
  providers: [{
    provide: APP_INITIALIZER,
    useFactory: initializeKeycloak,
    multi: true,
    deps: [KeycloakService],
  },
    CategoryBackendService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
