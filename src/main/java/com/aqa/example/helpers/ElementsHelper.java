package com.aqa.example.helpers;

import com.aqa.example.core.DriverManager;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class ElementsHelper {
    public static final String SELECTED = "--selected";
    public static final String CHECKED = "--checked";
    public static final String DISABLED = "--disabled";

    public static <T extends WebElement> T getExactFromListOrThrow(List<T> elements, String text) {
        return elements.stream()
                .filter(el -> StringUtils.equalsIgnoreCase(el.getText().trim(), text))
                .findFirst()
                .orElseThrow(() -> {
                    String list = elements.stream().map(el -> el.getText().trim()).collect(Collectors.joining(", "));
                    return new NoSuchElementException("Element with text " + text + " is not found in list: " + list);
                });
    }

    public static <T extends WebElement> T getPartFromListOrThrow(List<T> elements, String textPart) {
        return elements.stream()
                .filter(el -> StringUtils.containsIgnoreCase(el.getText().trim(), textPart))
                .findFirst()
                .orElseThrow(() -> {
                    String list = elements.stream().map(el -> el.getText().trim()).collect(Collectors.joining(", "));
                    return new NoSuchElementException("Element with text " + textPart + " is not found in list: " + list);
                });
    }

    public static <T extends WebElement> T executeClick(T element) {
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].click();", element);
        return element;
    }

    public static <T extends WebElement> T scrollTo(T element) {
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
        return element;
    }

    public static <T extends WebElement> T scrollToCenter(T element) {
        String scrollElementIntoMiddle = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
                + "var elementTop = arguments[0].getBoundingClientRect().top;"
                + "window.scrollBy(0, elementTop-(viewPortHeight/2));";

        ((JavascriptExecutor) DriverManager.getDriver()).executeScript(scrollElementIntoMiddle, element);
        return element;
    }

    public static FluentWait<WebDriver> getDefaultWait(int timeoutSec) {
        return new FluentWait<>(DriverManager.getDriver())
                .withTimeout(Duration.ofSeconds(timeoutSec))
                .pollingEvery(Duration.ofMillis(10))
                .ignoring(NoSuchElementException.class)
                .ignoring(NotFoundException.class)
                .ignoring(TimeoutException.class);
    }
}
