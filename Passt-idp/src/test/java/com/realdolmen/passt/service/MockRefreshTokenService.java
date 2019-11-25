package com.realdolmen.passt.service;

import com.realdolmen.passt.domain.JwtRefreshToken;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author Angelo Carly
 */
public class MockRefreshTokenService implements RefreshTokenService
{

    private Map<UUID, JwtRefreshToken> tokens = new HashMap();
    
    @Override
    public Optional<JwtRefreshToken> findByJtiAndClientId(UUID uuid, String clientId)
    {
        if(tokens.containsKey(uuid))
        {
            JwtRefreshToken t = tokens.get(uuid);
            if(t.getClientId().equals(clientId)) return Optional.of(t);
        }
        
        return Optional.empty();
    }

    @Override
    public void deleteByJtiAndClientId(UUID uuid, String clientId)
    {
        if(tokens.containsKey(uuid))
        {
            JwtRefreshToken t = tokens.get(uuid);
            if(t.getClientId().equals(clientId)) tokens.remove(uuid);
        }}

    @Override
    public void saveToken(JwtRefreshToken crt)
    {
        tokens.put(crt.getJti(), crt);
    }

}
