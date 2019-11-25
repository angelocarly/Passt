/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realdolmen.passt.controller.validator;

import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.AdditionalMatchers.not;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 *
 * @author Angelo Carly
 * Test on the NotRevokedRefreshTokenValidator which checks if a refresh token
 * is valid by reading it in the tokenstore
 */
public class NotRevokedRefreshTokenValidatorTest
{
    
    private NotRevokedRefreshTokenValidator validator;
    
    private TokenStore mockTokenStore;
    
    private String validToken;
    
    @Before
    public void setUp()
    {
        //Create JWT refresh token
        this.validToken = Jwts.builder()
                .setSubject("testuser")
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100000))
                .compact();
        
        //Mocks
        this.mockTokenStore = Mockito.mock(TokenStore.class);
        when(mockTokenStore.readRefreshToken(this.validToken)).thenReturn(new DefaultOAuth2RefreshToken(this.validToken));
        when(mockTokenStore.readRefreshToken(not(eq(this.validToken)))).thenThrow(InvalidTokenException.class);
        
        validator = new NotRevokedRefreshTokenValidator(mockTokenStore);
    }
    
    @Test
    public void GivenValidJwt_ReturnsTrue()
    {
        boolean result = validator.isValid(this.validToken, null);
        assertEquals(true, result);
    }
    
    @Test
    public void GivenNoDotSeparators_ReturnsFalse()
    {
        String token = 
                "eyJhbGciOiJIUzI1%NiIsI$nR5cCI6IkpXVCJ9eyJzdWIiOiIxMjM0N"
                + "TY3ODkwIiwibmFtZSI6IkpvaG4gRG9£lIiwiaWF0IjoxNTE2MjM5MDIyfQS"
                + "flKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        
        boolean result = validator.isValid(token, null);
        assertEquals(false, result);
    }
    
    @Test
    public void GivenEmptyString_ReturnsFalse()
    {
        String token = 
                "";
        
        boolean result = validator.isValid(token, null);
        assertEquals(false, result);
    }
    
}
