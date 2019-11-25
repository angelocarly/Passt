import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
/**
 * Homepage component
 */
export class HomeComponent implements OnInit {

  public isLoggedIn = false;

  private refreshToken = this._auth.getRefreshToken();
  private accessToken = this._auth.getAccessToken();

  constructor(private _auth: AuthService) { }

  ngOnInit() {
    this.isLoggedIn = this._auth.checkCredentials();
  }

  refresh() {
    this._auth.refreshAccessToken().subscribe(data => {
      this.refreshToken = this._auth.getRefreshToken();
      this.accessToken = this._auth.getAccessToken();
    });
  }
}
