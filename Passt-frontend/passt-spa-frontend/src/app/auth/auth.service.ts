import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { Observable } from 'rxjs/internal/Observable';
import { throwError } from 'rxjs';
import { Config } from '../config/config';

@Injectable({
  providedIn: 'root'
})
/**
 * Service for general authorization
 * Login/logout/managing tokens
 */
export class AuthService {

  constructor(private _http: HttpClient, private _config : Config) { }

  /**
   * Refresh the current access token
   */
  refreshAccessToken() {
    var headers = new HttpHeaders({ 'Content-type': 'application/x-www-form-urlencoded; charset=utf-8' });
    var req = this._http.post(
      window.location.origin + '/api/token',
      new HttpParams().set('grant_type', 'refresh_token').set('refresh_token', this.getRefreshToken()),
      { headers: headers }
    ).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(error || 'Server error');
      }));

    req.subscribe(data => {
      localStorage.setItem('refresh_token', data['refresh_token']);
      localStorage.setItem('access_token', data['access_token']);
    })

    return req;
  }

  verifyRefreshToken() {
    return this._http.post(this._config.idpUrl + '/api/check_token',
      new HttpParams().set('token', this.getRefreshToken())
    );
  }

  /**
   * Logout the user
   * Removes the tokens in localstorage
   * Revokes the refresh token
   * Refreshes the page
   */
  logout() {
    //If only try to revoke a refresh token if we have access to one
    if (this.getRefreshToken() != null) {

      this._http.post(
        window.location.origin + '/api/revoke',
        {'token': this.getRefreshToken()}
      ).toPromise()
        .then(
          data => {
            window.location.reload();
          }
        ).catch(err => {
          window.location.reload();
        })
    }
    else {
      window.location.reload();
    }

    //Clear storage and refresh page
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    localStorage.removeItem('auth_error');
  }

  /**
   * Redirect to backend for OAuth authorization_code flow
   */
  login() {
    window.location.href = '/api/auth';
  }

  //Getters and setters
  checkCredentials() {
    return this.getAccessToken() != null;
  }

  isAuthenticated() {
    return this.getAccessToken() != null;
  }

  checkError() {
    return this.getError() != null;
  }

  getAccessToken() {
    return localStorage.getItem('access_token');
  }

  setAccessToken(access_token: string) {
    localStorage.setItem('access_token', access_token);
  }

  getRefreshToken() {
    return localStorage.getItem('refresh_token');
  }

  setRefreshToken(refresh_token: string) {
    localStorage.setItem('refresh_token', refresh_token);
  }

  getError() {
    return localStorage.getItem('auth_error');
  }

}

