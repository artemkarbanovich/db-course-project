import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Statistic } from '../models/statistic';

@Injectable({
  providedIn: 'root'
})
export class StatisticService {
  private apiUrl: string = environment.apiUrl;

  constructor(private http: HttpClient) { }
  
  public getStatistics() : Observable<Statistic> {
    return this.http.get<Statistic>(this.apiUrl + 'statistics/admin');
  }
}
