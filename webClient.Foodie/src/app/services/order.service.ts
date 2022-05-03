import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Order } from '../models/order';
import { OrderStatus } from '../models/order-status';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl: string = environment.apiUrl;

  constructor(private http: HttpClient) { }

  public getOrders(status: string) : Observable<Order[]> {
    let params = new HttpParams();

    if(status) params = params.append('status', status);
    
    return this.http.get<Order[]>(this.apiUrl + 'orders/admin-list', { observe: 'response', params }).pipe(
      map((response: HttpResponse<Order[]>) => {
        return response.body;
      })
    );
  }

  public updateOrderStatus(orderStatus: OrderStatus): Observable<Object> {
    return this.http.patch(this.apiUrl + 'orders', orderStatus);
  }
}
