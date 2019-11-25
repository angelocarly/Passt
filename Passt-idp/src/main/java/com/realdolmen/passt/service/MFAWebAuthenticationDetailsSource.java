package com.realdolmen.passt.service;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 *
 * @author Angelo Carly
 */
@Component
public class MFAWebAuthenticationDetailsSource implements
  AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {
     
    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new MFAWebAuthenticationDetails(context);
    }
}