package com.realdolmen.passt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Angelo Carly
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Credential not found")
public class CredentialNotFoundException extends RuntimeException
{

}
