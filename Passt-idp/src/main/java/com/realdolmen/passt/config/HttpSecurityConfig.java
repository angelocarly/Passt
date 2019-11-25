package com.realdolmen.passt.config;

import com.realdolmen.passt.service.MFAWebAuthenticationDetailsSource;
import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 *
 * @author Angelo Carly
 * 
 * Configuration class for HttpSecurity
 * Configures security of endpoints
 */
@Configuration
@EnableWebSecurity
@Order(4)
public class HttpSecurityConfig extends WebSecurityConfigurerAdapter
{

    @Autowired
    private Filter ssoFilter;

    @Autowired
    private MFAWebAuthenticationDetailsSource authenticationDetailsSource;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        //Login settings
        http
                .formLogin()
                .authenticationDetailsSource(authenticationDetailsSource)
                .loginPage("/login")
                .defaultSuccessUrl("/index")
                .permitAll()
                .and()
                .logout().logoutUrl("/logout");

        //Client settings
        http
                .authorizeRequests()
                .antMatchers("/client").permitAll()
                .antMatchers("/client/**").authenticated()
                .and()
                .addFilterAfter(ssoFilter, BasicAuthenticationFilter.class);

        //General settings
        http.authorizeRequests()
                .antMatchers("/register", "/webjars/**", "/script/**", "/error**", "/login*", "/h2/**").permitAll()
                .and()
                .headers().frameOptions().sameOrigin()//Enable h2 console
                .and()
                .cors().disable();

        // Require all other requests to be authenticated
        http.authorizeRequests().anyRequest().authenticated();

        // SSL
        http.requiresChannel().anyRequest().requiresSecure();

        //Disable csrf
        http.csrf().disable();
    }

    @Override
    protected void configure(
            AuthenticationManagerBuilder auth) throws Exception
    {
        auth.authenticationProvider(authenticationProvider);
    }
}
