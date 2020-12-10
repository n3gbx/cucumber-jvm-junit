package com.aqa.example.domain.page_objects.home.search;

import com.aqa.example.domain.page_elements.TabButton;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

@Name("Tabs container")
@FindBy(css = "hp-search-widget-tabs-container")
public class TabsContainer extends HtmlElement {

    @Name("'Flights' tab")
    @FindBy(xpath = ".//button[@aria-label='flights']")
    private TabButton flightsTab;

    @Name("'Car Hire' tab")
    @FindBy(xpath = ".//button[@aria-label='Car Hire']")
    private TabButton carHireTab;

    @Name("'Hotels' tab")
    @FindBy(xpath = ".//button[@aria-label='hotels']")
    private TabButton hotelsTab;

    private static final Logger LOGGER = LogManager.getLogger(TabsContainer.class);

    public void selectType(SearchType type) {
        LOGGER.info("Select {} search type", type.name());
        getTab(type).click();
    }

    public TabButton getTab(SearchType type) {
        switch (type) {
            case FLIGHTS:
                return flightsTab;
            case CAR_HIRE:
                return carHireTab;
            case HOTELS:
                return hotelsTab;
            default:
                throw new NotImplementedException(type + " type is not implemented");
        }
    }

    public enum SearchType {
        FLIGHTS, CAR_HIRE, HOTELS
    }
}
