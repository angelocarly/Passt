package com.realdolmen.passt.repository;

import com.realdolmen.passt.domain.Credential;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Angelo Carly
 */
public interface CredentialRepository extends JpaRepository<Credential, Integer>
{
        
    List<Credential> findByUserId(UUID userId);
    
    Optional<Credential> findById(UUID id);
    
    /**
     * Transactional because of this
     * https://stackoverflow.com/questions/32269192/spring-no-entitymanager-with-actual-transaction-available-for-current-thread
     */
    @Transactional
    void deleteById(UUID id);
}
