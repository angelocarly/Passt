package com.realdolmen.passt.service;

import com.realdolmen.passt.domain.Credential;
import com.realdolmen.passt.repository.CredentialRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Angelo Carly
 */
@Service
public class CredentialServiceImpl implements CredentialService
{

    @Autowired
    private CredentialRepository credentialRepo;

    @Override
    public List<Credential> findByUserId(UUID id)
    {
        return credentialRepo.findByUserId(id);
    }

    @Override
    public Optional<Credential> findById(UUID id)
    {
        return credentialRepo.findById(id);
    }

    @Override
    public void save(Credential credential)
    {
        credentialRepo.save(credential);
    }

    @Override
    public void deleteById(UUID id)
    {
        credentialRepo.deleteById(id);
    }

}
