package com.realdolmen.passt.controller;

import com.realdolmen.passt.domain.Credential;
import com.realdolmen.passt.exception.CredentialNotFoundException;
import com.realdolmen.passt.service.AccessTokenService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.realdolmen.passt.repository.CredentialRepository;
import com.realdolmen.passt.service.CredentialService;
import java.util.Map;
import java.util.UUID;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Angelo Carly
 *
 * Controller for getting the User's Credentials
 *
 */
@RestController
@Validated
public class CredentialController
{

    Logger logger = LoggerFactory.getLogger(CredentialController.class);

    private CredentialService credentialService;

    private AccessTokenService accessTokenService;
    
    public CredentialController(CredentialService credentialService, AccessTokenService accessTokenService)
    {
        this.credentialService = credentialService;
        this.accessTokenService = accessTokenService;
    }

    /**
     * Maps GET request to get all credentials
     *
     * @return List of all credentials
     */
    @GetMapping(value = "/credentials", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Credential> getCredentials()
    {

        UUID id = accessTokenService.getAccessToken().getUserId();
        
        return credentialService.findByUserId(id);
    }

    /**
     * Maps GET request to get credential by ID
     *
     * @param id
     * @return List of all credentials of logged in user
     */
    @RequestMapping(value = "/credentials/{id}", method = RequestMethod.GET)
    public Credential getCredentials(@PathVariable UUID id)
    {

        UUID userId = accessTokenService.getAccessToken().getUserId();

        Credential credential = credentialService.findById(id).orElseThrow(() -> new CredentialNotFoundException());

        if (!credential.getUserId().equals(userId))
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid user account");
        }
        
        return credential;
    }

    /**
     * Maps POST request to post a credential
     *
     * @param credential
     */
    @RequestMapping(value = "/credentials", method = RequestMethod.POST)
    public Credential postCredential(@RequestBody @Valid Credential credential)
    {
        UUID id = accessTokenService.getAccessToken().getUserId();

        credential.setUserId(id);
        credentialService.save(credential);

        return credential;
    }

    /**
     * Maps DELETE request to delete a credential
     *
     */
    @RequestMapping(value = "/credentials/{id}", method = RequestMethod.DELETE)
    public void deleteCredential(@PathVariable UUID id)
    {
        UUID userId = accessTokenService.getAccessToken().getUserId();

        Credential c = credentialService.findById(id).orElseThrow(() -> new CredentialNotFoundException());

        if (!c.getUserId().equals(userId))
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid user account");
        }

        credentialService.deleteById(id);
    }
}
