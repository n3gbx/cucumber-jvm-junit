package com.aqa.example.definitions;

import com.aqa.example.core.DriverManager;
import com.aqa.example.domain.enums.PaymentInsuranceType;
import com.aqa.example.domain.models.CardDetails;
import com.aqa.example.domain.page_objects.flight_payment.FlightPaymentPage;
import com.aqa.example.domain.page_objects.trip_plan.TripPlanPage;
import com.github.javafaker.Faker;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

public class FlightPaymentSteps {
    private final WebDriver driver = DriverManager.getDriver();
    private TripPlanPage tripPlanPage;
    private FlightPaymentPage flightPaymentPage;

    @When("I set booking payment card details {string}, {string}, {string} and {string}")
    public void setBookingPaymentCardDetails(String number, String expDate, String cvv, String cardholder) {
        CardDetails card = new CardDetails(number, expDate, cvv, cardholder);

        tripPlanPage = new TripPlanPage(driver);
        tripPlanPage.waitForLoading();
        tripPlanPage.checkOut();

        flightPaymentPage = new FlightPaymentPage(driver);
        flightPaymentPage.waitForLoading();
        flightPaymentPage.setPhoneNumber(Faker.instance().phoneNumber().subscriberNumber(10), false);
        flightPaymentPage.setInsurance(PaymentInsuranceType.NO_INSURANCE);
        flightPaymentPage.setCardDetails(card);
    }

    @And("I set any billing details for booking payment")
    public void setAnyBookingPaymentBillingDetails() {
        String streetAddress = Faker.instance().address().streetAddress();
        String secondaryAddress = Faker.instance().address().secondaryAddress();

        flightPaymentPage.setBillingDetails(streetAddress, secondaryAddress, "Minsk", "Belarus", "222222");
        flightPaymentPage.setCurrency("EUR");
    }

    @Then("I submit payment and should get payment declined message")
    public void submitPaymentAndGetPaymentDeclinedMessage() {
        flightPaymentPage.submitPayment();

        Assert.assertTrue(flightPaymentPage.waitForPaymentErrorVisibility());
        Assert.assertTrue(flightPaymentPage.isOpened());
    }
}
