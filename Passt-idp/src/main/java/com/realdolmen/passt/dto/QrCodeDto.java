package com.realdolmen.passt.dto;

import java.util.UUID;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * @author Angelo Carly
 */
public class QrCodeDto
{

    @NotNull
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$", message = "Invalid formatted UUID")
    private String userId;

    @NotNull
    @NotEmpty
    private String secret2FA;

    public UUID getUserId()
    {
        return UUID.fromString(userId);
    }

    public void setUserId(UUID userId)
    {
        this.userId = userId.toString();
    }

    public String getSecret2FA()
    {
        return secret2FA;
    }

    public void setSecret2FA(String secret2FA)
    {
        this.secret2FA = secret2FA;
    }

}
