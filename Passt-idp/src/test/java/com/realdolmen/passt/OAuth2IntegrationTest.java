package com.realdolmen.passt;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Angelo Carly
 * 
 * Integration test on the OAuth2 Authorization Code Flow
 * This test does the following:
 *   - Login the user (/login)
 *   - Get the Authorization Code from the Authorize endpoint (/oauth/authorize)
 *   - Get the token information from the token endpoint (/oauth/token)
 *   - Verify that a token is retrieved
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = PasstApplication.class)
public class OAuth2IntegrationTest
{

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    Logger logger = LoggerFactory.getLogger(OAuth2IntegrationTest.class);

    @Before
    public void setup()
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void testOAuthAuthorizationCodeFlow() throws Exception
    {
        MockHttpSession session = login("angeloc", "angelo123");
        
        String authCode = authorize(
                "token-client",
                "https://localhost/connect/passt",
                "code",
                "5RR4kz",
                session);
        
        String response = tokens(authCode);
        
        //Verify response
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        assertTrue(jsonObject.has("access_token"));
        assertTrue(jsonObject.has("refresh_token"));
    }

    private MockHttpSession login(String username, String password) throws Exception
    {
        //Post the username and password to the /login endpoint and retrieve a session
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        params.add("password", password);

        MvcResult loginResult
                = mockMvc.perform(post("https://localhost/login")
                        .params(params))
                        .andExpect(status().is3xxRedirection())
                        .andReturn();
        return (MockHttpSession) loginResult.getRequest().getSession();

    }
    
    private String authorize(String clientId, String redirectUri, String responseType, String state, MockHttpSession session) throws Exception
    {
        //Post to authorization url
        MvcResult authorizationResult
                = mockMvc.perform(get(String.format(
                        "https://localhost/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=%s&state=%s",
                        clientId,
                        redirectUri,
                        responseType,
                        state))
                        .session(session))
                        .andExpect(status().is3xxRedirection())
                        .andReturn();

        //Parse auth code
        String code = "";
        List<NameValuePair> resParams = URLEncodedUtils.parse(new URI(authorizationResult.getResponse().getRedirectedUrl()), Charset.forName("UTF-8"));
        for(NameValuePair n : resParams)
        {
            if(n.getName().equals("code")) code = n.getValue();
        }
        
        return code;
    }
    
    private String tokens(String authCode) throws Exception
    {
        //Post the authorization code to the /oauth/tokens endpoint and retrieve the tokens
        MvcResult authCodeResult
                = mockMvc.perform(post("https://localhost/oauth/token")
                        .header("content-type", "application/x-www-form-urlencoded")
                        .content(String.format("grant_type=authorization_code&client_id=token-client&client_secret=secret&code=%s&redirect_uri=https://localhost/connect/passt", authCode)))
                        .andExpect(status().isOk())
                        .andReturn();
        
        return authCodeResult.getResponse().getContentAsString();
    }

}
