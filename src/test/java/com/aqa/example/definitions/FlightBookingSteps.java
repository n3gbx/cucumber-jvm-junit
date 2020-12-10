package com.aqa.example.definitions;

import com.aqa.example.core.DriverManager;
import com.aqa.example.domain.enums.*;
import com.aqa.example.domain.page_objects.flight_bags.FlightBagsPage;
import com.aqa.example.domain.page_objects.flight_extras.FlightExtrasPage;
import com.aqa.example.domain.page_objects.flights.FlightsPage;
import com.aqa.example.domain.page_objects.home.HomePage;
import com.aqa.example.domain.page_objects.home.search.TabsContainer;
import com.aqa.example.domain.page_objects.flight_payment.FlightPaymentPage;
import com.aqa.example.domain.page_objects.flight_seats.FlightSeatsPage;
import com.aqa.example.domain.page_objects.trip_plan.TripPlanPage;
import io.cucumber.java.en.Given;
import org.openqa.selenium.WebDriver;

import java.util.Map;
import java.util.stream.IntStream;


public class FlightBookingSteps {
    private final WebDriver driver = DriverManager.getDriver();
    private HomePage homePage;
    private FlightsPage flightsPage;
    private FlightSeatsPage flightSeatsPage;
    private FlightBagsPage flightBagsPage;
    private FlightExtrasPage flightExtrasPage;
    private TripPlanPage tripPlanPage;
    private FlightPaymentPage flightPaymentPage;

    @Given("I make a booking from {string} to {string} airports on {string} for following passengers count")
    public void makeFlightBookingFromOriginToDestOnDateForPassengers(String fromAirport,
                                                                     String toAirport,
                                                                     String date,
                                                                     Map<PassengerType, Integer> passengers) {
        homePage = new HomePage(driver);
        homePage.selectTab(TabsContainer.SearchType.FLIGHTS);
        homePage.selectTripType(FlightType.ONEWAY);
        homePage.selectAirport(FlightDirectionType.ORIGIN, fromAirport, true);
        homePage.selectAirport(FlightDirectionType.DESTINATION, toAirport, false);
        homePage.selectDate(FlightDirectionType.ORIGIN, date);
        passengers.forEach((passengerType, seatsCount) -> {
            homePage.setPassengerCount(passengerType, seatsCount);
        });
        homePage.searchFlight();

        flightsPage = new FlightsPage(driver);
        flightsPage.waitForLoading();
        flightsPage.selectDefaultDateFlight(1);
        flightsPage.selectFare(FlightFareType.VALUE);
        passengers.forEach((passengerType, count) -> {
            IntStream.rangeClosed(1, count).forEach(order -> flightsPage.setAnyPassengerInfo(passengerType, order));
        });
        flightsPage.continueBookingFlow();

        flightSeatsPage = new FlightSeatsPage(driver);
        flightSeatsPage.waitForLoading();

        if (passengers.containsKey(PassengerType.CHILD) || passengers.containsKey(PassengerType.INFANT)) {
            flightSeatsPage.acceptFamilySeatingDialog();
        }

        flightSeatsPage.setSeatsForAllSequentially();
        flightSeatsPage.continueBookingFlow();
        flightSeatsPage.discardFastTrackDialog();

        flightBagsPage = new FlightBagsPage(driver);
        flightBagsPage.waitForLoading();
        flightBagsPage.selectForAll(FlightBagType.SMALL);
        flightBagsPage.continueBookingFlow();

        flightExtrasPage = new FlightExtrasPage(driver);
        flightExtrasPage.waitForLoading();
        flightExtrasPage.continueBookingFlow();

        tripPlanPage = new TripPlanPage(driver);
        tripPlanPage.waitForLoading();
    }
}
