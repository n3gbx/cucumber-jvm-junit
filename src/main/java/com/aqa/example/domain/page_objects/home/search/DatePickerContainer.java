package com.aqa.example.domain.page_objects.home.search;

import com.aqa.example.core.Constants;
import com.aqa.example.environment.ConfigManager;
import com.aqa.example.helpers.ElementsHelper;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.TextBlock;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

@Name("Date picker")
@FindBy(xpath = "//fsw-datepicker-container")
public class DatePickerContainer extends HtmlElement {

    @Name("Next month button")
    @FindBy(css = "div[data-ref='calendar-btn-next-month']")
    private Button nextMonthButton;

    @Name("Previous month button")
    @FindBy(css = "div[data-ref='calendar-btn-prev-month']")
    private Button prevMonthButton;

    @Name("Left month title")
    @FindBy(xpath = "(//div[@data-ref='calendar-month-name'])[1]")
    private TextBlock leftMonthTitle;

    @Name("Right month title")
    @FindBy(xpath = "(//div[@data-ref='calendar-month-name'])[2]")
    private TextBlock rightMonthTitle;

    private static final Logger LOGGER = LogManager.getLogger(DatePickerContainer.class);

    public void waitForLoading() {
        ElementsHelper.getDefaultWait(ConfigManager.getGeneralConfig().elementTimeout())
                .until(ExpectedConditions.visibilityOf(leftMonthTitle));
    }

    // Automatically scrolls the calendar to find the given date.
    // Checks whether the calendar pair contains desired date or not
    // bases on the last date in the second calendar
    public void selectDate(String date) {
        LOGGER.debug("Select {} date", date);

        LocalDate parsedDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.FLIGHT_DATE_FORMAT, Locale.ENGLISH);
            parsedDate = LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new DateTimeException(date + " should be of '" + Constants.FLIGHT_DATE_FORMAT + "' format");
        }

        if (parsedDate.isBefore(LocalDate.now())) {
            throw new DateTimeException(date + " should not be in past");
        }

        LocalDate rightCalendarDate = getCalendarLastDay(CalendarType.RIGHT);

        if (parsedDate.isAfter(rightCalendarDate)) {
            LOGGER.debug("{} date is after {}", parsedDate, rightCalendarDate);
            int monthsDiff = (int) ChronoUnit.MONTHS.between(rightCalendarDate, parsedDate);

            IntStream.rangeClosed(0, monthsDiff).forEach(i -> {
                LOGGER.debug("Scroll to the next month");
                nextMonthButton.click();
            });
        }

        LOGGER.debug("Select {}", date);

        String selector = String.format(".//div[@data-id='%s']", date);
        WebElement desiredDateElement = getWrappedElement().findElement(By.xpath(selector));
        desiredDateElement.click();
    }

    private LocalDate getCalendarLastDay(CalendarType type) {
        WebElement dayElement = getCalendarElement(type).findElement(By.xpath("(//div[contains(@class, 'calendar-body__cell')])[last()]"));
        String date = dayElement.getAttribute("data-id");
        return LocalDate.parse(date);
    }

    private WebElement getCalendarElement(CalendarType type) {
        List<WebElement> calendars = getWrappedElement().findElements(By.xpath("//div[@data-ref='calendar-month-name']"));
        switch (type) {
            case LEFT:
                return calendars.get(0);
            case RIGHT:
                return calendars.get(1);
            default:
                throw new NotImplementedException(type + " type is not implemented");
        }
    }

    private enum CalendarType {
        LEFT, RIGHT
    }
}
