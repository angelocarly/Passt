package com.realdolmen.passt.domain;

import java.util.Collection;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;

public class User extends org.springframework.security.core.userdetails.User {

   //This constructor is a must
    public User(String username, String password, boolean enabled, boolean accountNonExpired,
            boolean credentialsNonExpired, boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
    //Setter and getters are required
    private UUID id;

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

}