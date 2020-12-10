package com.aqa.example.domain.page_objects.flight_payment;

import com.aqa.example.domain.page_elements.CheckButton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.TextInput;


@Name("Contact details container")
@FindBy(css = "div.contact-details")
public class ContactDetailsContainer extends HtmlElement {

    @Name("Subscriptions checkbox")
    @FindBy(css = "label[for='marketingOffers']")
    private CheckButton subscriptionsCheckBox;

    @Name("Phone number input")
    @FindBy(css = "ry-input-d[formcontrolname='phoneNumber'] input")
    private TextInput phoneNumberInput;

    private static final Logger LOGGER = LogManager.getLogger(ContactDetailsContainer.class);

    protected CheckButton getSubscriptionsCheckBox() {
        return subscriptionsCheckBox;
    }

    public void setPhoneNumber(String phoneNumber) {
        LOGGER.debug("Set '{}' to {}", phoneNumber, phoneNumberInput.getName());
        phoneNumberInput.sendKeys(phoneNumber);
    }
}
