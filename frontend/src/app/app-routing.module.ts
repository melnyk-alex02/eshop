import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CategoryListComponent } from "./category-list/category-list.component";
import { AuthGuard } from "./guard/auth.guard";
import { CategoryEditComponent } from "./category-edit/category-edit.component";
import { FirstPageComponent } from "./first-page/first-page.component";
import { CategoryViewComponent } from "./category-view/category-view.component";
import { CategoryCreateComponent } from "./category-create/category-create.component";

const routes: Routes = [
  {path: 'admin/categories', component: CategoryListComponent, canActivate: [AuthGuard]},
  {path: 'admin/categories/:id/edit', component: CategoryEditComponent, canActivate: [AuthGuard]},
  {path: 'admin/categories/:id/view', component: CategoryViewComponent, canActivate: [AuthGuard]},
  {path: '', component: FirstPageComponent, canActivate: [AuthGuard]},
  {path: 'admin/categories/create', component: CategoryCreateComponent, canActivate: [AuthGuard]},
  {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
