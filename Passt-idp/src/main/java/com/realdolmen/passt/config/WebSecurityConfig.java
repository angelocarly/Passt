package com.realdolmen.passt.config;

import com.realdolmen.passt.service.MFAAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 
 * @author Angelo Carly
 * This class sets up security configurations for a more global scope, such as:
 *   - userDetailsService setup
 *   - passwordEncoder bean
 *   - MFA setup
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{

    /**
     * Injection of custom UserDetailsServiceImpl Provides Spring Security with
     * user details from the database
     */
    @Autowired
    private UserDetailsService userDetailsService;

    //Provide BCrypt (12 cycles) as password encryption
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder(12);
    }
    
    /**
     * Provide Spring Security's authentication manager
     *
     * @return
     * @throws Exception
     */
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    /**
     * Configure the authenticationManager to use the custom userDetailsService
     * and passwordEncoder
     *
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) 
            throws Exception
    {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
    
    
    @Bean
    public AuthenticationProvider authProvider()
    {
        MFAAuthenticationProvider authProvider = new MFAAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
}
