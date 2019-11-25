import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { ReactiveFormsModule } from '@angular/forms';
import { CredentialsService } from './credentials.service';

@Component({
  selector: 'app-passwordoverview',
  templateUrl: './passwordoverview.component.html',
  styleUrls: ['./passwordoverview.component.css']
})
export class PasswordoverviewComponent implements OnInit {

  private isLoggedIn = this._authService.isAuthenticated();
  private credentials: object[];
  public editing: boolean[];
  public editCredentials: object[];

  constructor(private _credentialsService: CredentialsService, private _authService: AuthService) { }

  ngOnInit() {

    //Get and store the credentials
    if (this.isLoggedIn) {

      this._credentialsService.getCredentials().subscribe(
        (data: object[]) => {
          this.credentials = data;
          this.editing = Array<boolean>(this.credentials.length);
          this.editCredentials = Array<object>(this.credentials.length);
        },
        err => { console.log(err) }
      );
    }
  }

  //Delete credential
  remove(credential) {

    this._credentialsService.deleteCredential(credential.id).subscribe(
      data => {
        var index = this.credentials.indexOf(credential)
        this.credentials.splice(index, 1);
      },
      err => {  }
    );
  }

  //Enable editing of credential with id i
  edit(i) {
    this.editing[i] = true;
    this.editCredentials[i] = Object.assign({}, this.credentials[i]);
  }

  //Cancel editing of credential with id 1
  cancelEdit(i) {
    this.editing[i] = false;
    this.editCredentials[i] = null;
  }

  //Save editing of credential with id 1
  //TODO put to resource server
  saveEdit(i) {
    this.editing[i] = false;
    this.credentials[i] = Object.assign({}, this.editCredentials[i]);
    this.editCredentials[i] = null;
  }
}
