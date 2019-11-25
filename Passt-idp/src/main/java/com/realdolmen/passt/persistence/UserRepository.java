package com.realdolmen.passt.persistence;

import com.realdolmen.passt.domain.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Angelo Carly
 */
public interface UserRepository extends JpaRepository<User, Integer>
{
    User findById(UUID id);
    User findByUsername(String username);
    User findByEmail(String email);
    User findBySecret2FA(String secret);
}
