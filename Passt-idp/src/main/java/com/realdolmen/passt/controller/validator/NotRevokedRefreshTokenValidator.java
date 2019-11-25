package com.realdolmen.passt.controller.validator;

import com.realdolmen.passt.dto.TokenDto;
import com.realdolmen.passt.persistence.UserRepository;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 *
 * @author Angelo Carly
 */
class NotRevokedRefreshTokenValidator implements ConstraintValidator<NotRevokedRefreshToken, String> {
 
    private final TokenStore tokenStore;
 
    public NotRevokedRefreshTokenValidator(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }
 
    @Override
    public void initialize(NotRevokedRefreshToken constraint) {
    }   
 
    @Override
    public boolean isValid(String token, ConstraintValidatorContext context) {
        if(token != null)
        {
            try
            {
                tokenStore.readRefreshToken(token);
                return true;
            }
            catch(Exception e)
            {
                return false;
            }
        }
        return false;
    }
 
}