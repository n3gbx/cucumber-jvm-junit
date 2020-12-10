package com.aqa.example.domain.page_elements;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.htmlelements.element.Button;

public class TabButton extends Button {

    public TabButton(WebElement wrappedElement) {
        super(wrappedElement);
    }

    public boolean isActive() {
        try {
            getWrappedElement().findElement(By.xpath("//*[@class='tab-active']"));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
