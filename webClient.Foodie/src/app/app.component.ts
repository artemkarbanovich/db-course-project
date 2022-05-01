import { Component } from '@angular/core';
import { Account } from './models/account';
import { AccountService } from './services/account.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  constructor(private accountService: AccountService) { }

  public ngOnInit(): void {
    this.setCurrentUser();  
  }
  
  private setCurrentUser(): void {
    const user: Account = JSON.parse(localStorage.getItem('user'));
    if(user) {
      this.accountService.setCurrentUser(user);
    }
  }
}
