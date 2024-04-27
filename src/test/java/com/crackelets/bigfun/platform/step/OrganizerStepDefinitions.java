package com.crackelets.bigfun.platform.step;

import com.crackelets.bigfun.platform.profile.domain.model.Organizer;
import com.crackelets.bigfun.platform.profile.domain.persistence.OrganizerRepository;
import com.crackelets.bigfun.platform.profile.resource.CreateOrganizerResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@TestConfiguration
@CucumberContextConfiguration
public class OrganizerStepDefinitions {

    @Autowired
    private OrganizerRepository organizerRepository;
    private final TestRestTemplate testRestTemplate = new TestRestTemplate();

    private int randomServerPort;
    private String endpointPath;
    private ResponseEntity<String> responseEntity;



    @Given("The Endpoint {string} is available")
    public void theEndpointIsAvailable(String endpointPath) {
        this.endpointPath = String.format(endpointPath, randomServerPort);
    }

    @When("A Post Request is sent with values {string}, {string}, {string}")
    public void aPostRequestIsSentWithValues(String name, String userName, String email) {
        CreateOrganizerResource resource = new CreateOrganizerResource()
                .withName(name)
                .withUserName(userName)
                .withEmail(email);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateOrganizerResource> request = new HttpEntity<>(resource, headers);
        responseEntity = testRestTemplate.postForEntity(endpointPath, request, String.class);

    }


    @Then("A Response is received with Status {int}")
    public void aResponseIsReceivedWithStatus(int expectedStatus) {
        HttpStatus currentStatus = (HttpStatus) responseEntity.getStatusCode();
        Assert.assertEquals(expectedStatus, currentStatus.value());
    }

    @And("An Organizer Resource is included in Response Body, with values  {string}, {string}, {string}")
    public void anOrganizerResourceIsIncludedInResponseBodyWithValues(String name, String userName, String email) {

        String responseBody = responseEntity.getBody();
        Assert.assertNotNull(responseBody);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Organizer organizer = objectMapper.readValue(responseBody, Organizer.class);
            Assert.assertEquals(name, organizer.getName());
            Assert.assertEquals(userName, organizer.getUserName());
            Assert.assertEquals(email, organizer.getEmail());
        } catch (JsonProcessingException e) {
            Assert.fail("Error parsing response body: " + e.getMessage());
        }

    }


    @Given("An Organizer Resource with values {string}, {string}, {string} is already stored")
    public void anOrganizerResourceWithValuesIsAlreadyStored(String name, String userName, String email) {

        // Verificar si ya existe un Organizer con los mismos valores
        Organizer existingOrganizer = organizerRepository.findByEmail(email);
        if (existingOrganizer == null) {
            // No existe un Organizer con los mismos valores, crear uno nuevo
            Organizer organizer = new Organizer();
            organizer.setName(name);
            organizer.setUserName(userName);
            organizer.setEmail(email);
            organizerRepository.save(organizer);
        }

    }


    @And("A Message is included in Response Body, with value {string}")
    public void aMessageIsIncludedInResponseBodyWithValue(String expectedMessage) {

        String responseBody = responseEntity.getBody();
        Assert.assertNotNull(responseBody);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, String> responseMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, String>>() {});
            String actualMessage = responseMap.get("message");
            Assert.assertEquals(expectedMessage, actualMessage);
        } catch (JsonProcessingException e) {
            Assert.fail("Error parsing response body: " + e.getMessage());
        }

    }
}





