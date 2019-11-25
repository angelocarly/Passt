package com.realdolmen.passt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realdolmen.passt.domain.Credential;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.realdolmen.passt.mock.WithMockJwtUser;
import com.realdolmen.passt.repository.CredentialRepository;
import com.realdolmen.passt.service.AccessTokenService;
import com.realdolmen.passt.service.CredentialService;
import java.util.UUID;

/**
 *
 * @author Angelo Carly
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
@SpringBootTest
public class CredentialControllerTest
{

    private MockMvc mockMvc;

    private CredentialService mockCredentialService;

    @Before
    public void setup() throws Exception
    {

        this.mockCredentialService = mock(CredentialService.class);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new CredentialController(mockCredentialService, new AccessTokenService()))
                .build();

    }

    private void mockCredentials(Credential c)
    {
        List<Credential> credentials = new ArrayList<>();
        credentials.add(c);
        when(mockCredentialService.findByUserId(c.getUserId())).thenReturn(credentials);
        when(mockCredentialService.findById(c.getId())).thenReturn(Optional.of(c));
    }

    
    @Test
    @WithMockJwtUser(subject = "6069f0ce-8b84-48f4-863d-13c8fc354623")
    public void givenCorrectUsername_whenGetCredentials_doOk() throws Exception
    {
        String URI = "/credentials";

        mockCredentials(new Credential(UUID.randomUUID(), UUID.fromString("6069f0ce-8b84-48f4-863d-13c8fc354623"), "facebook", "angelo.carly@mail.com", "password"));
        mockCredentials(new Credential(UUID.randomUUID(), UUID.randomUUID(), "facebook", "sanderb@mail.com", "password"));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URI)
                .secure(true)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("facebook")));
    }

    @Test
    @WithMockJwtUser(subject = "6069f0ce-8b84-48f4-863d-13c8fc354623")
    public void givenCorrectUsername_whenGetSpecificCredential_doOk() throws Exception
    {
        UUID id = UUID.randomUUID();
        String URI = "/credentials/" + id.toString();

        mockCredentials(new Credential(id, UUID.fromString("6069f0ce-8b84-48f4-863d-13c8fc354623"), "facebook", "angelo.carly@mail.com", "password"));

        //Get the created credentials as angeloc
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URI)
                .secure(true)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("facebook")));
    }

    @Test
    @WithMockJwtUser(subject = "7269f0ce-8b84-48f4-863d-13c8fc354623")
    public void givenWrongUsername_whenGetSpecificCredential_doUnAuthorized() throws Exception
    {
        UUID id = UUID.randomUUID();
        String URI = "/credentials/" + id.toString();
        
        mockCredentials(new Credential(id, UUID.fromString("6069f0ce-8b84-48f4-863d-13c8fc354623"), "facebook", "angelo.carly@mail.com", "password"));

        //Try to get sanderb's credentials as angeloc
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URI)
                .secure(true)
                .accept(MediaType.APPLICATION_JSON);

        //User is unauthorized to get this request
        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockJwtUser(subject = "6069f0ce-8b84-48f4-863d-13c8fc354623")
    public void givenInvalidId_whenGetSpecificCredential_doBadRequest() throws Exception
    {
        String URI = "/credentials/abcdef";

        //Try to get credentials with wrongly formatted id
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URI)
                .secure(true)
                .accept(MediaType.APPLICATION_JSON);

        //Address is not found
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockJwtUser(subject = "6069f0ce-8b84-48f4-863d-13c8fc354623")
    public void givenNonExistentId_whenGetSpecificCredential_doNotFound() throws Exception
    {
        String URI = "/credentials/" + UUID.randomUUID().toString();

        //Try to get credential which doesn't exist
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URI)
                .secure(true)
                .accept(MediaType.APPLICATION_JSON);

        //Credential is not found
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockJwtUser(subject = "6069f0ce-8b84-48f4-863d-13c8fc354623")
    public void givenCorrectCredential_whenPostCredential_doOk() throws Exception
    {
        String URI = "/credentials";

        UUID id = new UUID(10, 10);
        Credential c = new Credential(UUID.randomUUID(), UUID.fromString("8269f0ce-8b84-48f4-863d-13c8fc354623"), "facebook", "a.c@mail.be", "password");

        //Post a correct credential
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URI)
                .secure(true)
                .content(asJsonString(c))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //Reauest is succesfull
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    @WithMockJwtUser(subject = "6069f0ce-8b84-48f4-863d-13c8fc354623")
    public void givenNoName_whenPostCredential_doBadRequest() throws Exception
    {
        String URI = "/credentials";

        Credential c = new Credential(UUID.randomUUID(), null, null, null, null);
        
        //Post a correct credential
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(URI)
                .secure(true)
                .content(asJsonString(c))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //Credential is not found
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockJwtUser(subject = "6069f0ce-8b84-48f4-863d-13c8fc354623")
    public void givenInvalidId_whenDeleteCredential_doNotFound() throws Exception
    {
        String URI = "/credentials/abcd";

        //Delete a non existing credential
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(URI)
                .secure(true);

        //Credential is not found
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockJwtUser(subject = "6069f0ce-8b84-48f4-863d-13c8fc354623")
    public void givenCorrectId_whenDeleteCredential_doOk() throws Exception
    {
        UUID id = UUID.randomUUID();
        String URI = "/credentials/" + id.toString();

        mockCredentials(new Credential(id, UUID.fromString("6069f0ce-8b84-48f4-863d-13c8fc354623"), "facebook", "angelo.carly@mail.com", "password"));
        
        //Delete a the existing credential
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(URI)
                .secure(true);

        //Credential is found
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj)
    {
        try
        {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
