import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable, ReplaySubject } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Login} from '../models/login';
import { Account } from '../models/account';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private apiUrl: string = environment.apiUrl;
  private currentUserSource: ReplaySubject<Account> = new ReplaySubject<Account>(1);
  public currentUser$: Observable<Account> = this.currentUserSource.asObservable();
  
  constructor(private http: HttpClient) { }

  public login(user: Login): Observable<void> {
    return this.http.post(this.apiUrl + 'account/login', user).pipe(
      map((user: Account) => {
        if(user) {
          this.setCurrentUser(user);
        }
      })
    );
  }

  public logut(): void {
    localStorage.removeItem('user');
    this.currentUserSource.next(null);
  }
  
  public setCurrentUser(user: Account): void {
    user.role = JSON.parse(atob(user.token.split('.')[1])).role;
    localStorage.setItem('user', JSON.stringify(user));
    this.currentUserSource.next(user);
  }
}
