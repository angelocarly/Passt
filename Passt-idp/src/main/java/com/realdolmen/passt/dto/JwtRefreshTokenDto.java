package com.realdolmen.passt.dto;

import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author Angelo Carly
 */
public class JwtRefreshTokenDto implements Serializable
{
    private UUID jti;
    private UUID userId;
    private String clientId;

    public JwtRefreshTokenDto() {}
    
    public JwtRefreshTokenDto(DecodedJWT token)
    {
        this.jti = UUID.fromString(token.getClaim("jti").asString());
        this.userId = UUID.fromString(token.getClaim("sub").asString());
        this.clientId = token.getClaim("client_id").asString();
    }

    public UUID getUserId()
    {
        return userId;
    }

    public void setUserId(UUID userId)
    {
        this.userId = userId;
    }

    public String getClientId()
    {
        return clientId;
    }

    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    public UUID getJti()
    {
        return jti;
    }

    public void setJti(UUID jti)
    {
        this.jti = jti;
    }

}