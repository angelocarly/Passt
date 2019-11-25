package com.realdolmen.passt.service;

import com.realdolmen.passt.domain.JwtRefreshToken;
import com.realdolmen.passt.persistence.RefreshTokenRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Angelo Carly
 */
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService
{

    @Autowired
    private RefreshTokenRepository tokenRepository;
    
    @Override
    public Optional<JwtRefreshToken> findByJtiAndClientId(UUID fromString, String asString)
    {
        return tokenRepository.findByJtiAndClientId(fromString, asString);
    }

    @Override
    public void deleteByJtiAndClientId(UUID fromString, String asString)
    {
        tokenRepository.deleteByJtiAndClientId(fromString, asString);
    }

    @Override
    public void saveToken(JwtRefreshToken crt)
    {
        tokenRepository.save(crt);
    }

}
