package com.realdolmen.passt.controller.validator;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Angelo Carly
 * Test on the RefreshTokenValidator which checks if a refresh token is correctly formatted
 */
public class RefreshTokenValidatorTest
{
    
    private RefreshTokenValidator validator;
    
    @Before
    public void setUp()
    {
        validator = new RefreshTokenValidator();
    }
    
    @Test
    public void GivenValidJwt_ReturnsTrue()
    {
        String token = 
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0N"
                + "TY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.S"
                + "flKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        
        boolean result = validator.isValid(token, null);
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
