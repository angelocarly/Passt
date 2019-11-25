package com.realdolmen.passt.exception;

/**
 *
 * @author Angelo Carly
 */
public class UsernameExistsException extends RuntimeException
{

    public UsernameExistsException()
    {
        super("Username already exists");
    }

    public UsernameExistsException(String message)
    {
        super("Username already exists: " + message);
    }

}
