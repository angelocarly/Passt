package com.realdolmen.passt.mock;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockJwtUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockJwtUser>
{

    @Override
    public SecurityContext createSecurityContext(WithMockJwtUser customUser)
    {
        //Inject custom authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(getOauthTestAuthentication(customUser));
        
        return context;
    }

    //Returns a custom authentication object
    private Authentication getOauthTestAuthentication(WithMockJwtUser customUser)
    {
        //Create a user
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("Everything");
        com.realdolmen.passt.domain.User userPrincipal = new com.realdolmen.passt.domain.User("user", "", true, true, true, true, authorities);

        //Create a token
        TestingAuthenticationToken token = new TestingAuthenticationToken(userPrincipal, null, authorities);
        token.setAuthenticated(true);
        
        //Create authentication
        OAuth2Authentication auth = new OAuth2Authentication(getOauth2Request(), token);
        
        //Set details
        MockHttpServletRequest request = new MockHttpServletRequest();
        OAuth2AuthenticationDetails odetails = new OAuth2AuthenticationDetails(request);
        HashMap<String, String> details = createDetails(customUser);
        odetails.setDecodedDetails(details);
        auth.setDetails(odetails);
        
        return auth;
    }
    
    private HashMap<String, String> createDetails(WithMockJwtUser user)
    {
        HashMap<String, String> details = new HashMap<>();
        details.put("jti", UUID.randomUUID().toString());
        details.put("user_name", user.username());
        details.put("sub", user.subject());
        details.put("client_id", "test");
        details.put("user_agent", "java_test");
        return details;
    }

    //Default request details
    private OAuth2Request getOauth2Request()
    {
        String clientId = "oauth-client-id";
        Map<String, String> requestParameters = Collections.emptyMap();
        boolean approved = true;
        String redirectUrl = "http://my-redirect-url.com";
        Set<String> responseTypes = Collections.emptySet();
        Set<String> scopes = Collections.emptySet();
        Set<String> resourceIds = Collections.emptySet();
        Map<String, Serializable> extensionProperties = Collections.emptyMap();
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("Everything");

        OAuth2Request oAuth2Request = new OAuth2Request(requestParameters, clientId, authorities,
                approved, scopes, resourceIds, redirectUrl, responseTypes, extensionProperties);

        return oAuth2Request;
    }

}
