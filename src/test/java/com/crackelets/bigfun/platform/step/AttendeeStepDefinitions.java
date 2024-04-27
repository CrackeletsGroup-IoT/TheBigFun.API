package com.crackelets.bigfun.platform.step;

import com.crackelets.bigfun.platform.profile.domain.model.Attendee;
import com.crackelets.bigfun.platform.profile.domain.persistence.AttendeeRepository;
import com.crackelets.bigfun.platform.profile.resource.CreateAttendeeResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Map;

@TestConfiguration

@CucumberContextConfiguration
public class AttendeeStepDefinitions {

    @Autowired
    private AttendeeRepository attendeeRepository;
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
        CreateAttendeeResource resource = new CreateAttendeeResource()
                .withName(name)
                .withUserName(userName)
                .withEmail(email);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateAttendeeResource> request = new HttpEntity<>(resource, headers);
        responseEntity = testRestTemplate.postForEntity(endpointPath, request, String.class);
    }


    @Then("A Response is received with Status {int}")
    public void aResponseIsReceivedWithStatus(int expectedStatus) {
        HttpStatus currentStatus = (HttpStatus) responseEntity.getStatusCode();
        Assert.assertEquals(expectedStatus, currentStatus.value());
    }

    @And("An Attendee Resource is included in Response Body, with values  {string}, {string}, {string}")
    public void anAttendeeResourceIsIncludedInResponseBodyWithValues(String name, String userName, String email) {

        String responseBody = responseEntity.getBody();
        Assert.assertNotNull(responseBody);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Attendee attendee = objectMapper.readValue(responseBody, Attendee.class);
            Assert.assertEquals(name, attendee.getName());
            Assert.assertEquals(userName, attendee.getUserName());
            Assert.assertEquals(email, attendee.getEmail());
        } catch (JsonProcessingException e) {
            Assert.fail("Error parsing response body: " + e.getMessage());
        }

    }

    @Given("An Attendee Resource with values {string}, {string}, {string} is already stored")
    public void anAttendeeResourceWithValuesIsAlreadyStored(String name, String userName, String email) {

        // Verificar si ya existe un Attendee con los mismos valores
        Attendee existingAttendee = attendeeRepository.findByEmail(email);
        if (existingAttendee == null) {
            Attendee attendee = new Attendee();
            attendee.setName(name);
            attendee.setUserName(userName);
            attendee.setEmail(email);
            attendeeRepository.save(attendee);
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
