import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Login } from 'src/app/models/login';
import { AccountService } from 'src/app/services/account.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  public loginForm: FormGroup;

  constructor(public accountService: AccountService, private formBuilder: FormBuilder,
    private toastr: ToastrService, private router: Router) { }

  public ngOnInit(): void {
    this.initForm();
  }

  public login(): void {
    const user: Login = {
      email: this.loginForm.controls['email'].value,
      password: this.loginForm.controls['password'].value
    };

    this.accountService.login(user).subscribe({
      complete: () => {
        this.loginForm.reset();
        this.toastr.success('You successfully logged in');
      },
      error: (error) => {
        this.toastr.error(error.error);
      }
    });
  }

  public logout(): void {
    this.accountService.logut();
    this.router.navigateByUrl('/');
  }

  private initForm(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(32)]]
    });
  }
}
