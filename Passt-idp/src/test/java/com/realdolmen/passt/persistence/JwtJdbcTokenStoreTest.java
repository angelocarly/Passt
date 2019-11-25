/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realdolmen.passt.persistence;

import com.realdolmen.passt.domain.JwtRefreshToken;
import com.realdolmen.passt.service.MockRefreshTokenService;
import io.jsonwebtoken.Jwts;
import java.security.InvalidKeyException;
import java.util.Date;
import java.util.UUID;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 *
 * @author Angelo Carly
 */
public class JwtJdbcTokenStoreTest
{

    private MockRefreshTokenService tokenService;
    private JwtJdbcTokenStore tokenStore;

    private String correctToken;
    private Logger logger = LoggerFactory.getLogger(JwtJdbcTokenStoreTest.class);
    
    private final SignatureVerifier emptyVerifier = new SignatureVerifier()
    {
        @Override
        public void verify(byte[] bytes, byte[] bytes1)
        {
        }

        @Override
        public String algorithm()
        {
            return "none";
        }
    };

    private static final String SUB = "8a919ac1-69e3-bca1-0169-e3bccb450000";
    private static final String LOC = "10.0.0.0 Unknown";
    private static final String USER_NAME = "angeloc";
    private static final String JTI = "d683602d-98dd-4cf2-9afa-a43e9c626949";
    private static final String CLIENT_ID = "first_client";
    private static final String ATI = "e772591e-98dd-4cf2-9afa-a43e9c626940";
    private static final String USER_AGENT = "Java";

    @Before
    public void before() throws InvalidKeyException
    {
        this.tokenService = new MockRefreshTokenService();

        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifier(emptyVerifier);
        this.tokenStore = new JwtJdbcTokenStore(converter, this.tokenService);

        //Create JWT refresh token
        this.correctToken = Jwts.builder()
                .setSubject(SUB)
                .setId(JTI)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100000))
                .claim("name", USER_NAME)
                .claim("loc", LOC)
                .claim("user_name", USER_NAME)
                .claim("client_id", CLIENT_ID)
                .claim("ati", ATI)
                .claim("user_agent", USER_AGENT)
                .claim("scope", new String[]
                {
                    "read", "write"
        })
                .claim("authorities", new String[]
                {
                    "ROLE_ADMIN"
        })
                .compact();

        //Create domain JWT refresh token
        JwtRefreshToken domRefreshToken = new JwtRefreshToken();
        domRefreshToken.setAti(UUID.fromString(ATI));
        domRefreshToken.setClientId(CLIENT_ID);
        domRefreshToken.setJti(UUID.fromString(JTI));
        domRefreshToken.setLocation(LOC);
        domRefreshToken.setUsername(USER_NAME);
        domRefreshToken.setUserId(UUID.fromString(SUB));
        this.tokenService.saveToken(domRefreshToken);
    }

    @Test
    public void readRefreshToken_GivenCorrectToken_ReturnsOAuth2RefreshToken()
    {
        OAuth2RefreshToken r = tokenStore.readRefreshToken(this.correctToken);

        assertTrue(r != null);
    }

    //If a token is read that is not saved in the database it should be seen as an invalid token
    @Test(expected = InvalidTokenException.class)
    public void readRefreshToken_GivenNotSavedToken_ThrowsInvalidTokenException()
    {
        String jti = "ab83602d-98dd-4cf2-9afa-a43e9c626949";

        //Create JWT refresh token with a wrong jti
        String wrongToken = Jwts.builder()
                .setSubject(SUB)
                .setId(jti)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100000))
                .claim("name", USER_NAME)
                .claim("loc", LOC)
                .claim("user_name", USER_NAME)
                .claim("client_id", CLIENT_ID)
                .claim("ati", ATI)
                .claim("user_agent", USER_AGENT)
                .claim("scope", new String[]
                {
                    "read", "write"
        })
                .claim("authorities", new String[]
                {
                    "ROLE_ADMIN"
        })
                .compact();

        tokenStore.readRefreshToken(wrongToken);

    }
    
    @Test(expected = InvalidTokenException.class)
    public void readRefreshToken_GivenEmptyString_ThrowsInvalidTokenException()
    {
        tokenStore.readRefreshToken("");
    }
    
    @Test(expected = InvalidTokenException.class)
    public void readRefreshToken_GivenIllFormattedToken_ThrowsInvalidTokenException()
    {
        tokenStore.readRefreshToken(
                "eyJhbGciOiJub25lIn0.eyJzdWIiOiI4YTkxOWFjMS02OWUzLWJjYTEtMDE2OS"
                        + "1lM2JjY2I0NTAwMDAiLCJqdGkiOiJhYjgzNjAyZC05OGRkLTRjZj"
                        + "ItOWFmYS1hNDNlOWM2MjY5NDkiLCJpYXQiOjE1NTc4MjExNzUsIm"
                        + "V4cCI6MTU1NzgyMTI3NSwibmFtZSI6ImFuZ2Vsb2MiLCJsb2MiOi"
                        + "IxMC4wLjAuMCBVbmtub3duIiwidXNl.cl9uYW1lIjoiYW5nZWxvYy"
                        + "IsImNsaWVudF9pZCI6ImZpcnN0X2NsaWVudCIsImF0aSI6ImU3Nz"
                        + "I1OTFlLTk4ZGQtNGNmMi05YWZhLWE0M2U5YzYyNjk0MCIsInVzZX"
                        + "JfYWdlbnQiOiJKYXZhIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl"
                        + "0sImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXX0."
        );
    }
}
