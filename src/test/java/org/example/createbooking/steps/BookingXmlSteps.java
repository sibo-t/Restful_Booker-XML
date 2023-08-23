package org.example.createbooking.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import org.example.createbooking.cucumber.ServicesContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BookingXmlSteps {
    private final RequestSpecBuilder requestSpec;
    Map<String, String> contact;
    private String token;
    private Response response;
    private String guestDetails;
    private String priceDetails;
    private String bookingDates;
    private String additionals;
    private List<Integer> bookingIds;
    ServicesContext servicesContext;

    public BookingXmlSteps(ServicesContext servicesContext) {
        requestSpec = new RequestSpecBuilder().setBaseUri("https://restful-booker.herokuapp.com/");
        this.servicesContext = servicesContext;
        bookingIds = servicesContext.getBookingIds();
    }

    @Given("the booking additional information {string}")
    public void theBookingAdditionalInformation(String additionalInfo) {
        additionals = String.format(
                "\t<additionalneeds>%s</additionalneeds>",
                additionalInfo);
    }

    @Given("the guest has a first name {string}, a last name {string}")
    public void theContactHasAFirstNameALastNameAndBirthdate(String name, String surname) {
        guestDetails = String.format(
                "\t<firstname>%1s</firstname>\n\t<lastname>%2s</lastname>",
        name, surname);
    }

    @Given("the guests total price is {string} and deposit paid is {string}")
    public void theContactHasAnEmailAddressAndCellphoneNumber(String totalPrice, String deposit) {

        priceDetails = String.format(
                "\t<totalprice>%1s</totalprice>\n\t<depositpaid>%2s</depositpaid>",
                totalPrice, deposit);
    }

    @Given("the guest check-in date is {string} and check-out {string} days away from today")
    public void theGuestCheckInAndCheckOutDate(String checkIn, String checkOut) {

        bookingDates = String.format("\t<bookingdates>\n" +
                "      <checkin>%1s</checkin>\n" +
                "      <checkout>%2s</checkout>\n" +
                "\t</bookingdates>", LocalDate.now().plusDays(Integer.parseInt(checkIn)).format(DateTimeFormatter.ofPattern(("yyyy-MM-dd"))),
                LocalDate.now().plusDays(Integer.parseInt(checkOut)).format(DateTimeFormatter.ofPattern(("yyyy-MM-dd"))));
    }

    @When("a user creates a booking using xml")
    public void theUserAddsANewContact() {

        String bookingBody = String.format("<booking>\n%1s\n%2s\n%3s\n%4s\n</booking>",
                guestDetails, priceDetails, bookingDates, additionals);


        response = given().spec(requestSpec.build())
                .config(RestAssured.config()
                        .encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
//                .contentType(ContentType.XML)
                .contentType("text/xml")
                .accept("application/xml")
                .body(bookingBody)
                .post("booking");

        bookingIds.add(response.getBody().xmlPath().get("$.bookingid"));
    }
}