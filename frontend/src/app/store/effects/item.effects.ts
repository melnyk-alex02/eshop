import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { ItemBackendService } from "../../services/item-backend.service";
import { catchError, map, of, switchMap } from "rxjs";
import { Item } from "../../models/item";
import * as ItemActions from "../actions/item.actions"
import { Page } from "../../models/page";

@Injectable()
export class ItemEffects {
  constructor(private actions$: Actions, private itemService: ItemBackendService) {
  }

  loadItems$ = createEffect(() => this.actions$.pipe(
      ofType(ItemActions.loadingItems),
      switchMap((action) =>
        this.itemService.getAllItems(
          action.pageIndex,
          action.pageSize,
          action.sortField,
          action.sortDirection
        ).pipe(
          map((page: Page<Item>) => {
            return ItemActions.loadingItemsSuccessful({page})
          }),
          catchError((error: any) => of(ItemActions.loadingItemsFailure({error})))
        )
      )
    )
  );
}
