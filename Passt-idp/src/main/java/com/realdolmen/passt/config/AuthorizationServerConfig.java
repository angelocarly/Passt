package com.realdolmen.passt.config;

import com.realdolmen.passt.service.JwtConverter;
import com.realdolmen.passt.service.PasstTokenEnhancer;
import com.realdolmen.passt.persistence.JwtJdbcTokenStore;
import com.realdolmen.passt.service.RefreshTokenService;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

/**
 *
 * @author Angelo Carly
 *
 * Authorization Server Configuration
 * 
 * Configures Client details and Jwt token enhancer
 */
@Configuration
@EnableAuthorizationServer
@Order(0)
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter
{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Environment environment;

    @Value("${passt.client.address}")
    private String clientAddress;
    
    @Value("${server.port}")
    private String serverPort;
    
    @Value("${jws.password}")
    private String jwsPassword;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Bean
    public JwtAccessTokenConverter accessTokenConverter()
    {
        
        JwtAccessTokenConverter converter = new JwtConverter();
        KeyStoreKeyFactory keyStoreKeyFactory
                = new KeyStoreKeyFactory((new ClassPathResource("jws/key.jks")), jwsPassword.toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("passtkey"));
        return converter;
    }

    @Bean
    public TokenStore tokenStore()
    {
        return new JwtJdbcTokenStore(accessTokenConverter(), refreshTokenService);
    }

    @Bean
    public TokenEnhancer tokenEnhancer()
    {
        return new PasstTokenEnhancer();
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices()
    {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setAccessTokenValiditySeconds(30);
        return defaultTokenServices;
    }
    
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception
    {
        clients
                .inMemory()
                .withClient(environment.getProperty("passtClient1"))
                .secret(passwordEncoder.encode(environment.getProperty("passtSecret1")))
                .scopes("read", "write")
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .redirectUris("https://localhost:4200/api/callback", "https://10.0.2.2:4200/api/callback")
                .autoApprove("read", "write")
                .refreshTokenValiditySeconds(600)
                .accessTokenValiditySeconds(300)
                .and()
                .withClient("token-client")
                .secret(passwordEncoder.encode("secret"))
                .scopes("read", "write")
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .redirectUris(
                        String.format("https://%s:%s/connect/passt", clientAddress, serverPort), 
                        String.format("https://localhost:%s/connect/passt", serverPort),
                        "https://localhost/connect/passt")
                .autoApprove("read", "write")
                .refreshTokenValiditySeconds(600)
                .accessTokenValiditySeconds(300);
    }

    
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception
    {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(
                Arrays.asList(tokenEnhancer(), accessTokenConverter())
        );

        //Provide the Authorization Server with the custom tokenStore and AuthenticationManager
        endpoints
                .accessTokenConverter(accessTokenConverter())
                .tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .tokenEnhancer(tokenEnhancerChain);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security)
    {
        security.allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .passwordEncoder(this.passwordEncoder);
    }

}
