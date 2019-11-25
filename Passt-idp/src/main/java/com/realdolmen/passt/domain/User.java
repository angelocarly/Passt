package com.realdolmen.passt.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Angelo Carly
 *
 * Domain class for Users
 */
@Entity
@Table
public class User implements UserDetails, Serializable
{

    @Id
    @GeneratedValue(generator = "uuid")
    private UUID id;

    @NotEmpty
    @Column(unique = true)
    private String email;

    @NotEmpty
    @Column(unique = true)
    private String username;

    @NotEmpty
    private String password;

    private boolean using2FA;
    private String secret2FA;

    //Many-to-many relationship with Authority
    //Creates a table named user_authority
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority",
            joinColumns =
            {
                @JoinColumn(name = "user_id")
            },
            inverseJoinColumns =
            {
                @JoinColumn(name = "authority_id")
            })
    private Set<Authority> authorities = new HashSet<>();

    public User()
    {
        this.secret2FA = Base32.random();
    }

    public User(String email, String username, String password)
    {
        this.email = email;
        this.username = username;
        this.password = password;
        this.secret2FA = Base32.random();
    }

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Set<Authority> getAuthorities()
    {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities)
    {
        this.authorities = authorities;
    }

    public boolean isUsing2FA()
    {
        return using2FA;
    }

    public void setUsing2FA(boolean isUsing2FA)
    {
        this.using2FA = isUsing2FA;
    }

    public String getSecret2FA()
    {
        return secret2FA;
    }

    public void setSecret2FA(String secret2FA)
    {
        this.secret2FA = secret2FA;
    }

    @Override
    public String toString()
    {
        return String.format("User(id:%s, email:%s, name:%s)", this.id.toString(), this.email, this.username);
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

}
