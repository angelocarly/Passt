import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router/src/utils/preactivation';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';

/**
 * Intercepts if a user is not allowed to go to a URL unauthorized
 * redirects him to home
 */
@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {
  path: import("@angular/router").ActivatedRouteSnapshot[];
  route: import("@angular/router").ActivatedRouteSnapshot;

  constructor(public auth: AuthService, public router: Router) { }

  canActivate(): boolean {

    //Redirect to the home page when not authenticated
    if (!this.auth.isAuthenticated()) {

      this.router.navigate(['home']);
      return false;
    }

    return true;
  }
}
