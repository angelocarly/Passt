package com.realdolmen.passt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.realdolmen.passt.domain.User;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * Whenever the authorization server generates a new token, this enhancer will
 * add any extra details
 *
 * @author Angelo Carly
 */
public class PasstTokenEnhancer implements TokenEnhancer
{

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private GeoLocationService locationService;

    Logger logger = LoggerFactory.getLogger(PasstTokenEnhancer.class);

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
            OAuth2Authentication authentication)
    {

        //Get claim values
        User user = (User) authentication.getUserAuthentication().getPrincipal();

        //Add custom claims
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("sub", user.getId().toString());
        additionalInfo.put("name", user.getUsername());

        //If the token has a jwt refresh token, get the info from there
        try
        {
            DecodedJWT token = JWT.decode(accessToken.getRefreshToken().getValue());
            additionalInfo.put("loc", token.getClaim("loc").asString());
            additionalInfo.put("user_agent", token.getClaim("user_agent").asString());
        }
        catch(JWTDecodeException e)
        {
            //Else get the info from the request
            additionalInfo.put("loc", getLocationInfo(authentication));
            additionalInfo.put("user_agent", request.getHeader("User-Agent"));
        }

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        return accessToken;
    }

    /**
     * @param authentication
     * @return A formatted string with the IP and city of the request
     */
    private String getLocationInfo(OAuth2Authentication authentication)
    {
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getUserAuthentication().getDetails();

        if (details == null)
        {
            return "";
        }

        StringBuilder location = new StringBuilder();
        location.append(details.getRemoteAddress()).append(" ");
        try
        {
            location.append(locationService.fetchCity(request.getRemoteHost()));
        } catch (AddressNotFoundException e)
        {
            location.append("unknown");
        } catch (IOException ex)
        {
            java.util.logging.Logger.getLogger(PasstTokenEnhancer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (GeoIp2Exception ex)
        {
            java.util.logging.Logger.getLogger(PasstTokenEnhancer.class.getName()).log(Level.SEVERE, null, ex);
            location.append("unknown");
        }
        return location.toString();
    }

}
