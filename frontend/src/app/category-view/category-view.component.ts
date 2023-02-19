import { Component, OnInit, ViewChild } from '@angular/core';
import { Category, CategoryBackendService } from "../services/category-backend.service";
import { KeycloakService } from "keycloak-angular";
import { ActivatedRoute } from "@angular/router";
import { Item, ItemBackendService } from "../services/item-backend.service";
import { MatTable } from "@angular/material/table";

@Component({
  selector: 'app-category-view',
  templateUrl: './category-view.component.html',
  styleUrls: ['./category-view.component.css']
})
export class CategoryViewComponent implements OnInit {

  id: number = this.route.snapshot.params['id'];

  category: Category;

  items: Item[];

  displayedColumns: string[] = ['id', 'name', 'description', 'categoryId', 'imageSrc', 'actions']

  role: boolean;

  @ViewChild(MatTable) table: any;

  constructor(private categoryService: CategoryBackendService,
              private itemService: ItemBackendService,
              private route: ActivatedRoute,
              private keycloak: KeycloakService) {
  }

  ngOnInit() {
    this.getCategoryById();
  }

  getCategoryById() {
    this.role = this.keycloak.isUserInRole('ROLE_ADMIN');

    this.categoryService.getCategoryById(this.id).subscribe(data => {
      this.category = data;
    })

    this.itemService.getItemsByCategoryId(this.id.valueOf()).subscribe(data => {
      this.items = data;

      this.table.renderRows();
    });
  }
}
