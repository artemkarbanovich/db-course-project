import { Component, OnInit } from '@angular/core';
import { Order } from 'src/app/models/order';
import { OrderStatus } from 'src/app/models/order-status';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss']
})
export class OrderComponent implements OnInit {
  public orders: Order[];
  public orderStatus: string = 'NOT_MATTER';
  
  constructor(private orderService: OrderService) { }

  public ngOnInit(): void {
    this.getOrders();
  }

  public updateStatus(orderId: number, status: string): void {
    let orderStatus: OrderStatus = {
      orderId: orderId,
      status: status
    };

    this.orderService.updateOrderStatus(orderStatus).subscribe(() => { });
  }

  public getOrders(): void {
    this.orderService.getOrders(this.orderStatus).subscribe((orders: Order[]) => {
      this.orders = orders;
    });
  }
}
