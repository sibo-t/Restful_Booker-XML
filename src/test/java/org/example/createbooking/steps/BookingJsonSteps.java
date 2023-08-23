package org.example.createbooking.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import org.example.createbooking.cucumber.ServicesContext;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.junit.Assert.assertFalse;

public class BookingJsonSteps {

    private final RequestSpecBuilder requestSpec;
    Response response;
    private Integer secondId;
    private String token;
    private List<Object> bookingIds;

    public BookingJsonSteps(ServicesContext servicesContext) {
        requestSpec = new RequestSpecBuilder().setBaseUri("https://restful-booker.herokuapp.com/");
    }

    @Given("three bookings have been made")
    public void threeBookingsHaveBeenMade() {

    }

    @When("a user deletes the second booking")
    public void aUserDeletesTheSecondBooking() {
        response = given().spec(requestSpec.build())
                .get("booking");
        secondId = (Integer) ((HashMap) response.getBody().jsonPath().getList("$.").get(1)).get("bookingid");

        response = given().spec(requestSpec.build())
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .delete("booking/"+secondId);
    }

    @Then("the deleted booking is not in the list of bookings anymore")
    public void theDeletedBookingIsNotInTheListOfBookingsAnymore() {
        response = given().spec(requestSpec.build())
                .get("booking");
        boolean isSecondIdFound = false;

        bookingIds = (List<Object>) response.getBody().jsonPath().getList("$.");
        for (Object id: bookingIds) {
            HashMap<String, Integer> valueKey = (HashMap<String, Integer>) id;
            if(valueKey.get("bookingid")==secondId){
                isSecondIdFound = true;
                break;
            }
        }
        assertFalse(isSecondIdFound);
    }

    @Given("the user creates a token for user {string} with password {string}")
    public void theUserCreatesATokenForUserWithPassword(String username, String password) {
        String userDetails = String.format("{\n" +
                "    \"username\" : \"%1s\",\n" +
                "    \"password\" : \"%2s\"\n" +
                "}", username, password);
        response = given().spec(requestSpec.build())
                .contentType("application/json")
                .body(userDetails)
                .post("auth");

        token = response.getBody().jsonPath().getString("token");
    }
}
