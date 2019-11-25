package com.realdolmen.passt.service;

import com.realdolmen.passt.domain.Credential;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author Angelo Carly
 */
public interface CredentialService {

    public List<Credential> findByUserId(UUID id);

    public Optional<Credential> findById(UUID id);

    public void save(Credential credential);

    public void deleteById(UUID id);

}
