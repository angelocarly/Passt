package com.realdolmen.passt.service;

import com.realdolmen.passt.domain.User;
import com.realdolmen.passt.persistence.UserRepository;
import org.jboss.aerogear.security.otp.Totp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 *
 * @author Angelo Carly
 * 
 * 
 */
public class MFAAuthenticationProvider extends DaoAuthenticationProvider
{

    @Autowired
    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(MFAAuthenticationProvider.class);

    //Verify MFA Authentication code
    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException
    {
        String verificationCode
                = ((MFAWebAuthenticationDetails) auth.getDetails())
                        .getVerificationCode();

        User user = userRepository.findByUsername(auth.getName());
        if ((user == null))
        {
            throw new BadCredentialsException("Invalid username or password");
        }
        if (user.isUsing2FA())
        {
            Totp totp = new Totp(user.getSecret2FA());
            if (!isValidLong(verificationCode) || !totp.verify(verificationCode))
            {
                throw new BadCredentialsException("Invalid verification code");
            }
        }

        Authentication result = super.authenticate(auth);
        return new UsernamePasswordAuthenticationToken(
                user, result.getCredentials(), result.getAuthorities());
    }

    private boolean isValidLong(String code)
    {
        try
        {
            Long.parseLong(code);
        } catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }

    @Override
    public boolean supports(Class<?> authentication)
    {
        return (UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authentication));
    }
}
