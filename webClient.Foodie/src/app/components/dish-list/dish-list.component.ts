import { Component, OnInit } from '@angular/core';
import { DishList } from 'src/app/models/dish-list';
import { DishService } from 'src/app/services/dish.service';

@Component({
  selector: 'app-dish-list',
  templateUrl: './dish-list.component.html',
  styleUrls: ['./dish-list.component.scss']
})
export class DishListComponent implements OnInit {
  public dishList: DishList[];
  public nameSearchStr: string = '';
  public orderBy: string = 'NOT_MATTER';
  public orderByType: string = 'ASC';
  public isVisible: string = 'NOT_MATTER';

  constructor(private dishService: DishService) { }

  public ngOnInit(): void {
    this.getDishList();
  }

  public getDishList(): void {
    this.dishService.getDishList(this.nameSearchStr, this.orderBy, this.orderByType, this.isVisible)
      .subscribe((dishList: DishList[]) => {
        this.dishList = dishList;
      });
  }
}
