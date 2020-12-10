package com.aqa.example.domain.page_objects.home.search;

import com.aqa.example.domain.enums.FlightDirectionType;
import com.aqa.example.domain.enums.FlightType;
import com.aqa.example.domain.page_elements.RadioButton;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.TextBlock;
import ru.yandex.qatools.htmlelements.element.TextInput;


@Name("Flights tab container")
@FindBy(css = "fsw-flight-search-widget.ng-tns-c74-3")
public class FlightsTabContainer extends HtmlElement {

    @Name("'One way' radiobutton")
    @FindBy(xpath = "//button[@aria-label='One way']")
    private RadioButton oneWayButton;

    @Name("'Return trip' radiobutton")
    @FindBy(xpath = "//button[@aria-label='Return trip']")
    private RadioButton returnTripButton;

    @Name("Departure input field")
    @FindBy(css = "input#input-button__departure")
    private TextInput departureInput;

    @Name("Destination input field")
    @FindBy(css = "input#input-button__destination")
    private TextInput destinationInput;

    @Name("Departure date field")
    @FindBy(xpath = "//div[@data-ref='input-button__dates-from']//div[@data-ref='input-button__display-value']")
    private TextBlock departureDateField;

    @Name("Return date field")
    @FindBy(xpath = "//div[@data-ref='input-button__dates-to']//div[@data-ref='input-button__display-value']")
    private TextBlock returnDateField;

    @Name("Passengers field")
    @FindBy(xpath = "//div[@data-ref='input-button__passengers']//div[@data-ref='input-button__display-value']")
    private TextBlock passengersField;

    @Name("'Search' button")
    @FindBy(xpath = ".//button[@aria-label='Search']")
    private Button searchButton;

    private CountriesPickerContainer countriesContainer;
    private DatePickerContainer datePickerContainer;
    private PassengersPickerContainer passengersPickerContainer;
    private static final Logger LOGGER = LogManager.getLogger(FlightsTabContainer.class);

    public void clickSearch() {
        LOGGER.debug("Click {}", searchButton.getName());
        searchButton.click();
    }

    public void selectTripType(FlightType type) {
        LOGGER.debug("Select {} trip type", type.name());
        switch (type) {
            case ONEWAY:
                oneWayButton.select();
                break;
            case RETURN:
                returnTripButton.select();
                break;
            default:
                throw new NotImplementedException(type + " type not implemented");
        }
    }

    public DatePickerContainer openDatePicker(FlightDirectionType type) {
        switch (type) {
            case ORIGIN:
                departureDateField.click();
                break;
            case DESTINATION:
                returnDateField.click();
                break;
            default:
                throw new NotImplementedException(type + " type not implemented");
        }
        return datePickerContainer;
    }

    public CountriesPickerContainer openCountriesPicker(FlightDirectionType type) {
        LOGGER.debug("Open {} countries picker", type.name());
        switch (type) {
            case ORIGIN:
                departureInput.click();
                break;
            case DESTINATION:
                destinationInput.click();
                break;
            default:
                throw new NotImplementedException(type + " type not implemented");
        }
        return countriesContainer;
    }

    public PassengersPickerContainer openPassengersPicker() {
        passengersField.click();
        return passengersPickerContainer;
    }
}
