package com.realdolmen.passt.dto;

import com.auth0.jwt.JWT;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.realdolmen.passt.controller.validator.NotRevokedRefreshToken;
import com.realdolmen.passt.controller.validator.RefreshToken;
import java.util.UUID;
import javax.validation.constraints.Pattern;

/**
 *
 * @author Angelo Carly
 */
public class JtiDto
{

    @NotNull
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$", message = "Invalid formatted UUID")
    private String jti;

    public UUID getJti()
    {
        return UUID.fromString(jti);
    }

    public void setJti(String jti)
    {
        this.jti = jti;
    }
    
}
