import { Component, DestroyRef, OnInit } from '@angular/core';
import { ItemBackendService } from "../../services/item-backend.service";
import { switchMap } from "rxjs";
import { ActivatedRoute, ParamMap } from "@angular/router";
import { Item } from "../../models/item";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";

@Component({
  selector: 'app-item-view',
  templateUrl: './item-view.component.html',
  styleUrls: ['./item-view.component.css']
})
export class ItemViewComponent implements OnInit {

  item: Item;

  constructor(private itemService: ItemBackendService,
              private route: ActivatedRoute,
              private destroyRef: DestroyRef) {
  }

  ngOnInit() {
    this.route.paramMap.pipe(
      takeUntilDestroyed(this.destroyRef),
      switchMap((params: ParamMap) => {
        const id = params.get('id');
        return this.itemService.getItemById(Number(id));
      })
    )
      .subscribe({
        next: data => {
          this.item = data;
        }
      });
  }
}
