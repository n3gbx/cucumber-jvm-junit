package com.aqa.example.domain.page_objects.home.search;

import com.aqa.example.environment.ConfigManager;
import com.aqa.example.helpers.ElementsHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.TextBlock;

import java.util.List;

import static com.aqa.example.helpers.ElementsHelper.SELECTED;

@Name("Countries container")
@FindBy(xpath = "//fsw-airports")
public class CountriesPickerContainer extends HtmlElement {

    @Name("Title")
    @FindBy(css = "div.h4.countries__title")
    private TextBlock titleText;

    @Name("Countries")
    @FindBy(css = "span[data-ref='country__name']")
    private List<HtmlElement> countriesTextList;

    private AirportPickerContainer airportPickerContainer;
    private static final Logger LOGGER = LogManager.getLogger(CountriesPickerContainer.class);

    public void waitForLoading() {
        ElementsHelper.getDefaultWait(ConfigManager.getGeneralConfig().elementTimeout())
                .until(ExpectedConditions.visibilityOf(titleText));
    }

    public void selectCountry(String country) {
        LOGGER.debug("Select '{}' country", country);
        HtmlElement airportElement = ElementsHelper.getExactFromListOrThrow(countriesTextList, country);
        airportElement.click();
    }

    public void selectAirportByFullName(String airport) {
        airportPickerContainer.selectByFullName(airport);
    }

    public void selectAirportByShortName(String abbreviation) {
        airportPickerContainer.selectByShortName(abbreviation);
    }

    public void clearSelection() {
        airportPickerContainer.clearSelection();
    }

    public boolean isCountrySelected(String country) {
        try {
            HtmlElement cityElement = ElementsHelper.getExactFromListOrThrow(countriesTextList, country);
            return cityElement.getAttribute("class").contains(SELECTED);
        } catch (NoSuchElementException ignore) {
            return false;
        }
    }
}
