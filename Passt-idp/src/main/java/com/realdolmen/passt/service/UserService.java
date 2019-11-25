package com.realdolmen.passt.service;

import com.realdolmen.passt.domain.User;
import com.realdolmen.passt.exception.EmailExistsException;
import com.realdolmen.passt.exception.UsernameExistsException;
import java.util.UUID;

/**
 *
 * @author Angelo Carly
 */
public interface UserService
{

    public User getUser(String username);
    public void createUser(User user) throws EmailExistsException, UsernameExistsException;

    public User findByUsername(String name);
    public User findById(UUID id);
    public User findBySecret2Fa(String secret);
    
}
