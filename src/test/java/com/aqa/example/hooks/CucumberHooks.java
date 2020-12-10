package com.aqa.example.hooks;

import com.aqa.example.core.DriverFactory;
import com.aqa.example.core.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;


public class CucumberHooks {
    private static final Logger LOGGER = LogManager.getLogger(CucumberHooks.class);

    @Before
    public void init() {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "ERROR");

        WebDriver webDriver = DriverFactory.create();
        DriverManager.setDriver(webDriver);
    }

    @After
    public void shutdown(Scenario scenario) {
        try {
            byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "screenshot");
        } catch (ClassCastException e) {
            LOGGER.error(e.getMessage());
        }
        DriverManager.terminate();
    }
}
