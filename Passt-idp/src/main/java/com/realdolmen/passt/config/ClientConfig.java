package com.realdolmen.passt.config;

import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.context.request.RequestContextListener;

/**
 * 
 * @author Angelo Carly
 * 
 * Configuration of the token client
 * 
 * Configures the redirect uri for the client
 */
@Order(5)
@Configuration
@EnableOAuth2Client
public class ClientConfig extends WebSecurityConfigurerAdapter
{

    private static final String TOKEN_CLIENT_URI = "/connect/passt";
    
    @Autowired
    private OAuth2ClientContext oauth2ClientContext;

    @Bean
    public OAuth2RestTemplate oauthRestTemplate()
    {
        return new OAuth2RestTemplate(passt(), oauth2ClientContext);
    }
    
    @Value("${server.port}")
    private String serverPort;

    @Bean
    public Filter ssoFilter()
    {
        OAuth2ClientAuthenticationProcessingFilter passtFilter = new OAuth2ClientAuthenticationProcessingFilter(TOKEN_CLIENT_URI);
        OAuth2RestTemplate passtTemplate = new OAuth2RestTemplate(passt(), oauth2ClientContext);
        passtFilter.setRestTemplate(passtTemplate);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(String.format("https://localhost:%s/client/api/me", serverPort), passt().getClientId());
        tokenServices.setRestTemplate(passtTemplate);
        passtFilter.setTokenServices(tokenServices);
        return passtFilter;
    }

    @Bean
    @ConfigurationProperties("passt.client")
    public AuthorizationCodeResourceDetails passt()
    {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistratiomn(OAuth2ClientContextFilter filter)
    {
        FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<OAuth2ClientContextFilter>();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    @Bean
    public RequestContextListener requestContextListener()
    {
        return new RequestContextListener();
    }

}
