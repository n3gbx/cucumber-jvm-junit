package com.aqa.example.domain.page_objects.flight_payment;

import com.aqa.example.helpers.ElementsHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.TextBlock;
import ru.yandex.qatools.htmlelements.element.TextInput;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Name("Payment methods container")
@FindBy(tagName = "payment-methods")
public class PaymentMethodsContainer extends HtmlElement {

    @Name("'Credit card' tab")
    @FindBy(css = "span[data-ref='payment-methods__type-Card']")
    private HtmlElement creditCardTab;

    @Name("'PayPal' tab")
    @FindBy(css = "span[data-ref='payment-methods__type-PP']")
    private HtmlElement payPalTab;

    @Name("'Card number' input")
    @FindBy(css = "ry-input-d[data-ref='add-card-modal__account-number'] input")
    private TextInput cardNumberInput;

    @Name("'Card expiration month' dropdown")
    @FindBy(xpath = "//expiry-date//ry-dropdown[.//span[text()='Month']]")
    private HtmlElement cardExpMonthDropdown;

    @Name("'Card expiration year' dropdown")
    @FindBy(xpath = "//expiry-date//ry-dropdown[.//span[text()='Year']]")
    private HtmlElement cardExpYearDropdown;

    @Name("CVV")
    @FindBy(css = "verification-code input")
    private TextInput cvvInput;

    @Name("'Cardholder name' input")
    @FindBy(css = "ry-input-d[data-ref='add-card-modal__account-name'] input")
    private TextInput cardholderNameInput;

    @Name("'Address line 1' input")
    @FindBy(css = "ry-input-d[data-ref='add-card-modal__address-line-1'] input")
    private TextInput firstAddressLineInput;

    @Name("'Address line 2' input")
    @FindBy(css = "ry-input-d[data-ref='add-card-modal__address-line-2'] input")
    private TextInput secondAddressLineInput;

    @Name("'City' input")
    @FindBy(css = "ry-input-d[data-ref='add-card-modal__city'] input")
    private TextInput cityInput;

    @Name("'Country' input")
    @FindBy(css = "ry-autocomplete[formcontrolname='country'] input")
    private TextInput countryInput;

    @Name("'Postal code' input")
    @FindBy(css = "ry-input-d[formcontrolname='postcode'] input")
    private TextInput postalCodeInput;

    @Name("Suggested country text")
    @FindBy(css = "div._autocomplete_menu__item span b")
    private TextBlock suggestedCountryText;

    @Name("'Currency' dropdown")
    @FindBy(css = "ry-dropdown[formcontrolname='foreignCurrencyCode'] button.dropdown__toggle")
    private HtmlElement currencyDropdown;

    private static final Logger LOGGER = LogManager.getLogger(PaymentMethodsContainer.class);
    private static final String DROPDOWN_ITEM_SELECTOR = "//div[@class='dropdown-item__label b2']";

    public void setCardNumber(String number) {
        number = StringUtils.deleteWhitespace(number);
        LOGGER.debug("Set '{}' to {}", number, cardNumberInput.getName());
        cardNumberInput.sendKeys(number);
    }

    public void setCardExpiration(YearMonth expDate) {
        LOGGER.debug("Set '{}' expiration date", expDate.toString());

        int year = expDate.getYear();
        String month = expDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        month = StringUtils.capitalize(month);

        selectExpDatePartFromDropdown(cardExpMonthDropdown, StringUtils.capitalize(month));
        selectExpDatePartFromDropdown(cardExpYearDropdown, String.valueOf(year));
    }

    public void setSecurityCode(String cvv) {
        LOGGER.debug("Set '{}' to {}", cvv, cvvInput.getName());
        cvvInput.sendKeys(cvv);
    }

    public void setCardholderName(String name) {
        LOGGER.debug("Set '{}' to {}", name, cardholderNameInput.getName());
        cardholderNameInput.sendKeys(name);
    }

    public void setFirstAddressLine(String addressLine) {
        LOGGER.debug("Set '{}' to {}", addressLine, firstAddressLineInput.getName());
        firstAddressLineInput.sendKeys(addressLine);
    }

    public void setSecondAddressLine(String addressLine) {
        LOGGER.debug("Set '{}' to {}", addressLine, secondAddressLineInput.getName());
        secondAddressLineInput.sendKeys(addressLine);
    }

    public void setCity(String city) {
        LOGGER.debug("Set '{}' to {}", city, cityInput.getName());
        cityInput.sendKeys(city);
    }

    public void setCountry(String country) {
        LOGGER.debug("Set '{}' to {}", country, countryInput.getName());
        countryInput.sendKeys(country);
        suggestedCountryText.click();
    }

    public void setCountryPostalCode(String postalCode) {
        LOGGER.debug("Set '{}' to {}", postalCode, postalCodeInput.getName());
        postalCodeInput.sendKeys(postalCode);
    }

    public void setCurrency(String currency) {
        currency = StringUtils.upperCase(currency);
        LOGGER.debug("Set '{}' to {}", currency, currencyDropdown.getName());

        List<WebElement> items = currencyDropdown.findElements(By.xpath(DROPDOWN_ITEM_SELECTOR));
        ElementsHelper.executeClick(ElementsHelper.getPartFromListOrThrow(items, currency));
    }

    public void selectExpDatePartFromDropdown(HtmlElement dropdownElement, String text) {
        LOGGER.debug("Select '{}' from {}", text, dropdownElement.getName());

        List<WebElement> items = dropdownElement.findElements(By.xpath(DROPDOWN_ITEM_SELECTOR));
        ElementsHelper.executeClick(ElementsHelper.getExactFromListOrThrow(items, text));
    }
}
