package com.realdolmen.passt.controller;

import java.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Angelo Carly
 */
@RestController
public class UserController
{

    @GetMapping("/user/me")
    public Principal user(Principal principal)
    {
        return principal;
    }
    
}
