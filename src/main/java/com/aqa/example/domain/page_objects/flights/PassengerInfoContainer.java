package com.aqa.example.domain.page_objects.flights;

import com.aqa.example.core.Constants;
import com.aqa.example.domain.enums.PassengerTitle;
import com.aqa.example.helpers.ElementsHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.TextBlock;
import ru.yandex.qatools.htmlelements.element.TextInput;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Name("Passenger info container")
@FindBy(xpath = "//pax-passenger")
public class PassengerInfoContainer extends HtmlElement {

    @Name("Passenger type")
    @FindBy(xpath = ".//span[contains(@class, 'passenger__type')]")
    private TextBlock typeText;

    @Name("Passenger title dropdown")
    @FindBy(xpath = ".//div[@class='dropdown b2']")
    private HtmlElement titleDropdown;

    @Name("Passenger title items")
    @FindBy(xpath = ".//div[@class='dropdown-item__label b2']")
    private List<TextBlock> titleItems;

    @Name("Passenger name input")
    @FindBy(xpath = ".//ry-input-d[@data-ref='pax-details__name']//input")
    private TextInput nameInput;

    @Name("Passenger surname input")
    @FindBy(xpath = ".//ry-input-d[@data-ref='pax-details__surname']//input")
    private TextInput surnameInput;

    @Name("Passenger date of birth input")
    @FindBy(xpath = ".//ry-input-d[@data-ref='pax-details__dob']//input")
    private TextInput dateOfBirthInput;

    private static final Logger LOGGER = LogManager.getLogger(PassengerInfoContainer.class);

    public String getType() {
        return typeText.getText();
    }

    public void setFirstName(final String name) {
        LOGGER.debug("Set passenger name: '{}'", name);
        nameInput.sendKeys(name);
    }

    public void setSurname(final String surname) {
        LOGGER.debug("Set passenger surname: '{}'", surname);
        surnameInput.sendKeys(surname);
    }

    public void setTitle(final PassengerTitle type) {
        LOGGER.debug("Set passenger title: '{}'", type.name());
        titleDropdown.click();
        getTitleItem(type).click();
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        LOGGER.debug("Set passenger date of birth: '{}'", dateOfBirth);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.INFANT_DATE_OF_BIRTH_FORMAT);
        String formattedDateOfBirth = dateOfBirth.format(formatter);
        dateOfBirthInput.sendKeys(formattedDateOfBirth);
    }

    private TextBlock getTitleItem(final PassengerTitle type) {
        return ElementsHelper.getExactFromListOrThrow(titleItems, type.name());
    }
}
