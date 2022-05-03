import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DishItemComponent } from './components/dish-item/dish-item.component';
import { DishListComponent } from './components/dish-list/dish-list.component';
import { DishComponent } from './components/dish/dish.component';
import { HomeComponent } from './components/home/home.component';
import { OrderComponent } from './components/order/order.component';
import { StatisticComponent } from './components/statistic/statistic.component';
import { AuthorizationGuard } from './guards/authorization.guard';

const routes: Routes = [
  { path: '', component: HomeComponent },
  {
    path: '',
    runGuardsAndResolvers: 'always',
    canActivate: [AuthorizationGuard],
    children: [
      { path: 'dishes', component: DishComponent },
      { path: 'dishes/:id', component: DishItemComponent },
      { path: 'orders', component: OrderComponent },
      { path: 'statistics', component: StatisticComponent }
    ]
  },
  { path: '**', component: HomeComponent, pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
