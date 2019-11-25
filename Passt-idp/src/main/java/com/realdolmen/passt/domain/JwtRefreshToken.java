package com.realdolmen.passt.domain;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author Angelo Carly
 * 
 * Domain class for Refresh tokens
 */
@Entity
public class JwtRefreshToken implements Serializable
{

    @Id
    @GeneratedValue(generator = "uuid")
    private UUID id;
    
    private UUID jti;
    private UUID userId;
    private UUID ati;
    private String clientId;
    private String username;
    private String userAgent;
    private String location;

    public JwtRefreshToken() {}
    
    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    public UUID getUserId()
    {
        return userId;
    }

    public void setUserId(UUID userId)
    {
        this.userId = userId;
    }

    public String getClientId()
    {
        return clientId;
    }

    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    public UUID getJti()
    {
        return jti;
    }

    public void setJti(UUID jti)
    {
        this.jti = jti;
    }

    public UUID getAti()
    {
        return ati;
    }

    public void setAti(UUID ati)
    {
        this.ati = ati;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUserAgent()
    {
        return userAgent;
    }

    public void setUserAgent(String userAgent)
    {
        this.userAgent = userAgent;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }
    
    

}
