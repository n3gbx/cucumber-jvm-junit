package com.aqa.example.domain.page_objects;

import com.aqa.example.domain.BasePage;
import com.aqa.example.helpers.ElementsHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

public abstract class BaseFlightBookingPage extends BasePage {

    @Name("'Basket' element")
    @FindBy(css = "div.header__right ry-basket-total")
    private HtmlElement basketContainer;

    @Name("'Check out' button")
    @FindBy(css = "ry-basket-continue-flow button")
    private Button checkOutButton;

    public BaseFlightBookingPage(WebDriver webDriver, String screenName) {
        super(webDriver, screenName);
    }

    public void expandBasket() {
        LOGGER.debug("Expand {}", basketContainer.getName());
        basketContainer.click();
    }

    public void clickCheckOut() {
        LOGGER.debug("Click {}", checkOutButton.getName());
        ElementsHelper.executeClick(checkOutButton);
    }

    public void checkOut() {
        expandBasket();
        clickCheckOut();
    }
}
