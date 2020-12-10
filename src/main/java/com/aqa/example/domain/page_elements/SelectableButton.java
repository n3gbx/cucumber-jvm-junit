package com.aqa.example.domain.page_elements;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.aqa.example.helpers.ElementsHelper.CHECKED;
import static com.aqa.example.helpers.ElementsHelper.SELECTED;

public abstract class SelectableButton extends Button {

    public SelectableButton(WebElement wrappedElement) {
        super(wrappedElement);
    }

    public void select() {
        if (!this.isSelected()) {
            getWrappedElement().click();
        }
    }

    @Override
    public boolean isSelected() {
        try {
            String selector = String.format(".//*[contains(@class,'%s') or contains(@class,'%s')]", SELECTED, CHECKED);
            getWrappedElement().findElement(By.xpath(selector));
            return true;
        } catch (NoSuchElementException ignore) {
            return false;
        }
    }

    @Override
    public String getText() {
        try {
            return getWrappedElement().findElement(By.xpath("following-sibling::label")).getText();
        } catch (NoSuchElementException ignore) {
            return "";
        }
    }
}
