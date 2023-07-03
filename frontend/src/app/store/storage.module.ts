import { NgModule } from "@angular/core";
import { StoreModule } from "@ngrx/store";
import { EffectsModule } from "@ngrx/effects";
import { StoreDevtoolsModule } from "@ngrx/store-devtools";
import { ItemEffects } from "./effects/item.effects";
import { reducers } from "./index";
import { CategoryEffects } from "./effects/category.effects";

@NgModule({
  imports: [
    StoreModule.forRoot(reducers),
    EffectsModule.forRoot([ItemEffects, CategoryEffects]),
    StoreDevtoolsModule.instrument({maxAge: 25})
  ], exports: [StoreModule]
})
export class StorageModule{

}
