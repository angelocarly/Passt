package com.realdolmen.passt.domain;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Angelo Carly
 * 
 * Domain class for Login Credentials to sites
 */
@Entity
@Table
public class Credential
{
    @Id
    @Column(name="credential_id")
    @GeneratedValue(generator = "uuid")
    private UUID id;
    
    //One-to-many relationship with User
    @Column(name="user_id")
    private UUID userId;
    
    @NotNull
    private String name;
    
    private String username;
    private String password;

    public Credential() {}

    public Credential(UUID id, UUID userId, String name, String username, String password)
    {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.password = password;
    }
    
    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public UUID getUserId()
    {
        return userId;
    }

    public void setUserId(UUID userId)
    {
        this.userId = userId;
    }
    
    
}
