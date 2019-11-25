package com.realdolmen.passt.service;

import java.util.Map;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * Maps the jwt claims to the principal
 * @author Angelo Carly
 */
public class JwtConverter extends JwtAccessTokenConverter
{

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map)
    {
        OAuth2Authentication auth = super.extractAuthentication(map);
        auth.setDetails(map); //this will get spring to copy JWT content into Authentication
        return auth;
    }
}
