package com.realdolmen.passt.service;

import com.realdolmen.passt.domain.Authority;
import com.realdolmen.passt.domain.AuthorityType;
import com.realdolmen.passt.persistence.UserRepository;
import com.realdolmen.passt.domain.User;
import com.realdolmen.passt.exception.EmailExistsException;
import com.realdolmen.passt.exception.UsernameExistsException;
import com.realdolmen.passt.persistence.AuthorityRepository;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Angelo Carly
 */
@Service
public class UserServiceImpl implements UserService
{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuthorityRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     *
     * @param user the user details to create a new user
     * @throws UsernameExistsException when a user with the same username exists
     * @throws EmailExistsException when a user with the same email exists
     */
    @Override
    public void createUser(User user) throws UsernameExistsException, EmailExistsException
    {
        if (userRepository.findByUsername(user.getUsername()) != null)
        {
            throw new UsernameExistsException();
        }
        if (userRepository.findByEmail(user.getEmail()) != null)
        {
            throw new EmailExistsException();
        }

        //Hash the user's password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //Give newly created user the ROLE_USER authority
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authRepository.findByName(AuthorityType.ROLE_USER));
        user.setAuthorities(authorities);

        userRepository.save(user);
    }

    @Override
    public User getUser(String username)
    {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByUsername(String name)
    {
        return userRepository.findByUsername(name);
    }

    @Override
    public User findById(UUID id)
    {
        return userRepository.findById(id);
    }

    @Override
    public User findBySecret2Fa(String secret)
    {
        return userRepository.findBySecret2FA(secret);
    }

}
