package com.aqa.example.domain.page_elements;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.htmlelements.element.Button;

import java.util.stream.IntStream;

public class CounterElement extends Button {

    public CounterElement(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public String getText() {
        try {
            WebElement element = this.getWrappedElement().findElement(By.xpath(".//div[@class='counter__value']"));
            return element.getText().trim();
        } catch (NoSuchElementException ignore) {
            return "0";
        }
    }

    public void setValue(int value) {
        int current = Integer.parseInt(getText());
        if (current > value) {
            IntStream.range(0, current - value).forEach(i -> decrement());
        } else if (current < value) {
            IntStream.range(0, value - current).forEach(i -> increment());
        }
    }

    public void increment() {
        WebElement incrementButton = getIncrementButton();
        incrementButton.click();
    }

    public void decrement() {
        WebElement decrementButton = getDecrementButton();
        decrementButton.click();
    }

    private WebElement getIncrementButton() {
        return getWrappedElement().findElement(By.cssSelector("div[data-ref='counter.counter__increment']"));
    }

    private WebElement getDecrementButton() {
        return getWrappedElement().findElement(By.cssSelector("div[data-ref='counter.counter__decrement']"));
    }
}
