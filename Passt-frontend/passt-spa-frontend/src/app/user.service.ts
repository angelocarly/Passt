import { Injectable } from '@angular/core';
import { AuthService } from './auth/auth.service';
import { HttpClient } from '@angular/common/http';
import { Config } from './config/config';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http : HttpClient, private _config : Config) { }

  public getUser()
  {
    return this.http.get(this._config.spUrl +  '/user/me');
  }

}
