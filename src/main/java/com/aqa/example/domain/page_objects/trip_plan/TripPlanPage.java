package com.aqa.example.domain.page_objects.trip_plan;

import com.aqa.example.domain.PageUrl;
import com.aqa.example.domain.page_objects.BaseFlightBookingPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.TextBlock;

public class TripPlanPage extends BaseFlightBookingPage {

    @Name("Header text")
    @FindBy(css = "trip-section-header h3")
    private TextBlock headerText;

    public TripPlanPage(WebDriver webDriver) {
        super(webDriver, "Plan your trip");
    }

    @Override
    public boolean isOpened() {
        return waitForLoading() && hasUrl(PageUrl.TRIP_PLAN_URL_PATH);
    }

    @Override
    public boolean waitForLoading() {
        return super.waitForLoading(ExpectedConditions.visibilityOf(headerText), getScreenTimeout());
    }
}
