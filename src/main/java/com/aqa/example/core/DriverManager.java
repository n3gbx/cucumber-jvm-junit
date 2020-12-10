package com.aqa.example.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverManager {
    private static final Logger LOGGER = LogManager.getLogger(DriverManager.class);
    private static ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();

    public synchronized static WebDriver getDriver() {
        if (webDriver == null) {
            setDriver(com.aqa.example.core.DriverFactory.create());
        }
        return webDriver.get();
    }

    public synchronized static void setDriver(WebDriver wd) {
        LOGGER.info("Set new WebDriver session with id={}", ((RemoteWebDriver) wd).getSessionId());
        webDriver.set(wd);
    }

    public synchronized static void terminate() throws NullPointerException {
        try {
            webDriver.get().quit();
            webDriver.remove();
            LOGGER.info("WebDriver session has been closed");
        } catch (NullPointerException ignore) {

        }
    }
}
