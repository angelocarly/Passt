import { Component } from '@angular/core';
import { AuthService } from './auth/auth.service';
import { HttpClient } from '@angular/common/http';
import { UserService } from './user.service';
import { Config } from './config/config';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  
  //Variables for html
  public isLoggedIn = false;
  private username: String;
  public idpUrl: String;

  constructor(private http: HttpClient, private _service: AuthService, private _user : UserService, private _config : Config) { }

  //Load user data from services
  ngOnInit() {

    this.isLoggedIn = this._service.checkCredentials();

    if (this.isLoggedIn) {
      this._user.getUser().subscribe(
        data => {
          this.username = data['name'];
        }
      );
    }

    this.idpUrl = this._config.idpUrl;
  }

  login() {
    this._service.login();
  }

  logout() {
    this._service.logout();
  }
}
