import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CategoryListComponent } from "./component/category-list/category-list.component";
import { AuthGuard } from "../utils/guard/auth.guard";
import { CategoryEditComponent } from "./component/category-edit/category-edit.component";
import { CategoryViewComponent } from "./component/category-view/category-view.component";
import { CategoryCreateComponent } from "./component/category-create/category-create.component";
import { ItemCreateComponent } from "./component/item-create/item-create.component";
import { ItemListComponent } from "./component/item-list/item-list.component";
import { ItemEditComponent } from "./component/item-edit/item-edit.component";
import { FirstPageComponent } from "./pages/first-page/first-page.component";
import { AdminPageComponent } from "./pages/admin-page/admin-page.component";
import { UserRouteAccessService } from "./services/user-route-access.service";
import { ItemViewComponent } from "./component/item-view/item-view.component";
import { Role } from "./models/role";
import { AccessDeniedPageComponent } from "./pages/access-denied-page/access-denied-page.component";

const routes: Routes = [
  {path: 'admin/categories', component: CategoryListComponent, data:{roles:[Role.Admin]}, canActivate: [UserRouteAccessService]},
  {path: 'admin/categories/:id/edit', component: CategoryEditComponent, data:{roles:[Role.Admin]}, canActivate: [UserRouteAccessService]},
  {path: 'admin/categories/:id/view', component: CategoryViewComponent, data:{roles:[Role.Admin]}, canActivate: [UserRouteAccessService]},
  {path: 'admin', component: AdminPageComponent, data:{roles:[Role.Admin]}, canActivate: [UserRouteAccessService]},
  {path: 'admin/categories/create', component: CategoryCreateComponent, data:{roles:[Role.Admin]}, canActivate: [UserRouteAccessService]},
  {path: 'admin/items/create', component: ItemCreateComponent,data:{roles:[Role.Admin]}, canActivate:[UserRouteAccessService]},
  {path: 'admin/items', component: ItemListComponent, data:{roles:[Role.Admin]}, canActivate:[ UserRouteAccessService]},
  {path: 'admin/items/:id/edit', component:ItemEditComponent, data:{roles:[Role.Admin]}, canActivate:[UserRouteAccessService]},
  {path: 'admin/items/:id/view', component: ItemViewComponent, data:{roles:[Role.Admin]}, canActivate:[UserRouteAccessService]},
  {path: '', component:FirstPageComponent, canActivate:[AuthGuard]},
  {path: 'access-denied', component:AccessDeniedPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
