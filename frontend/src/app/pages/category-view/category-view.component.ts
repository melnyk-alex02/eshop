import { Component, DestroyRef, OnInit } from '@angular/core';
import { CategoryBackendService } from "../../services/category-backend.service";
import { ActivatedRoute, ParamMap } from "@angular/router";
import { switchMap } from "rxjs";
import { Category } from "../../models/category";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";

@Component({
  selector: 'app-category-view',
  templateUrl: './category-view.component.html',
  styleUrls: ['./category-view.component.css']
})
export class CategoryViewComponent implements OnInit {

  category: Category;

  constructor(private categoryService: CategoryBackendService,
              private route: ActivatedRoute,
              private destroyRef: DestroyRef) {
  }

  ngOnInit() {

    this.route.paramMap.pipe(
      takeUntilDestroyed(this.destroyRef),
      switchMap((params: ParamMap) => {
        const id = params.get('id');

        return this.categoryService.getCategoryById(Number(id));
      })
    )
      .subscribe({
        next: (data) => {
          this.category = data;
        }
      });
  }
}
