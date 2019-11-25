package com.realdolmen.passt.controller;

import com.realdolmen.passt.domain.JwtRefreshToken;
import com.realdolmen.passt.domain.User;
import com.realdolmen.passt.dto.JtiDto;
import com.realdolmen.passt.service.AccessTokenService;
import com.realdolmen.passt.service.TokenService;
import com.realdolmen.passt.service.UserService;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Angelo Carly
 * 
 * Configuration of the token-client Resource Controller
 * This class acts as the backend for the token-client
 * Any request from the token-client towards the IDP goes through here
 */
@RestController
public class ClientResourceController
{

    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private AccessTokenService accessTokenService;
    
    //Get user details
    @GetMapping("client/api/me")
    public Map<String, String> userDetails(Principal principal)
    {
        User user = userService.findByUsername(principal.getName());
        Map<String, String> map = new LinkedHashMap<>();
        map.put("userid", user.getId().toString());
        return map;
    }

    //Get refresh tokens list
    @GetMapping("client/api/tokens")
    public List<JwtRefreshToken> tokens()
    {
        UUID id = accessTokenService.getAccessToken().getUserId();
        return tokenService.getTokenList(id);
    }
    
    //Revoke a token by jti
    @PostMapping("client/api/revoke")
    public void revokeToken(@RequestBody @Valid JtiDto token)
    {
        UUID userId = accessTokenService.getAccessToken().getUserId();
        tokenService.revokeTokenByJtiAndUserId(userId, token.getJti());
    }
    
    //Revoke all tokens
    @PostMapping("client/api/revokeall")
    public void revokeAll()
    {
        UUID userId = accessTokenService.getAccessToken().getUserId();
        tokenService.revokeAll(userId);
    }

}
