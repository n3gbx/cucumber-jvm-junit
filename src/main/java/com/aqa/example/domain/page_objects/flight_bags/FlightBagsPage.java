package com.aqa.example.domain.page_objects.flight_bags;

import com.aqa.example.domain.PageUrl;
import com.aqa.example.domain.enums.FlightBagType;
import com.aqa.example.domain.page_elements.RadioButton;
import com.aqa.example.domain.page_objects.BaseFlightBookingPage;
import com.aqa.example.helpers.ElementsHelper;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

import java.util.List;

public class FlightBagsPage extends BaseFlightBookingPage {

    @Name("Header text")
    @FindBy(id = "bag-header-1")
    private HtmlElement headerText;

    @Name("Small bag radio button")
    @FindBy(xpath = "//div[@data-ref='product.small-bag']//ry-radio-circle-button")
    private List<RadioButton> smallBagRadioButtons;

    @Name("Priority 2 cabin bags radio button")
    @FindBy(xpath = "//div[@data-ref='product.priority-boarding']//ry-radio-circle-button")
    private List<RadioButton> cabinBagRadioButtons;

    @Name("'Continue' button")
    @FindBy(xpath = "//bags-continue-flow//button")
    private HtmlElement continueButton;

    public FlightBagsPage(WebDriver webDriver) {
        super(webDriver, "Flight bags");
    }

    @Override
    public boolean isOpened() {
        return waitForLoading() && hasUrl(PageUrl.FLIGHT_BAGS_URL_PATH);
    }

    @Override
    public boolean waitForLoading() {
        return super.waitForLoading(ExpectedConditions.visibilityOf(headerText), getScreenTimeout());
    }

    public void continueBookingFlow() {
        LOGGER.debug("Click {}", continueButton.getName());
        ElementsHelper.scrollTo(continueButton);
        continueButton.click();
    }

    public void selectForAll(FlightBagType type) {
        LOGGER.debug("Select {} bag type for all passengers", type.name());
        switch (type) {
            case SMALL:
                smallBagRadioButtons.get(0).select();
                break;
            case CABIN:
                cabinBagRadioButtons.get(0).select();
                break;
            default:
                throw new NotImplementedException(type + " type not implemented");
        }
    }
}
