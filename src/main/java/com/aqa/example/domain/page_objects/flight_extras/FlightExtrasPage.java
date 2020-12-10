package com.aqa.example.domain.page_objects.flight_extras;

import com.aqa.example.domain.PageUrl;
import com.aqa.example.domain.page_objects.BaseFlightBookingPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

import java.util.List;

public class FlightExtrasPage extends BaseFlightBookingPage {

    @Name("Products container")
    @FindBy(xpath = "//div[contains(@class, 'products-list__container')]")
    private HtmlElement productsContainer;

    @Name("Product cards")
    @FindBy(css = "product-card-container.product-card")
    private List<HtmlElement> productCards;

    @Name("'Continue' button")
    @FindBy(css = "div.products-list__button-wrapper button")
    private Button continueButton;

    public FlightExtrasPage(WebDriver webDriver) {
        super(webDriver, "Flight extras");
    }

    @Override
    public boolean isOpened() {
        return waitForLoading() && hasUrl(PageUrl.FLIGHT_EXTRAS_URL_PATH);
    }

    @Override
    public boolean waitForLoading() {
        return super.waitForLoading(ExpectedConditions.visibilityOf(productsContainer), getScreenTimeout());
    }

    public void continueBookingFlow() {
        LOGGER.debug("Click {}", continueButton.getName());
        continueButton.click();
    }
}
