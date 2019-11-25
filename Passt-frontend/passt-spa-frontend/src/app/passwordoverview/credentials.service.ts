import { Injectable } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { HttpClient } from '@angular/common/http';
import { Config } from '../config/config';

@Injectable({
  providedIn: 'root'
})
/**
 * Service for managing credentials
 */
export class CredentialsService {

  constructor(private http : HttpClient, private _authService: AuthService, private _config : Config) { }

  public getCredentials()
  {
    return this.http.get(this._config.spUrl + '/credentials');
  }

  public deleteCredential(id : string) {
    return this.http.delete(this._config.spUrl + '/credentials/' + id)
  }
}
