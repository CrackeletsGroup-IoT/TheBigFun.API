package com.crackelets.bigfun.platform.step;

import com.crackelets.bigfun.platform.booking.domain.model.Event;
import com.crackelets.bigfun.platform.booking.domain.persistence.EventRepository;
import com.crackelets.bigfun.platform.booking.resource.CreateEventResource;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Date;
import java.util.Map;

@TestConfiguration

@CucumberContextConfiguration
public class EventStepDefinitions {

    @Autowired
    private EventRepository eventRepository;
    private final TestRestTemplate testRestTemplate = new TestRestTemplate();

    private int randomServerPort;
    private String endpointPath;
    private ResponseEntity<String> responseEntity;

    @Given("The Endpoint {string} is available")
    public void theEndpointIsAvailable(String endpointPath) {
        this.endpointPath = String.format(endpointPath, randomServerPort);
    }

    @When("A Post Request is sent with values {string}, {string}{double}, {string},{string}{double},{string}")
    public void aPostRequestIsSentWithValues(String name, String address, int capacity, String image, Date date, int cost, String district) {

        CreateEventResource resource = new CreateEventResource()
                .withName(name)
                .withAddress(address)
                .withCapacity(capacity)
                .withImage(image)
                .withDate(String.valueOf(date))
                .withCost(cost)
                .withDistrict(district);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateEventResource> request = new HttpEntity<>(resource, headers);
        responseEntity = testRestTemplate.postForEntity(endpointPath, request, String.class);
    }

    @Then("A Response is received with Status {int}")
    public void aResponseIsReceivedWithStatus(int expectedStatus) {
        HttpStatus currentStatus = (HttpStatus) responseEntity.getStatusCode();
        Assert.assertEquals(expectedStatus, currentStatus.value());
    }

    @And("An Event Resource is included in Response Body, with values {string}, {string}{double}, {string},{string}{double},{string}")
    public void anEventResourceIsIncludedInResponseBodyWithValues(String name, String address, int capacity, String image, Date date, int cost, String district) {

        String responseBody = responseEntity.getBody();
        Assert.assertNotNull(responseBody);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Event event = objectMapper.readValue(responseBody, Event.class);
            Assert.assertEquals(name, event.getName());
            Assert.assertEquals(address, event.getAddress());
            Assert.assertEquals(capacity, event.getCapacity());
            Assert.assertEquals(image, event.getImage());
            Assert.assertEquals(date, event.getDate());
            Assert.assertEquals(cost, event.getCost());
            Assert.assertEquals(district, event.getDistrict());
        } catch (JsonProcessingException e) {
            Assert.fail("Error parsing response body: " + e.getMessage());
        }
    }


    @Given("An Event Resource with values {string}, {string}{double}, {string},{string}{double},{string} is already stored")
    public void anEventResourceWithValuesIsAlreadyStored(String name, String address, int capacity, String image, Date date, int cost, String district) {

        // Verificar si ya existe un event con los mismos valores
        Event existingEvent = eventRepository.findByName(name);
        if (existingEvent == null) {
            // No existe un event con los mismos valores, crear uno nuevo
            Event event = new Event();
            event.setName(name);
            event.setAddress(address);
            event.setCapacity(capacity);
            event.setImage(image);
            event.setDate(String.valueOf(date));
            event.setCost(cost);
            event.setDistrict(district);
            eventRepository.save(event);
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
