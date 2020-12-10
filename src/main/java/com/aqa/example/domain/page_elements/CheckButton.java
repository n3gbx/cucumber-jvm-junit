package com.aqa.example.domain.page_elements;

import org.openqa.selenium.WebElement;

public class CheckButton extends SelectableButton {

    public CheckButton(WebElement wrappedElement) {
        super(wrappedElement);
    }

    public void deselect() {
        if (super.isSelected()) {
            getWrappedElement().click();
        }
    }
}
