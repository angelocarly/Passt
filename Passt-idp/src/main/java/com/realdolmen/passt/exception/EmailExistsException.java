package com.realdolmen.passt.exception;

/**
 *
 * @author Angelo Carly
 */
public class EmailExistsException extends RuntimeException
{

    public EmailExistsException()
    {
        super("Email already exists");
    }

}
