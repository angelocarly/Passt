package com.realdolmen.passt.persistence;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.realdolmen.passt.domain.JwtRefreshToken;
import com.realdolmen.passt.persistence.RefreshTokenRepository;
import com.realdolmen.passt.service.RefreshTokenService;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


/**
 * @author Angelo Carly
 * 
 * Custom implementation of tokenstore, mimics JwtTokenStore but with persistence
 * Stores any saved refresh token in database and verifies any refresh token by
 * checking if they are stored
 */
public class JwtJdbcTokenStore implements TokenStore
{

    Logger logger = LoggerFactory.getLogger(JwtJdbcTokenStore.class);

    private final JwtTokenStore store;

    private final RefreshTokenService refreshTokenService;

    public JwtJdbcTokenStore(JwtAccessTokenConverter jwtTokenEnhancer, RefreshTokenService refreshTokenService)
    {
        this.store = new JwtTokenStore(jwtTokenEnhancer);
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken oaat)
    {
        return this.store.readAuthentication(oaat);
    }

    @Override
    public OAuth2Authentication readAuthentication(String token)
    {
        return this.store.readAuthentication(token);
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken oaat, OAuth2Authentication oaa)
    {
        this.store.storeAccessToken(oaat, oaa);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String token)
    {
        return this.store.readAccessToken(token);
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken oaat)
    {
        this.store.removeAccessToken(oaat);
    }

    //Save a new refresh token in the database
    @Override
    public void storeRefreshToken(OAuth2RefreshToken oart, OAuth2Authentication oaa)
    {
        DecodedJWT token = JWT.decode(oart.getValue());
        JwtRefreshToken crt = new JwtRefreshToken();

        crt.setJti(UUID.fromString(token.getClaim("jti").asString()));
        crt.setUserId(UUID.fromString(token.getClaim("sub").asString()));
        crt.setClientId(token.getClaim("client_id").asString());
        crt.setAti(UUID.fromString(token.getClaim("ati").asString()));
        crt.setUserAgent(token.getClaim("user_agent").asString());
        crt.setUsername(token.getClaim("user_name").asString());
        crt.setLocation(token.getClaim("loc").asString());

        refreshTokenService.saveToken(crt);
        this.store.storeRefreshToken(oart, oaa);
    }

    //Verifies that the refresh token is valid and is stored in database
    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue)
    {
        OAuth2RefreshToken refreshToken = this.store.readRefreshToken(tokenValue);

        if (refreshToken != null)
        {
            DecodedJWT token = JWT.decode(tokenValue);
            Optional<JwtRefreshToken> j = refreshTokenService.findByJtiAndClientId(
                    UUID.fromString(token.getClaim("jti").asString()),
                    token.getClaim("client_id").asString());

            if (j.isPresent())
            {
                return refreshToken;
            } else
            {
                throw new InvalidTokenException("Invalid refresh token");
            }
        }
        return null;
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken oart)
    {
        return this.store.readAuthenticationForRefreshToken(oart);
    }

    //Remove the refresh token from the database
    @Override
    public void removeRefreshToken(OAuth2RefreshToken oart)
    {
        DecodedJWT token = JWT.decode(oart.getValue());
        Optional<JwtRefreshToken> j = refreshTokenService.findByJtiAndClientId(UUID.fromString(token.getClaim("jti").asString()), token.getClaim("client_id").asString());

        if (j.isPresent())
        {
            refreshTokenService.deleteByJtiAndClientId(UUID.fromString(token.getClaim("jti").asString()), token.getClaim("client_id").asString());
        }

        this.store.removeRefreshToken(oart);
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken oart)
    {
        this.store.removeAccessTokenUsingRefreshToken(oart);
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication oaa)
    {
        return this.store.getAccessToken(oaa);
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userId)
    {
        return this.store.findTokensByClientIdAndUserName(clientId, userId);
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId)
    {
        return this.store.findTokensByClientId(clientId);
    }

}
