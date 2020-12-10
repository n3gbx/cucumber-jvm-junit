package com.aqa.example.domain.page_objects.home;

import com.aqa.example.domain.BasePage;
import com.aqa.example.domain.PageUrl;
import com.aqa.example.domain.enums.FlightDirectionType;
import com.aqa.example.domain.enums.FlightType;
import com.aqa.example.domain.enums.PassengerType;
import com.aqa.example.domain.page_objects.dialogs.AuthDialog;
import com.aqa.example.domain.page_objects.home.search.*;
import com.aqa.example.environment.ConfigManager;
import com.aqa.example.helpers.ElementsHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.loader.HtmlElementLoader;

public class HomePage extends BasePage {

    @Name("Log in button")
    @FindBy(xpath = "//button[@aria-label='Log in']")
    private Button loginButton;

    @Name("User name")
    @FindBy(xpath = "//span[@class='ry-header__user-name']")
    private Button userNameButton;

    private TabsContainer tabsContainer;
    private FlightsTabContainer flightsTabContainer;

    public HomePage(WebDriver webDriver) {
        super(webDriver, "Home");
    }

    @Override
    public boolean isOpened() {
        return waitForLoading() && hasUrl(PageUrl.BASE_URL);
    }

    public void logIn(String email, String password) {
        loginButton.click();
        AuthDialog logInDialog = HtmlElementLoader.createHtmlElement(AuthDialog.class, getWebDriver());
        logInDialog.waitForOpened();
        logInDialog.setEmail(email);
        logInDialog.setPassword(password);
        logInDialog.submit();

        ElementsHelper.getDefaultWait(ConfigManager.getGeneralConfig().elementTimeout())
                .until(ExpectedConditions.elementToBeClickable(userNameButton.getWrappedElement()));
    }

    public void selectTab(TabsContainer.SearchType type) {
        if (!tabsContainer.getTab(type).isActive()) {
            tabsContainer.selectType(type);
        }
    }

    public void selectTripType(FlightType type) {
        flightsTabContainer.selectTripType(type);
    }

    public void selectAirport(FlightDirectionType direction, String abbreviation, boolean clear) {
        CountriesPickerContainer countriesContainer = flightsTabContainer.openCountriesPicker(direction);
        countriesContainer.waitForLoading();
        if (clear) countriesContainer.clearSelection();
        countriesContainer.selectAirportByShortName(abbreviation);
    }

    public void selectDate(FlightDirectionType type, String date) {
        DatePickerContainer datePickerContainer = flightsTabContainer.openDatePicker(type);
        datePickerContainer.waitForLoading();
        datePickerContainer.selectDate(date);
    }

    public void setPassengerCount(PassengerType type, int count) {
        PassengersPickerContainer passengersPickerContainer = flightsTabContainer.openPassengersPicker();
        passengersPickerContainer.setSeatsCount(type, count);
    }

    public void searchFlight() {
        flightsTabContainer.clickSearch();
    }
}
