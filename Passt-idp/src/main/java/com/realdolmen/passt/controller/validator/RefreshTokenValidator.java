package com.realdolmen.passt.controller.validator;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author Angelo Carly
 */
class RefreshTokenValidator implements ConstraintValidator<RefreshToken, String> {

 
    @Override
    public void initialize(RefreshToken constraint) {
    }   
 
    @Override
    public boolean isValid(String token, ConstraintValidatorContext context) {
        if(token != null)
        {
            try
            {
                JWT.decode(token);
                return true;
            }
            catch(JWTDecodeException e)
            {
                return false;
            }
        }
        return false;
    }
 
}