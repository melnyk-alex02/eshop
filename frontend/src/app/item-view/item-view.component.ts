import { Component } from '@angular/core';
import { Item, ItemBackendService } from "../services/item-backend.service";
import { Subject, switchMap, takeUntil } from "rxjs";
import { ActivatedRoute, ParamMap } from "@angular/router";

@Component({
  selector: 'app-item-view',
  templateUrl: './item-view.component.html',
  styleUrls: ['./item-view.component.css']
})
export class ItemViewComponent {

  item: Item;

  role: boolean;

  loading: boolean;

  private unsubscribe: Subject<void> = new Subject();

  constructor(private itemService: ItemBackendService,
              private route: ActivatedRoute) {
  }

  ngOnInit(){
    this.loading = true;

    this.route.paramMap.pipe(
      takeUntil(this.unsubscribe),
      switchMap((params: ParamMap) =>{
        const id = params.get('id');
        return this.itemService.getItemById(Number(id));
      })
    )
      .subscribe(data =>{
        this.item = data;
      })
      .add(() =>{

        this.loading = true;
      })
  }
}
