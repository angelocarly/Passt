import { Injectable } from '@angular/core';

/**
 * Config class for configuring the resource server and IDP URL
 */
@Injectable() 
export class Config {
  readonly spUrl:string = 'https://localhost:8081';
  readonly idpUrl:string = 'https://localhost:8443';
}