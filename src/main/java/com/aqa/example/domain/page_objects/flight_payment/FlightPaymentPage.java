package com.aqa.example.domain.page_objects.flight_payment;

import com.aqa.example.domain.BasePage;
import com.aqa.example.domain.PageUrl;
import com.aqa.example.domain.enums.PaymentInsuranceType;
import com.aqa.example.domain.models.CardDetails;
import com.aqa.example.domain.page_elements.CheckButton;
import com.aqa.example.helpers.ElementsHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;


public class FlightPaymentPage extends BasePage {

    @Name("Payment container")
    @FindBy(tagName = "payment-form")
    private HtmlElement paymentContainer;

    @Name("Terms confirmation checkbox")
    @FindBy(css = "label[for='termsAndConditions']")
    private CheckButton termsCheckBox;

    @Name("'Pay' button")
    @FindBy(css = "pay-button button")
    private Button payButton;

    @Name("Payment error")
    @FindBy(css = "ry-alert[data-ref='payment-methods__error-message']")
    private HtmlElement paymentErrorContainer;

    private ContactDetailsContainer contactDetailsContainer;
    private InsuranceContainer insuranceContainer;
    private PaymentMethodsContainer paymentMethodsContainer;

    public FlightPaymentPage(WebDriver webDriver) {
        super(webDriver, "Flight payment");
    }

    @Override
    public boolean isOpened() {
        return waitForLoading() && getWebDriver().getCurrentUrl().contains(PageUrl.FLIGHT_PAYMENT_URL_PATH);
    }

    @Override
    public boolean waitForLoading() {
        return super.waitForLoading(ExpectedConditions.visibilityOf(paymentContainer), getScreenTimeout());
    }

    public void setPhoneNumber(String phoneNumber, boolean subscribe) {
        ElementsHelper.scrollTo(contactDetailsContainer.getWrappedElement());
        contactDetailsContainer.setPhoneNumber(phoneNumber);
        if (!subscribe) {
            contactDetailsContainer.getSubscriptionsCheckBox().deselect();
        }
    }

    public void setInsurance(PaymentInsuranceType type) {
        insuranceContainer.selectInsurance(type);
    }

    public void setCardDetails(CardDetails card) {
        paymentMethodsContainer.setCardNumber(card.getNumber());
        paymentMethodsContainer.setCardExpiration(card.getExpYearMonth());
        paymentMethodsContainer.setSecurityCode(card.getCvv());
        paymentMethodsContainer.setCardholderName(card.getCardholder());
    }

    public void setBillingDetails(String firstAddress, String secondAddress, String city, String country, String postalCode) {
        paymentMethodsContainer.setFirstAddressLine(firstAddress);
        paymentMethodsContainer.setSecondAddressLine(secondAddress);
        paymentMethodsContainer.setCity(city);
        paymentMethodsContainer.setCountry(country);
        paymentMethodsContainer.setCountryPostalCode(postalCode);
    }

    public void setCurrency(String currency) {
        paymentMethodsContainer.setCurrency(currency);
    }

    public void submitPayment() {
        LOGGER.debug("Submit payment");
        termsCheckBox.select();
        payButton.click();
    }

    public boolean waitForPaymentErrorVisibility() {
        ElementsHelper.getDefaultWait(getScreenTimeout()).until(ExpectedConditions.visibilityOf(paymentErrorContainer));
        return true;
    }
}
