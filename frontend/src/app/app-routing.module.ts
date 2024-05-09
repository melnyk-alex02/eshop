import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CategoryListComponent } from "./pages/category-list/category-list.component";
import { CategoryEditComponent } from "./pages/category-edit/category-edit.component";
import { CategoryViewComponent } from "./pages/category-view/category-view.component";
import { CategoryCreateComponent } from "./pages/category-create/category-create.component";
import { ItemCreateComponent } from "./pages/item-create/item-create.component";
import { ItemListComponent } from "./pages/item-list/item-list.component";
import { ItemEditComponent } from "./pages/item-edit/item-edit.component";
import { AdminPageComponent } from "./pages/admin-page/admin-page.component";
import { ItemViewComponent } from "./pages/item-view/item-view.component";
import { Role } from "./models/role";
import { AccessDeniedPageComponent } from "./pages/access-denied-page/access-denied-page.component";
import { AllItemsPageComponent } from "./pages/all-items-page/all-items-page.component";
import { MyCartComponent } from "./pages/my-cart/my-cart.component";
import { OrderViewComponent } from "./pages/order-view/order-view.component";
import { MyOrdersComponent } from "./pages/my-orders/my-orders.component";
import { OrdersListComponent } from "./pages/orders-list/orders-list.component";
import { NotFoundComponent } from "./pages/not-found/not-found.component";
import { UserLoginComponent } from "./pages/user-login/user-login.component";
import { authGuard } from "./utils/guard/auth.guard";
import { UserRegisterComponent } from "./pages/user-register/user-register.component";
import { UserResetPasswordComponent } from "./pages/user-reset-password/user-reset-password.component";
import { UserVerifyEmailComponent } from "./pages/user-verify-email/user-verify-email.component";
import { UserProfileComponent } from "./pages/user-profile/user-profile.component";
import {
  UserSendEmailPasswordResetComponent
} from "./pages/user-send-email-password-reset/user-send-email-password-reset.component";

const routes: Routes = [
  {path: 'admin/categories', component: CategoryListComponent, data: {roles: [Role.Admin]}, canActivate: [authGuard]},
  {
    path: 'admin/categories/:id/edit',
    component: CategoryEditComponent,
    data: {roles: [Role.Admin]},
    canActivate: [authGuard]
  },
  {
    path: 'admin/categories/:id/view',
    component: CategoryViewComponent,
    data: {roles: [Role.Admin]},
    canActivate: [authGuard]
  },
  {path: 'admin', component: AdminPageComponent, data: {roles: [Role.Admin]}, canActivate: [authGuard]},
  {
    path: 'admin/categories/create',
    component: CategoryCreateComponent,
    data: {roles: [Role.Admin]},
    canActivate: [authGuard]
  },
  {path: 'admin/items/create', component: ItemCreateComponent, data: {roles: [Role.Admin]}, canActivate: [authGuard]},
  {path: 'admin/items', component: ItemListComponent, data: {roles: [Role.Admin]}, canActivate: [authGuard]},
  {path: 'admin/items/:id/edit', component: ItemEditComponent, data: {roles: [Role.Admin]}, canActivate: [authGuard]},
  {path: 'admin/items/:id/view', component: ItemViewComponent, data: {roles: [Role.Admin]}, canActivate: [authGuard]},
  {path: 'admin/orders', component: OrdersListComponent, data: {roles: [Role.Admin]}, canActivate: [authGuard]},
  {
    path: 'admin/orders/:orderNumber/view',
    component: OrderViewComponent,
    data: {roles: [Role.Admin]},
    canActivate: [authGuard]
  },
  {path: 'access-denied', component: AccessDeniedPageComponent},
  {path: 'all-items', component: AllItemsPageComponent},
  {path: 'all-items/:id/view', component: ItemViewComponent},
  {path: 'my-cart', component: MyCartComponent},
  {path: 'my-order/:orderNumber', component: OrderViewComponent},
  {path: 'my-orders', component: MyOrdersComponent, canActivate: [authGuard], data: {roles: [Role.User]}},
  {path: 'not-found', component: NotFoundComponent},
  {path: '', redirectTo: "/all-items", pathMatch: 'full'},
  {path:'login', component: UserLoginComponent},
  {path: 'register', component: UserRegisterComponent},
  {path: "profile", component: UserProfileComponent, canActivate: [authGuard], data:{roles: [Role.User]}},
  {path: 'reset-password', component: UserSendEmailPasswordResetComponent},
  {path: "reset-password/:oneTimePassword", component: UserResetPasswordComponent},
  {path: "verify-email/:oneTimePassword", component: UserVerifyEmailComponent, canActivate: [authGuard], data: {roles: [Role.User]}},
  {path: '**', redirectTo: "/not-found"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
