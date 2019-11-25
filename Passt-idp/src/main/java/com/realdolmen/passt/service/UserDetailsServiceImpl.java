package com.realdolmen.passt.service;

import com.realdolmen.passt.persistence.UserRepository;
import com.realdolmen.passt.domain.User;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Angelo Carly
 * 
 * Provides persisted user details to the authenticationProvider
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 
     * @param username
     * @return UserDetails of user with matching username
     * @throws UsernameNotFoundException 
     */
    @Override
    @Transactional(readOnly = true)
    public User loadUserByUsername(String username) throws UsernameNotFoundException
    {
        //Get user from database
        User user = userRepository.findByUsername(username);
        if(user == null) throw new UsernameNotFoundException(username);
        
        //Convert User Authorities to GrantedAuthorities
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        user.getAuthorities().forEach((auth) ->
        {
            grantedAuthorities.add(new SimpleGrantedAuthority(auth.getName().toString()));
        });
        
        //Map details to a Spring Security User
        return user;
    }
    
}
