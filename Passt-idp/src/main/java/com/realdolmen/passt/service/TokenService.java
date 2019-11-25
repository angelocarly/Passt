package com.realdolmen.passt.service;

import com.realdolmen.passt.domain.JwtRefreshToken;
import java.util.List;
import java.util.UUID;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 *
 * @author Angelo Carly
 */
public interface TokenService
{
    public void revokeToken(JwtRefreshToken token);
    public void revokeTokenByJtiAndUserId(UUID userId, UUID jti);
    public void revokeAll(UUID userId);
    public void revokeTokenByAccessToken(OAuth2AccessToken oaat);
    
    public List<JwtRefreshToken> getTokenList(JwtRefreshToken token);
    public List<JwtRefreshToken> getTokenList(UUID userId);
}
