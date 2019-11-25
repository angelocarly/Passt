import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { RouterModule } from '@angular/router';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { PasswordoverviewComponent } from './passwordoverview/passwordoverview.component';
import { AuthService } from './auth/auth.service';
import { FormsModule } from '@angular/forms';
import { AuthGuardService } from './auth/auth-guard.service';
import { AuthInterceptor } from './auth/auth-interceptor';
import { Config } from './config/config';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    PasswordoverviewComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    RouterModule.forRoot([
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { path: 'home', component: HomeComponent }],
      { onSameUrlNavigation: 'reload' }),
    FormsModule
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi: true
  },
    Config
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
