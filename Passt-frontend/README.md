# PasstFrontend

This application is a fictional third party application that acts as an OAauth2 Client.

The application itself is a password manager named Passt.
It has the simple functionality of managing a list of passwords.

Since we can't save secrets in a SPA, we have to add a backend that will act as the actual client.
This means that any OAuth2 request we send, must pass through our backend. 

## Running the application

It is advised that you install the localhost certificates.
This is done by adding /ssl/root.crt and /ssl/server.crt to your certificates

Running the frontend:
```
cd ./passt-spa-frontend/
npm start
```
You can access the frontend on `https://localhost:4200/`.

Running the backend:
```
cd ./passt-spa-backend/
nodemon
```
You can access the backend on `https://localhost:3000`.
You can also reach the backend via a proxy on the frontend on `https://localhost:4200/api/`.

## Backend
The Passt-Backend is a third party application that acts as OAuth2 Client.
It is responsible for any OAuth2 request towards the IDP.
These are:
  * Redirecting users to the idp for login
  * Refreshing access tokens
  * Revoking refresh tokens
  * Acting as OAuth2 callback

### Configuring the backend
The configuration for the backend is stored in a JSON in `/config/config.js`.
This JSON contains any client settings that you can edit.
```json
{
    oauth: {
        clientId: "client_id",
        clientSecret: "keepmesecret",
        redirectUri: "https://localhost:4200/api/callback", //callback URL
        authServer: "https://localhost:8443", //IDP URL
        scopes: ["read", "write"]
    },
    frontendUri: "https://localhost:4200" //Frontend URL for redirection after authorization
}
```