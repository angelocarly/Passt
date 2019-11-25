package com.realdolmen.passt.service;

import com.realdolmen.passt.domain.JwtRefreshToken;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author Angelo Carly
 */
public interface RefreshTokenService {

    public Optional<JwtRefreshToken> findByJtiAndClientId(UUID fromString, String asString);

    public void deleteByJtiAndClientId(UUID fromString, String asString);

    public void saveToken(JwtRefreshToken crt);

}
