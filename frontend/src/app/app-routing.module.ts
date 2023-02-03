import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CategoryListComponent} from "./category-list/category-list.component";
import {AuthGuard} from "./guard/auth.guard";

const routes: Routes = [
  {path:'admin/categories', component: CategoryListComponent, canActivate:[AuthGuard]},
  {path:'**',  redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
