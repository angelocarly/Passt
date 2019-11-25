package com.realdolmen.passt.controller;

import com.realdolmen.passt.domain.JwtRefreshToken;
import com.realdolmen.passt.dto.JtiDto;
import com.realdolmen.passt.service.TokenService;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author Angelo Carly
 * 
 * Configures the token-client's endpoints
 */
@Controller
public class ClientController
{
    
    @Autowired
    private OAuth2RestTemplate rest;

    @Autowired
    private OAuth2ClientContext c;
    
    @Autowired
    private TokenService tokenService;
    
    @Value("${server.port}")
    private String serverPort;

    Logger logger = LoggerFactory.getLogger(ClientController.class);
    
    //Redirect to /client
    @GetMapping("/")
    public RedirectView redirectHome(
      RedirectAttributes attributes) {
        return new RedirectView("/client");
    }
    
    //Redirect to /client
    @GetMapping("/index")
    public RedirectView redirectIndex(
      RedirectAttributes attributes) {
        return new RedirectView("/client");
    }
    
    //Show the index page
    @GetMapping("/client")
    public String index(Model model)
    {
        model.addAttribute("access_token", c.getAccessToken());
        
        boolean loggedIn = c.getAccessToken() != null;
        model.addAttribute("loggedIn", loggedIn);
        if(loggedIn) model.addAttribute("username", rest.getForObject(String.format("https://localhost:%s/client/api/me", serverPort), String.class));
        
        return "index";
    }
    
    //Show the tokens page
    @GetMapping("/client/tokens")
    public String tokens(Model model)
    {
        model.addAttribute("tokens", rest.getForObject(String.format("https://localhost:%s/client/api/tokens", serverPort), JwtRefreshToken[].class));
        return "tokens";
    }
    
    //Logout
    @GetMapping("/client/logout")
    public String logout(RedirectAttributes attributes, HttpSession session)
    {
        if(c.getAccessToken() != null) tokenService.revokeTokenByAccessToken(c.getAccessToken());
        c.setAccessToken(null);
        session.invalidate();
        return "redirect:/client";
    }
    
    //Revoke a token by jti
    @PostMapping("/client/revoke")
    @ResponseBody
    public void revokeToken(@RequestBody @Valid JtiDto token)
    {
        rest.postForObject(String.format("https://localhost:%s/client/api/revoke", serverPort), token, String.class);
    }
    
    //Revoke all tokens
    @PostMapping("/client/revokeall")
    @ResponseBody
    public void revokeAll()
    {
        rest.postForObject(String.format("https://localhost:%s/client/api/revokeall", serverPort), null, String.class);
    }

}
