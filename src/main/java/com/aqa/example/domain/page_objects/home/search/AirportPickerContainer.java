package com.aqa.example.domain.page_objects.home.search;

import com.aqa.example.helpers.ElementsHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.TextBlock;

import java.util.List;

import static com.aqa.example.helpers.ElementsHelper.SELECTED;

@Name("Airport picker container")
@FindBy(xpath = "//fsw-airports-list")
public class AirportPickerContainer extends HtmlElement {

    @Name("Title")
    @FindBy(css = "div.list__header-title")
    private TextBlock titleText;

    @Name("Clear selection")
    @FindBy(css = "button.list__clear-selection")
    private Button clearSelectionButton;

    @Name("Clear selection")
    @FindBy(css = "span[data-ref='airport-item__name']")
    private List<TextBlock> airportsTextList;

    @Name("Airports scrollable container")
    @FindBy(css = "div.list__airports-scrollable-container")
    private HtmlElement airportsScrollableContainer;

    private static final Logger LOGGER = LogManager.getLogger(AirportPickerContainer.class);

    public void clearSelection() {
        LOGGER.debug("Clear selection");
        clearSelectionButton.click();
    }

    public void selectByFullName(String fullName) {
        LOGGER.debug("Select '{}' airport", fullName);
        TextBlock airportElement = ElementsHelper.getExactFromListOrThrow(airportsTextList, fullName);
        airportElement.click();
    }

    public void selectByShortName(String shortName) {
        LOGGER.debug("Select '{}' airport", shortName);
        String selector = String.format("span[data-id='%s']", shortName);
        ElementsHelper.executeClick(getWrappedElement().findElement(By.cssSelector(selector)));
    }

    public boolean isAirportSelected(String airport) {
        try {
            String selector = String.format("//span[contains(@class,'%s')]//*[text()=' %s ']", SELECTED, airport);
            getWrappedElement().findElement(By.xpath(selector));
            return true;
        } catch (NoSuchElementException ignore) {
            return false;
        }
    }
}
