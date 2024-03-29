import { Component, OnInit } from '@angular/core';
import { CategoryBackendService } from "../../services/category-backend.service";
import { KeycloakService } from "keycloak-angular";
import { ActivatedRoute, ParamMap } from "@angular/router";
import { Role } from "../../models/role";
import { Subject, switchMap, takeUntil } from "rxjs";
import { Category } from "../../models/category";

@Component({
  selector: 'app-category-view',
  templateUrl: './category-view.component.html',
  styleUrls: ['./category-view.component.css']
})
export class CategoryViewComponent implements OnInit {

  category: Category;

  role: boolean;

  private unsubscribe: Subject<void> = new Subject<void>();

  constructor(private categoryService: CategoryBackendService,
              private route: ActivatedRoute,
              private keycloak: KeycloakService) {
  }

  ngOnInit() {
    this.role = this.keycloak.isUserInRole(Role.Admin);

    this.route.paramMap.pipe(
      takeUntil(this.unsubscribe),
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

  ngOnDestroy() {
    this.unsubscribe.complete();
    this.unsubscribe.next();
  }
}
