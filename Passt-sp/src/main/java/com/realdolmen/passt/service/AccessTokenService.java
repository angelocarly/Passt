package com.realdolmen.passt.service;

import com.realdolmen.passt.domain.JwtAccessToken;
import com.realdolmen.passt.exception.InvalidJWTTokenException;
import java.util.Map;
import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;

/**
 *
 * @author Angelo Carly
 */
@Service
public class AccessTokenService
{

    public JwtAccessToken getAccessToken()
    {
        //Read user's authentication
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        Object details = authentication.getDetails();

        if (details instanceof OAuth2AuthenticationDetails)
        {
            //Get the token details
            OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) details;

            Map<String, Object> decodedDetails = (Map<String, Object>) oAuth2AuthenticationDetails.getDecodedDetails();

            JwtAccessToken token = new JwtAccessToken();
            try
            {
                token.setId(null);
                token.setJti(UUID.fromString(decodedDetails.get("jti").toString()));
                token.setUserId(UUID.fromString(decodedDetails.get("sub").toString()));
                token.setClientId(decodedDetails.get("client_id").toString());
                token.setUsername(decodedDetails.get("user_name").toString());
                token.setUserAgent(decodedDetails.get("user_agent").toString());
            } catch (NullPointerException e)
            {
                throw new InvalidJWTTokenException();
            }

            return token;
        }

        //Invalid details
        throw new IllegalArgumentException("Invalid authentication details");
    }

}
