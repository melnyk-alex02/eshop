import { Component, OnInit } from '@angular/core';
import { ItemBackendService } from "../../services/item-backend.service";
import { Subject, switchMap, takeUntil } from "rxjs";
import { ActivatedRoute, ParamMap } from "@angular/router";
import { Item } from "../../models/item";

@Component({
  selector: 'app-item-view',
  templateUrl: './item-view.component.html',
  styleUrls: ['./item-view.component.css']
})
export class ItemViewComponent implements OnInit {

  item: Item;

  private unsubscribe: Subject<void> = new Subject();

  constructor(private itemService: ItemBackendService,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.paramMap.pipe(
      takeUntil(this.unsubscribe),
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

  ngOnDestroy() {
    this.unsubscribe.complete();
    this.unsubscribe.next();
  }
}
