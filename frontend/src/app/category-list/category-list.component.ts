import {Component, OnInit, ViewChild} from '@angular/core';
import {Category, CategoryBackendService} from "../services/category-backend.service";
import {MatTable, MatTableDataSource} from "@angular/material/table";
import {KeycloakService} from "keycloak-angular";
import {keyframes} from "@angular/animations";

@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.css']
})
export class CategoryListComponent implements OnInit {


  categories: Category[] = [];
  displayedColumns: string[] = ['id', 'name', 'description']
  public dataSource = new MatTableDataSource<Category>();
  public  role: boolean;
  @ViewChild(MatTable) table: any;

  constructor(private categoryService: CategoryBackendService,
              private keycloak:KeycloakService) {
  }



  ngOnInit() {
    this.getAllCategories();
  }
  getAllCategories(){
    this.role=this.keycloak.isUserInRole('ROLE_USER');
    this.categoryService.getAllCategories().subscribe((res)=>{
      this.dataSource.data = res;
      this.table.renderRows();
    })
  }
}
