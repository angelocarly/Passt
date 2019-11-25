# Security features

## Table of contents
1. [Features](#features)
    * [OAuth 2.0](#oauth-2.0)
    * [JWT](#jwt)
    * [JWS](#jws)
    * [SSL](#ssl)
    * [2FA](#2fa)
    * [Refresh token revocation](#refresh-token-revocation)
2. [Possible enhancements](#possible-enhancements)
    * [PKCE](#pkce)
    * [JWE](#jwe)
    * [MFA](#mfa)
    * [Client configuration](#client-configuration)
    * [Location verification](#location-verification)

## Features

### OAuth 2.0 <a>
[OAuth 2.0](https://oauth.net/2/) is the industry standard for authentication. It allows third-party applications to be granted limited access to a HTTP service, either on behalf of a resource owner or by allowing the third-party application to obtain access on its own behalf. [1]

OAuth 2.0 knows 4 roles:

  * Resource Owner: This is generally yourself   
  * Resource Server: A server hosting protected data  
  * Client: Application requesting access to a resource server  
  * Authorization Server: Server issuing access tokens to a client  

This application mainly acts as an Authorization Server. It provides tokens for other services. This application also has a Resource Server and a client used for token revocation.

[1]: http://www.bubblecode.net/en/2016/01/22/understanding-oauth2/ "Bubblecode article"

### JWT
[JSON Web Tokens (JWT)](https://auth0.com/learn/json-web-tokens/) is an [open standard](https://tools.ietf.org/html/rfc7519) for securily transmitting information between parties as a JSON object. This information can be verified and trusted because it is digitally signed.

JWTs were chosen for this project because they are self-contained, which means that the token itself contains information about the subject. Because these can be signed it means that the resource server can verify if a token is issued by the IDP and is legitimate. These properties make a good choice for a stateless authentication mechanism.

A caveat of not needing to verify the tokens at the IDP is that we are not able to quickly revoke them. This is because access to the Resource Server doesn't need any interaction with the IDP.  
To tackle this, the ability to revoke refresh tokens is added. This is described in the section [refresh token revocation](#refresh-token-revocation)

### JWS

[JSON Web Signature (JWS)](https://en.wikipedia.org/wiki/JSON_Web_Signature) is a standard for signing arbitrary data.

### SSL
Secure Sockets Layer (SSL) is a way to ensure an encrypted link between a web server and a browser. It ensures that all data passed between the web server and browsers remain private and integral.

### 2FA
Two-Factor Authentication is an extra step for identifying a user. It enables for better security because you need the phone you registered with instead of only a password.

The implementation in this project uses soft-tokens which are randomized based on the timestamp. When a user wants to log in he is asked to provide hist soft-token to be authenticated.

In this project you can enable 2FA when registering a new user. After succesful registration, the user will be taken to a screen where he can scan his code using a MFA app.

### Geolocation
Any issued refresh token gets an added claim named 'loc' in which the ip and location of the subject is stored. This makes it easier to differentiate tokens when a user wants to revoke them.

### Refresh token revocation
Since JWTs are signed and only need to be verified by the resource server and not the IDP, we don't have a way to effectively revoke them. The only time we can check if a token is valid, is when a new access token is requested at the IDP.  

This is done by storing the valid refresh tokens their id (jti) in a whitelist when they are created, and removing them when they are revoked. Then any time a new access token is requested we verify the token.   

## Possible enhancements

### PKCE
[Proof Key for Code Exchange (PKCE)](https://tools.ietf.org/html/rfc7636) is a security measure that can be taken when using public clients in an OAuth 2.0 Authentication Code flow. With PKCE, the clients are no longer susceptible to authorization code interception attacks.  

PKCE could not be added to this project because it has not yet been added to spring-security-oauth. (09/05/2019)

### JWE
[JSON Web Encryption (JWE)](https://tools.ietf.org/html/rfc7516) is the encryption of our JWT tokens. This gives the benefit that only an authorized server can tell what is written in the tokens.

This was not added to this project because of time limitation.

### MFA
With 2FA we have the option to verify the users using soft-tokens. However, more ways to authenticate are possible such as SMS messages, gps, biometrics.

### Client configuration
Currently the client configuration is hardcoded in memory. A proper implementation should have a way to register any new clients via the IDP itself.

### Location verification
Currently the location of a user is stored in the refresh token, but nothing is done to prohibit anyone from logging in from another location.

It would be useful to add a check that the user is coming from the same location and if not the case, make him log in again.