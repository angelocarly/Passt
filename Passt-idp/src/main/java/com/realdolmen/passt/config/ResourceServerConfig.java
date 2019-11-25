    package com.realdolmen.passt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * 
 * @author Angelo Carly
 * 
 * Configuration class for the Resource Server of the token-client
 * Enables and configures the path of the resource server
 */
@Order(3)
@EnableResourceServer
@Configuration
public class ResourceServerConfig
        extends ResourceServerConfigurerAdapter
{

    @Override
    public void configure(HttpSecurity http) throws Exception
    {
        //Access tokens are required for /client/api/**, nowhere else
        http.antMatcher("/client/api/**").authorizeRequests().anyRequest().authenticated();
    }

}
