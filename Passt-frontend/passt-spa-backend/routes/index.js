var express = require('express');
var router = express.Router();
var config = require('../config/config');
var ClientOAuth2 = require('client-oauth2')
const url = require('url');
const request = require('request');

/**
 * Client configuration using ../config/config
 */
var passtAuth = new ClientOAuth2({
  clientId: config.oauth.clientId,
  clientSecret: config.oauth.clientSecret,
  accessTokenUri: config.oauth.authServer + '/oauth/token',
  authorizationUri: config.oauth.authServer + '/oauth/authorize',
  redirectUri: config.oauth.redirectUri,
  scopes: config.oauth.scopes
})

/** 
 * /auth endpoint
 * redirects user to idp /oauth/authorize page
*/
router.get('/auth', function (req, res) {

  var uri = passtAuth.code.getUri()

  res.redirect(uri)

})

/**
 * /Token endpoint
 * Returns new tokens using the refresh token
 */
router.post('/token', function (req, res) {

  var options = { headers : {
    'user-agent':req.headers["user-agent"]
  }}

  //Create a token object with the issued refresh_token
  var token = passtAuth.createToken({
    refresh_token: req.body.refresh_token
  })

  //Refresh the token
  token.refresh(options)
    .then((newToken) => {
      res.send(newToken.data);
    })
    .catch((err) => {
      res.status(401)
      if (err.body) {
        res.send(err.body);
      }
      else {
        res.send({ "error": err.message });
      }
    });

})

//Revoke the token
router.post('/revoke', function (req, res) {
console.log(config.oauth.authServer + '/api/revoke')
  request.post(config.oauth.authServer + '/api/revoke', {
    json: {
      'token': req.body.token
    }
  }, (error, res2, body) => {
    if (error) {
      console.error(error)
      res.send(error);
      return
    }
    console.log(body);
    res.statusCode = res2.statusCode;
    res.send(body)
  });

})

/**
 * /callback endpoint
 * Returns page that saves the access_token and refresh_token
 * Then redirects to frontend
 */
router.get('/callback', function (req, res) {

  var options = { headers : {
    'user-agent':req.headers["user-agent"]
  }}

  passtAuth.code.getToken(req.originalUrl, options)
    .then(function (user) {

      // Refresh the current user's access token.
      user.refresh(options);

      //Render redirect page with tokens.
      res.render('redirect', {
        access_token: user.data.access_token,
        refresh_token: user.data.refresh_token,
        error: '',
        frontendUri: config.frontendUri
      });
    }).catch((error) => {
      console.error(error)
      //Render redirect page and save the auth error in localstorage
      res.render('errorredirect', {
        auth_error: JSON.stringify(error.body),
        frontendUri: config.frontendUri
      });
    })

})

module.exports = router;
