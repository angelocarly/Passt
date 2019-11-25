package com.realdolmen.passt.config;

import java.io.IOException;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 *
 * @author Angelo Carly
 *
 * Resource Server Configuration
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter
{

    /**
     * Configures the availability of the Resource Server's endpoints.
     *
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception
    {
        //Sets '/login', '/oauth/authorize' and '/oauth/token' accessible for all users
        //Use authentication on all other URLs
        http
                .headers().frameOptions().sameOrigin() //Enable h2 console view
                .and()
                .authorizeRequests().antMatchers("/login", "/oauth/authorize", "/oauth/token", "/h2/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and().cors().disable();

        http.csrf().disable();

        // Customize the application security
        http.requiresChannel().anyRequest().requiresSecure();
    }

    /**
     * Configure the token services
     * @param config 
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer config)
    {
        config.tokenServices(tokenServices());
    }

    /**
     * Provide a global tokenstore
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore()
    {
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * Configures asymmetric signing of tokens using public key
     * resources/jws/public.txt
     *
     * @return JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter()
    {

        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setAccessTokenConverter(new JwtConverter());
        Resource resource = new ClassPathResource("jws/public.txt");
        String publicKey = null;
        try
        {
            publicKey = IOUtils.toString(resource.getInputStream());
        } catch (final IOException e)
        {
            throw new RuntimeException(e);
        }
        converter.setVerifierKey(publicKey);
        return converter;
    }

    /**
     * Provide the custom tokenStore
     *
     * @return DefaultTokenServices
     */
    @Bean
    public DefaultTokenServices tokenServices()
    {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

}
