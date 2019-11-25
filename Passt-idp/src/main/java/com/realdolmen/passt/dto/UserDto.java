package com.realdolmen.passt.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Angelo Carly
 */
public class UserDto
{

    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String password;
    @NotNull
    @NotEmpty
    private String matchingPassword;

    @NotNull
    @NotEmpty
    private String email;
    
    private boolean using2FA;

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

    public String getMatchingPassword()
    {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword)
    {
        this.matchingPassword = matchingPassword;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public boolean isUsing2FA()
    {
        return using2FA;
    }

    public void setUsing2FA(boolean using2FA)
    {
        this.using2FA = using2FA;
    }
}
