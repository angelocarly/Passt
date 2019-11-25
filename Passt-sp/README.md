# Passt-sp

This project is the Resource Server for the passt application. It provides the data for the Passt Client (passwords) and is accessed using a token issued by the IDP.

## Installation

Install the certificate at `/src/main/resources/keystore/server.p12`
Run passt-sp
By default, the Resource Server will run on `https://localhost:8081`

## Features

### Credential management
This project exposes an API to manage a user's credentials
Any request is required to have an Authorization Bearer token with a valid token.

|Method|URI|Parameters|Returns|
|---|---|---|---|
|GET|/credentials||List of credentials|
|GET|/credentials/{id}|id: UUID of the requested credential|Credential|
|POST|/credentials|Credential|/|
|DELETE|/credentials/{id}|id: UUID of the requested credential|/|

### SSL
This project is secured using SSL certificates.

### JWS
Any token is signed assymetrically and is certain to be issued by the IDP.