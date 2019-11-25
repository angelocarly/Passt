package com.realdolmen.passt.controller;

import com.realdolmen.passt.dto.TokenDto;
import com.realdolmen.passt.dto.JwtRefreshTokenDto;
import com.realdolmen.passt.domain.JwtRefreshToken;
import com.realdolmen.passt.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author Angelo Carly
 *
 * Controller for token revocation and access
 */
@RestController
@SpringBootApplication
public class TokenController
{

    @Autowired
    private TokenService tokenService;
    
    Logger logger = LoggerFactory.getLogger(TokenController.class);
    
    @PostMapping("/api/check_token")
    public void checkToken(HttpServletRequest request, @RequestBody @Valid TokenDto tokenDto)
    {

    }

    //Gives a list of all tokens of the specified user
    @PostMapping("/api/tokens")
    public List<JwtRefreshToken> getTokens(HttpServletRequest request, @RequestBody @Valid TokenDto tokenDto)
    {

        JwtRefreshTokenDto token = tokenDto.toJWTToken();

        return tokenService.getTokenList(convertToEntity(token));
    }

    //Revokes a specified token
    @PostMapping("/api/revoke")
    public void revokeToken(HttpServletRequest request, @RequestBody @Valid TokenDto tokenDto)
    {

        JwtRefreshTokenDto token = tokenDto.toJWTToken();

        tokenService.revokeToken(convertToEntity(token));

    }
    
    private JwtRefreshToken convertToEntity(JwtRefreshTokenDto dto)
    {
        JwtRefreshToken token = new JwtRefreshToken();
        token.setClientId(dto.getClientId());
        token.setJti(dto.getJti());
        token.setUserId(dto.getUserId());
        return token;
    }

}
