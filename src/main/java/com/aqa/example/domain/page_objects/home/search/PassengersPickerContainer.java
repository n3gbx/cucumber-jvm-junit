package com.aqa.example.domain.page_objects.home.search;

import com.aqa.example.domain.enums.PassengerType;
import com.aqa.example.domain.page_elements.CounterElement;
import com.aqa.example.domain.page_objects.dialogs.InfoDialog;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

@Name("Passengers picker container")
@FindBy(xpath = "//fsw-passengers")
public class PassengersPickerContainer extends HtmlElement {

    @Name("Adults counter")
    @FindBy(css = "ry-counter[data-ref='passengers-picker__adults']")
    private CounterElement adultsCounter;

    @Name("Teens counter")
    @FindBy(css = "ry-counter[data-ref='passengers-picker__teens']")
    private CounterElement teensCounter;

    @Name("Children counter")
    @FindBy(css = "ry-counter[data-ref='passengers-picker__children']")
    private CounterElement childrenCounter;

    @Name("Infant counter")
    @FindBy(css = "ry-counter[data-ref='passengers-picker__infant']")
    private CounterElement infantCounter;

    @Name("Done button")
    @FindBy(css = "button.passengers__confirm-button")
    private Button doneButton;

    private InfoDialog infantDialog;
    private static final Logger LOGGER = LogManager.getLogger(PassengersPickerContainer.class);

    public void setSeatsCount(PassengerType type, int seatsCount) {
        LOGGER.debug("Set {} {}", seatsCount, type.name());
        if (type.equals(PassengerType.INFANT)) {
            getPassengerCounterElement(type).increment();
            LOGGER.debug("Accept {}", infantDialog.getName());
            infantDialog.accept();
        }
        getPassengerCounterElement(type).setValue(seatsCount);
    }

    private CounterElement getPassengerCounterElement(PassengerType type) {
        switch (type) {
            case ADULT:
                return adultsCounter;
            case TEEN:
                return teensCounter;
            case CHILD:
                return childrenCounter;
            case INFANT:
                return infantCounter;
            default:
                throw new NotImplementedException(type + " type not implemented");
        }
    }
}
