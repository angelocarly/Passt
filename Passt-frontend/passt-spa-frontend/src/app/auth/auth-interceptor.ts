import { Injectable } from "@angular/core";
import { HttpEvent, HttpHandler, HttpRequest, HttpClient } from "@angular/common/http";
import { HttpInterceptor } from "@angular/common/http";
import { Observable, BehaviorSubject, throwError } from 'rxjs';
import { catchError, flatMap, filter, switchMap, take } from 'rxjs/operators';
import { AuthService } from './auth.service';

/**
 * Authorization interceptor
 * Checks if the access token is still valid and if not refreshes it.
 */
@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    private isRefreshing: boolean;
    private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

    constructor(public http: HttpClient, private _auth: AuthService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        //Add the access token
        req = this.addAuthenticationHeader(req)

        return next.handle(req).pipe(
            catchError(err => {

                //The request fails with 401 Access token expired
                if (err.status == 401) {
                    
                    if (err.error.error_description && err.error.error_description.includes('Access token expired')) {

                        //wait if new tokens are already being retrieved
                        if (this.isRefreshing) {

                            return this.refreshTokenSubject.pipe(
                                filter(result => result !== null),
                                take(1),
                                switchMap(() => next.handle(this.addAuthenticationHeader(req)))
                            );
                        }
                        else {

                            //Retrieve new tokens
                            return this.refreshToken(req, next)
                        }
                    }
                    else {

                        this._auth.logout();
                    }
                }

                //Forward the error
                return throwError(err);
            }));
    }

    refreshToken(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        //Notify other calls that we're refreshing
        this.isRefreshing = true;

        let params = {
            grant_type: 'refresh_token',
            refresh_token: this._auth.getRefreshToken()
        }

        //Post refresh token
        return this.http.post('/api/token', params).pipe(
            flatMap((data: any) => {

                if (data.status = 200) {

                    //Update tokens
                    this._auth.setAccessToken(data['access_token']);
                    this._auth.setRefreshToken(data['refresh_token']);

                    //Add new token to header
                    req = this.addAuthenticationHeader(req)

                    this.isRefreshing = false;

                    //Notify the waiting requests that we have a new token
                    this.refreshTokenSubject.next(this._auth.getAccessToken());

                    return next.handle(req);
                }
                else {

                    this.isRefreshing = false;

                    //Logout from account
                    this._auth.logout();
                }
            })
        );
    }

    addAuthenticationHeader(req: HttpRequest<any>) {
        let token = this._auth.getAccessToken();

        if (token) {
            req = req.clone({
                setHeaders: {
                    'Authorization': 'Bearer ' + token
                }
            });
        }

        return req;
    }
}