# Passt-idp

## Table of contents
1. [About](#about)
2. [Installation](#installation)
3. [Configuration](#configuration)

## About
An Identity Provider (IDP) is a centralized system for user authentication/authorization. It provides user information to other services.

### Used libraries
  * Spring-boot
  * Spring-security
  * Spring-security-oauth
  * Hibernate
  * Geoip2
  * Aerogear-otp-java
  * zxing

## Installation

To get a running application you need to do the following steps:
  - Install the localhost certificate /src/target/conf/ssl/server.p12
  - Import the certificate in the java cacerts using the following command: `keytool -import -alias ca -file cert.crt -keystore cacerts -storepass changeit`
  - Run passt-idp, by default the Identity Provider will run on https://localhost:8443  

### Installing certificates
You have the option to install the certificates provided in the repo, or you can generate them yourself.  

To generate a keystore yourself you can use the following command:  
`keytool -genkeypair -alias passtssl -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore passtsst.p12 -validity 3650`

### OAuth 2.0 Authorization code flow examples
Here is described how you can perform the authorization code flow with curl

1. Login  
`curl --cookie-jar cookie.txt -d "username=angeloc&password=angelo123" -kvX POST https://localhost:8443/login`

2. Get Authorization code
`curl -b cookies.txt -kv 'https://localhost:8443/oauth/authorize?client_id=token-client&redirect_uri=https://localhost:8443/connect/passt&response_type=code&state=5RR4kz'
`  
Response:  
```
{ [5 bytes data]  
< HTTP/1.1 302  
< Access-Control-Allow-Origin: * 
< Access-Control-Allow-Methods:  PATCH,POST,GET,OPTIONS,DELETE  
< Access-Control-Max-Age: 3600  
< Access-Control-Allow-Headers: x-requested-with,  authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN
< Cache-Control: no-store
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 1; mode=block
< Strict-Transport-Security: max-age=31536000 ; includeSubDomains
< X-Frame-Options: SAMEORIGIN
< Location: https://localhost:8443/connect/passt?code=w20cJL&state=5RR4kz
< Content-Language: en-US
< Content-Length: 0
< Date: Thu, 16 May 2019 08:54:57 GMT
```

Here we get take the authorization code from the redirect url. w20cJL

3. Get tokens
Next we post the authorization code to /oauth/token and we receive our tokens.
`curl -b cookies.txt -d 'grant_type=authorization_code&client_id=token-client&client_secret=secret&code=ezcAWZ&redirect_uri=https://localhost:8443/connect/passt' -kv 'https://localhost:8443/oauth/token'
`
Response:
```{ [1735 bytes data] 100  1985    0  1850  100   135   4933    360 --:--:-- --:--:-- --:--:--  5293
{
	"access_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI4YTkxOWFjMS02OWUzLWJjYTEtMDE2OS1lM2JjY2I0NTAwMDAiLCJsb2MiOiIwOjA6MDowOjA6MDowOjEgdW5rbm93biIsInVzZXJfbmFtZSI6ImFuZ2Vsb2MiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwibmFtZSI6ImFuZ2Vsb2MiLCJleHAiOjE1NTgwMDAxMzIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXSwianRpIjoiMGYxNDYwMzItODRmZi00OTI5LWFjYWQtMDVjNTg2Mzg1ZjBmIiwidXNlcl9hZ2VudCI6ImN1cmwvNy42My4wIiwiY2xpZW50X2lkIjoidG9rZW4tY2xpZW50In0.Hd3Etq-QKf4Ae2kY5-qSp_lqKVg8t4CyhWSXC5lA-JizVQ9rWndV9d3jw7hIfjlIrGgvdj4xacUq6Wl9CsF715aTGD2ZUqsUkyubBfaUcjFWTwbi14F12BIRPs_OH7WLdAyjd1JckFjjCMEPOFv5FGHnrNzfdPA10Xd-5m3uHW2nfbKQuMZ1jfOZ_FMCUwW8DuEOu8HMP0Ho-OXhRwkf7XwL4uWMwj6n6w__WAWQ8z30VZFP7oVyESYtMUiEHbmT3c612ISQfiaPalWN4vLgr79ha75RlnlaRwMJ_Hgm8QThr-TdDxN3W6hqgo7cTDZQRSvPkLbDkWp-9S2FQ-hvWw",
	"token_type":"bearer",
	"refresh_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI4YTkxOWFjMS02OWUzLWJjYTEtMDE2OS1lM2JjY2I0NTAwMDAiLCJsb2MiOiIwOjA6MDowOjA6MDowOjEgdW5rbm93biIsInVzZXJfbmFtZSI6ImFuZ2Vsb2MiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiYXRpIjoiMGYxNDYwMzItODRmZi00OTI5LWFjYWQtMDVjNTg2Mzg1ZjBmIiwibmFtZSI6ImFuZ2Vsb2MiLCJleHAiOjE1NTgwMDA0MzIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXSwianRpIjoiM2RiMzhkYjAtNTUzMC00MDE3LWI1ZGItZGViZDIzODNkNTJjIiwidXNlcl9hZ2VudCI6ImN1cmwvNy42My4wIiwiY2xpZW50X2lkIjoidG9rZW4tY2xpZW50In0.EqNegIJv2KTI2rvzO1dgHlBzVBJAGQpkVcQWCxO-FxqxFCTdm4ddVEnyVHoM1OO3NRqD_Q5ZNfnICbKjX1mlBBZPI2Oe0xlTaxdCaBDy05XIgfjf55WOXKtcQKdWX-fr57_y4CsKiWQPJw86VQQEkYV_Jtd5vsLt6FYLN2EDGfci0lsOncHGYQBvEeOuN-Fd_OqZ29_IJwETTIwO9cD8oa160I_hHvDfue95JAangIQxiAkkWA1BKuBawJrLokolYCfc4DQ1HugXg6IWYIN5KtaYkkO01kXyqNMxBV1ss4Tdw_Kqda_O4IINtZIXso_UebEJZ9kg3w9GXYIUarSfQg",
	"expires_in":299,
	"scope":"read write",
	"sub":"8a919ac1-69e3-bca1-0169-e3bccb450000",
	"loc":"0:0:0:0:0:0:0:1 unknown",
	"name":"angeloc",
	"user_agent":"curl/7.63.0",
	"jti":"0f146032-84ff-4929-acad-05c586385f0f"
}
```

## Configuration

### Specifying the address:port
The port that the server will run on can be configured by changing 'server.port' in application.properties.  
The address can be changed by editing passt.client.address in application.yml.

You are required to specify the address because the idp-client needs to correct url for a correct authorization-code flow.

### Adding clients
You can add clients yourself by configuring the ClientDetailsServiceConfigurer in /config/AuthorizationServerConfig, here is an example:
```java
clients
                .inMemory()
                .withClient("clientID")
                .secret(passwordEncoder.encode("secret"))
                .scopes("read", "write")
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .redirectUris("https://localhost:4200/api/callback");
```

## Features
Features can be found [here](SECURITY.md)