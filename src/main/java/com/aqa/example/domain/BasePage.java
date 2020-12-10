package com.aqa.example.domain;

import com.aqa.example.environment.ConfigManager;
import com.aqa.example.helpers.ElementsHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementDecorator;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;


public abstract class BasePage {
    private final String screenName;
    private final WebDriver webDriver;
    private final int screenTimeout;
    private final int elementTimeout;
    protected final Logger LOGGER = LogManager.getLogger(this.getClass().getName());

    public BasePage(WebDriver webDriver, String screenName) {
        this.webDriver = webDriver;
        this.screenName = screenName;
        this.screenTimeout = ConfigManager.getGeneralConfig().screenTimeout();
        this.elementTimeout = ConfigManager.getGeneralConfig().elementTimeout();
        PageFactory.initElements(new HtmlElementDecorator(new HtmlElementLocatorFactory(webDriver)), this);
    }

    abstract public boolean isOpened();

    public boolean waitForLoading(ExpectedCondition<? extends WebElement> additionalCondition, int timeoutSec) {
        waitForLoading(timeoutSec);                                           // wait for page to be ready
        ElementsHelper.getDefaultWait(timeoutSec).until(additionalCondition); // wait for content to be loaded
        LOGGER.debug("'{}' page is loaded", getScreenName());
        return true;
    }

    public boolean waitForLoading(int timeoutSec) {
        boolean isPageReady = ElementsHelper.getDefaultWait(timeoutSec).until(wd -> {
            return ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete");
        });

        if (!isPageReady) {
            throw new TimeoutException(getScreenName() + " isn't ready after " + timeoutSec + " seconds");
        } else {
            LOGGER.debug("'{}' page is ready", getScreenName());
            return true;
        }
    }

    public boolean waitForLoading() {
        return waitForLoading(screenTimeout);
    }

    public void acceptCookies() {
        webDriver.findElement(By.cssSelector("button[data-ref='cookie.accept-all']")).click();
    }

    public boolean hasUrl(String urlPart) {
        String url = com.aqa.example.domain.PageUrl.BASE_URL.equals(urlPart) ? urlPart : com.aqa.example.domain.PageUrl.BASE_URL + urlPart;
        return webDriver.getCurrentUrl().startsWith(url);
    }

    public String getScreenName() {
        return screenName;
    }

    protected WebDriver getWebDriver() {
        return webDriver;
    }

    protected int getScreenTimeout() {
        return screenTimeout;
    }

    protected int getElementTimeout() {
        return elementTimeout;
    }
}
