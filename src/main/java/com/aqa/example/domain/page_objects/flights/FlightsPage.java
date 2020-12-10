package com.aqa.example.domain.page_objects.flights;

import com.aqa.example.core.DriverManager;
import com.aqa.example.domain.PageUrl;
import com.aqa.example.domain.enums.FlightFareType;
import com.aqa.example.domain.enums.PassengerTitle;
import com.aqa.example.domain.enums.PassengerType;
import com.aqa.example.domain.page_objects.BaseFlightBookingPage;
import com.aqa.example.environment.ConfigManager;
import com.aqa.example.helpers.ElementsHelper;
import com.github.javafaker.Faker;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.loader.HtmlElementLoader;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FlightsPage extends BaseFlightBookingPage {

    @Name("Header text")
    @FindBy(css = "div.details__header")
    private HtmlElement headerDetailsText;

    @Name("Flights cards")
    @FindBy(xpath = "//flight-list//div[@class='card-wrapper']")
    private List<HtmlElement> flightsCardsList;

    @Name("Fare cards")
    @FindBy(xpath = "//div[@class='fare-card-container']//div[contains(@class, 'fare-card-item')]")
    private List<HtmlElement> fareCardsList;

    @Name("'Value' fare card")
    @FindBy(xpath = "//div[@data-e2e='fare-card--standard']")
    private HtmlElement valueFareCard;

    @Name("'Regular' fare card")
    @FindBy(xpath = "//div[@data-e2e='fare-card--regular']")
    private HtmlElement regularFareCard;

    @Name("'Plus' fare card")
    @FindBy(xpath = "//div[@data-e2e='fare-card--plus']")
    private HtmlElement plusFareCard;

    @Name("'Flexi Plus' fare card")
    @FindBy(xpath = "//div[@data-e2e='fare-card--flexi']")
    private HtmlElement flexiPlusFareCard;

    @Name("'Passengers' form")
    @FindBy(xpath = "//div[contains(@class, 'form-wrapper')]//*[text()='Passengers']")
    private HtmlElement passengersFormContainer;

    @Name("'Continue' button")
    @FindBy(css = "div.continue-flow__container button")
    private Button continueButton;

    public FlightsPage(WebDriver webDriver) {
        super(webDriver, "Flights");
    }

    @Override
    public boolean isOpened() {
        return waitForLoading() && hasUrl(PageUrl.FLIGHTS_SELECT_URL_PATH);
    }

    @Override
    public boolean waitForLoading() {
        return super.waitForLoading(ExpectedConditions.visibilityOf(headerDetailsText), getScreenTimeout());
    }

    public void continueBookingFlow() {
        LOGGER.debug("Click {}", continueButton.getName());
        continueButton.click();
    }

    public void selectDefaultDateFlight(final int position) {
        LOGGER.debug("Select flight at {} position", position);

        int totalFlights = flightsCardsList.size();

        if (totalFlights >= position) {
            flightsCardsList.get(position - 1).click();
        } else {
            throw new IllegalArgumentException("Can not select flight at " + position + " position, total: " + totalFlights);
        }
    }

    public void selectFare(FlightFareType type) {
        LOGGER.debug("Select {} fare", type.name());

        WebElement fareElement = getFareElement(type);
        ElementsHelper.getDefaultWait(ConfigManager.getGeneralConfig().elementTimeout())
                .until(ExpectedConditions.elementToBeClickable(fareElement))
                .click();
        ElementsHelper.getDefaultWait(ConfigManager.getGeneralConfig().elementTimeout())
                .until(ExpectedConditions.visibilityOf(passengersFormContainer));
    }

    public PassengerInfoContainer setAnyPassengerInfo(PassengerType type, int order) {
        String anyName = Faker.instance().name().firstName();
        String anySurname = Faker.instance().name().lastName();

        PassengerInfoContainer passengerInfo = setPassengerInfo(type, anyName, anySurname, order);

        if (type.equals(PassengerType.INFANT)) {
            // Date of birth cannot be less than 8 days and cannot exceed 2 years compared to the scheduled date of flight
            passengerInfo.setDateOfBirth(LocalDate.of(2020, 1, 1));
        } else if (type.equals(PassengerType.ADULT) || type.equals(PassengerType.TEEN)) {
            List<PassengerTitle> passengerTitles = Arrays.asList(PassengerTitle.values());
            if (type.equals(PassengerType.TEEN)) {
                passengerTitles = Arrays.asList(PassengerTitle.MR, PassengerTitle.MS);
            }
            Collections.shuffle(passengerTitles);

            passengerInfo.setTitle(passengerTitles.get(0));
        }
        return passengerInfo;
    }

    public PassengerInfoContainer setPassengerInfo(PassengerType type, PassengerTitle title, String name, String surname, int order) {
        PassengerInfoContainer passengerInfoContainer = setPassengerInfo(type, name, surname, order);
        passengerInfoContainer.setTitle(title);
        return passengerInfoContainer;
    }

    public PassengerInfoContainer setPassengerInfo(PassengerType type, String name, String surname, LocalDate dateOfBirth, int order) {
        PassengerInfoContainer passengerInfoContainer = setPassengerInfo(type, name, surname, order);
        passengerInfoContainer.setDateOfBirth(dateOfBirth);
        return passengerInfoContainer;
    }

    public PassengerInfoContainer setPassengerInfo(PassengerType type, String name, String surname, int order) {
        LOGGER.debug("Setting info to {} {} passenger", order, type.name());
        PassengerInfoContainer passengerInfoContainer = getPassengerInfoContainer(type, order);
        passengerInfoContainer.setFirstName(name);
        passengerInfoContainer.setSurname(surname);
        return passengerInfoContainer;
    }

    private PassengerInfoContainer getPassengerInfoContainer(PassengerType type, int order) {
        String capitalisedType = StringUtils.capitalize(type.name().toLowerCase());

        try {
            String selector = String.format("(//pax-passenger[.//span[contains(text(), '%s')]])[%s]", capitalisedType, order);
            String elementName = String.format("%s %s passenger info", order, capitalisedType);

            WebElement elementToWrap = DriverManager.getDriver().findElement(By.xpath(selector));
            PassengerInfoContainer passengerInfo = HtmlElementLoader.createHtmlElement(PassengerInfoContainer.class, elementToWrap, elementName);
            ElementsHelper.scrollTo(passengerInfo);

            return passengerInfo;
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not find " + type.name() + " passenger info at " + order + " position");
        }
    }

    private HtmlElement getFareElement(FlightFareType type) {
        switch (type) {
            case VALUE:
                return valueFareCard;
            case REGULAR:
                return regularFareCard;
            case PLUS:
                return plusFareCard;
            case FLEXI_PLUS:
                return flexiPlusFareCard;
            default:
                throw new NotImplementedException(type + " type not implemented");
        }
    }
}
