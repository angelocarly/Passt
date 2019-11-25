package com.realdolmen.passt.persistence;

import com.realdolmen.passt.domain.Authority;
import com.realdolmen.passt.domain.AuthorityType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Angelo Carly
 */
public interface AuthorityRepository extends JpaRepository<Authority, Integer>
{
    public Authority findByName(AuthorityType name);
}
