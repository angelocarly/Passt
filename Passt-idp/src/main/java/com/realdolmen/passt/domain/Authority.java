package com.realdolmen.passt.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author Angelo Carly
 * 
 * Domain class for User Authorities a.k.a Roles
 */
@Entity
public class Authority implements GrantedAuthority
{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @Enumerated(EnumType.STRING)
    @NotEmpty
    private AuthorityType name;
    
    public Authority()
    {
        
    }
    
    public Authority(AuthorityType name)
    {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public AuthorityType getName()
    {
        return name;
    }

    public void setName(AuthorityType name)
    {
        this.name = name;
    }

    @Override
    public String getAuthority()
    {
        return this.name.name();
    }
    
    
    
}
