package com.aqa.example.domain.page_objects.flight_payment;

import com.aqa.example.domain.enums.PaymentInsuranceType;
import com.aqa.example.domain.page_elements.RadioButton;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

@Name("Insurance container")
@FindBy(tagName = "insurance")
public class InsuranceContainer extends HtmlElement {

    @Name("Insurance Plus radio button")
    @FindBy(css = "label[for='insurance-opt-in']")
    private RadioButton insurancePlusRadioButton;

    @Name("No insurance radio button")
    @FindBy(css = "label[for='insurance-opt-out']")
    private RadioButton noInsuranceRadioButton;

    private static final Logger LOGGER = LogManager.getLogger(InsuranceContainer.class);

    public void selectInsurance(PaymentInsuranceType type) {
        LOGGER.debug("Select {} insurance type", type);
        switch (type) {
            case INSURANCE_PLUS:
                insurancePlusRadioButton.select();
                break;
            case NO_INSURANCE:
                noInsuranceRadioButton.select();
                break;
            default:
                throw new NotImplementedException(type + " type not implemented");
        }
    }
}
