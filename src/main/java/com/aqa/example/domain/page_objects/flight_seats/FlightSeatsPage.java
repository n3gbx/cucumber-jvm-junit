package com.aqa.example.domain.page_objects.flight_seats;

import com.aqa.example.domain.PageUrl;
import com.aqa.example.domain.page_objects.BaseFlightBookingPage;
import com.aqa.example.domain.page_objects.dialogs.InfoDialog;
import com.aqa.example.environment.ConfigManager;
import com.aqa.example.helpers.ElementsHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FlightSeatsPage extends BaseFlightBookingPage {

    @Name("Header text")
    @FindBy(xpath = ".//h1[contains(@class, 'seats-page-title')]")
    private HtmlElement headerText;

    @Name("Seat row")
    @FindBy(css = "div.seatmap__seats")
    private List<HtmlElement> seatRows;

    @Name("Passenger rows")
    @FindBy(css = "div.passenger-carousel__container div.ry-vertical-tabs__menu-item")
    private List<HtmlElement> passengerRows;

    @Name("'Continue' button")
    @FindBy(css = "button[data-ref='seats-action__button-continue']")
    private HtmlElement continueButton;

    private InfoDialog familySeatingDialog;
    private FastTrackDialog fastTrackDialog;
    private static final int MAX_SEATS_PER_ROW = 6;

    public FlightSeatsPage(WebDriver webDriver) {
        super(webDriver, "Flight Seats");
    }

    @Override
    public boolean isOpened() {
        return waitForLoading() && hasUrl(PageUrl.FLIGHT_SEATS_URL_PATH);
    }

    @Override
    public boolean waitForLoading() {
        return super.waitForLoading(ExpectedConditions.visibilityOf(headerText), getScreenTimeout());
    }

    public void continueBookingFlow() {
        LOGGER.debug("Click {}", continueButton.getName());
        continueButton.click();
    }

    public void acceptFamilySeatingDialog() {
        LOGGER.debug("Accept {}", familySeatingDialog.getName());
        familySeatingDialog.accept();
    }

    public void discardFastTrackDialog() {
        LOGGER.debug("Decline {}", fastTrackDialog.getName());
        ElementsHelper.getDefaultWait(getScreenTimeout()).until(webDriver -> fastTrackDialog.exists());
        fastTrackDialog.discard();
    }

    // Selects the seats sequentially for all the passengers
    // The logic filters all airplane rows to find a row where we can put all the passengers
    // If the number of passengers exceeds the number of seats in a row,
    // the findSequentialSeats() function recursively finds another row for the rest passengers
    // Attention: this method does not take into account the fact that infants should be put next to the window
    public void setSeatsForAllSequentially() {
        final int passengersCount = passengerRows.size();

        List<WebElement> freeSeats = findSequentialSeats(passengersCount);

        // select the seats sequentially for all passengers
        IntStream.range(0, passengersCount).forEach(i -> {
            LOGGER.debug("Select {} passenger's seat", i);
            WebElement seat = freeSeats.get(i);

            ElementsHelper.scrollToCenter(seat);
            ElementsHelper.executeClick(seat);
            ElementsHelper.getDefaultWait(ConfigManager.getGeneralConfig().elementTimeout())
                    .until(webDriver -> seat.getAttribute("class").endsWith("seat--selected"));
        });
    }

    private List<WebElement> findSequentialSeats(int passengersCount) {
        LOGGER.debug("Finding sequential seats for {} passenger(s)", passengersCount);
        List<WebElement> freeSeqSeats = new ArrayList<>();

        seatRows.stream().filter(row -> {
            LinkedHashMap<Integer, WebElement> freeSeatsIndicesMap = getRowFreeSeatsIndicesMap(row);

            if (freeSeatsIndicesMap.size() < passengersCount) {
                return false;
            }

            int[] seatsIndices = freeSeatsIndicesMap.keySet().stream().mapToInt(Integer::intValue).toArray();
            Set<Integer> seqIndicesGroup = getLongestSeqIndicesGroup(seatsIndices);

            if (seqIndicesGroup.size() >= passengersCount) {
                LOGGER.debug("Found a {} with {} free seats indices", row.getName(), seatsIndices);
                seqIndicesGroup.forEach(i -> freeSeqSeats.add(freeSeatsIndicesMap.get(i)));
                return true;
            }
            return false;
        }).findFirst().orElseThrow(() -> {
            return new NoSuchElementException("Can't find free sequential seats for " + passengersCount + " passengers");
        });

        // in case when passengers count > max seats per one row
        if (MAX_SEATS_PER_ROW < passengersCount) {
            freeSeqSeats.addAll(findSequentialSeats((passengersCount - MAX_SEATS_PER_ROW)));
        }

        return freeSeqSeats;
    }

    private LinkedHashMap<Integer, WebElement> getRowFreeSeatsIndicesMap(HtmlElement row) {
        LinkedHashMap<Integer, WebElement> setIndicesMap = new LinkedHashMap<>();
        // all seats including unavailable
        List<WebElement> rowSeats = row.findElements(By.xpath("./*[not(contains(@class, 'seatmap__seat--aisle'))]"));

        // get indices for free seats only
        for (int i = 0; i < rowSeats.size(); i++) {
            WebElement seat = rowSeats.get(i);
            if (seat.getAttribute("class").endsWith("--standard")) {
                setIndicesMap.put(i, seat);
            }
        }
        return setIndicesMap;
    }

    private Set<Integer> getLongestSeqIndicesGroup(int[] indices) {
        Set<Integer> longest = new HashSet<>();
        Set<Integer> current = new HashSet<>();

        for (int i = 1; i < indices.length; i++) {
            if (indices[i] != indices[i - 1]) {
                if (indices[i] - indices[i - 1] == 1) {
                    current.add(indices[i - 1]);
                    current.add(indices[i]);
                } else {
                    longest = Stream.of(current, longest).max(Comparator.comparingInt(Set::size)).get();
                    current.clear();
                }
            }
            if (i == indices.length - 1 && longest.size() == 0) {
                longest = current;
            }
        }
        return longest;
    }
}
