package com.realdolmen.passt.dto;

import com.auth0.jwt.JWT;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.realdolmen.passt.controller.validator.NotRevokedRefreshToken;
import com.realdolmen.passt.controller.validator.RefreshToken;

/**
 *
 * @author Angelo Carly
 */
public class TokenDto
{

    @NotNull(message = "Token can't be null")
    @NotEmpty(message = "Token can't be empty")
    @RefreshToken
    @NotRevokedRefreshToken
    private String token;

    public String getToken()
    {
        return token;
    }
    

    public void setToken(String token)
    {
        this.token = token;
    }
    
    public JwtRefreshTokenDto toJWTToken()
    {
        return new JwtRefreshTokenDto(JWT.decode(this.token));
    }
    
}
