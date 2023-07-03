import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { CategoryBackendService } from "../../services/category-backend.service";
import * as CategoryActions from "../actions/category.actions"
import { catchError, map, of, switchMap } from "rxjs";
import { Page } from "../../models/page";
import { Category } from "../../models/category";

@Injectable()
export class CategoryEffects {
  constructor(private actions$: Actions, private categoryService: CategoryBackendService) {
  }

  loadCategories$ = createEffect(() => this.actions$.pipe(
      ofType(CategoryActions.loadingCategories),
      switchMap((action) =>
        this.categoryService.getAllCategories(
          action.pageIndex,
          action.pageSize,
          action.sortField,
          action.sortDirection
        ).pipe(
          map((page: Page<Category>) => {
            return CategoryActions.loadingCategoriesSuccessful({page})
          }),
          catchError((error: any) => of(CategoryActions.loadingCategoriesFailure({error})))
        )
      )
    )
  );
}
