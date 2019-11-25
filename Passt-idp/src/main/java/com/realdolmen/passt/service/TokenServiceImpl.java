package com.realdolmen.passt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.realdolmen.passt.dto.JwtRefreshTokenDto;
import com.realdolmen.passt.domain.JwtRefreshToken;
import com.realdolmen.passt.persistence.RefreshTokenRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author Angelo Carly
 */
@Service
public class TokenServiceImpl implements TokenService
{

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Override
    public void revokeToken(JwtRefreshToken token)
    {
        refreshTokenRepository.deleteByJtiAndClientId(token.getJti(), token.getClientId());
    }

    @Override
    public List<JwtRefreshToken> getTokenList(JwtRefreshToken token)
    {
        return refreshTokenRepository.findByUserIdAndClientId(token.getUserId(), token.getClientId());
    }

    @Override
    public List<JwtRefreshToken> getTokenList(UUID userId)
    {
        return refreshTokenRepository.findByUserId(userId);
    }

    @Override
    public void revokeTokenByJtiAndUserId(UUID userId, UUID jti)
    {
        this.refreshTokenRepository.deleteByJtiAndUserId(jti, userId);
    }

    @Override
    public void revokeTokenByAccessToken(OAuth2AccessToken oaat)
    {
        //Remove the refresh token associated with the access token
        DecodedJWT token = JWT.decode(oaat.getValue());
        UUID jti = UUID.fromString(token.getClaim("jti").asString());
        String clientId = token.getClaim("client_id").asString();
        
        refreshTokenRepository.deleteByAtiAndClientId(
                jti, //Use the access-token's jti as the refresh token's ati
                clientId);
    }

    @Override
    public void revokeAll(UUID userId)
    {
        refreshTokenRepository.deleteByUserId(userId);
    }

}
