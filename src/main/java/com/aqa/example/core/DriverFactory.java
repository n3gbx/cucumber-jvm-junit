package com.aqa.example.core;

import com.aqa.example.environment.ChromeConfig;
import com.aqa.example.environment.ConfigManager;
import com.aqa.example.environment.FirefoxConfig;
import com.aqa.example.environment.GeneralConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class DriverFactory {
    private static final Logger LOGGER = LogManager.getLogger(DriverFactory.class);

    private static class BrowserSessionKiller implements Runnable {
        public void run() {
            LOGGER.info("Terminating driver session");
            DriverManager.terminate();
        }
    }

    public static WebDriver create() {
        LOGGER.info("Starting WebDriver session creation...");

        Runtime.getRuntime().addShutdownHook(new Thread(new BrowserSessionKiller()));
        final GeneralConfig config = ConfigManager.getGeneralConfig();

        try {
            DriverManagerType browserType = DriverManagerType.valueOf(config.browser().toUpperCase());

            if (config.driverBinary() != null) {
                LOGGER.info("Using WebDriver binary from the given path: {}", config.driverBinary());
                String driverBinaryFilePath = config.driverBinary();
                System.setProperty(resolveWebDriverSystemPropertyKey(browserType), driverBinaryFilePath);
            } else {
                LOGGER.info("Using WebDriver binary from WebDriverManager");
                WebDriverManager wdm = createWebDriverManager(browserType, config.browserVersion(), config.driverVersion());
                wdm.setup();
            }

            WebDriver webDriver = createBrowserWebDriver(browserType);
            webDriver.manage().deleteAllCookies();
            webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            return webDriver;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static WebDriverManager createWebDriverManager(final DriverManagerType browserType,
                                                           final String browserVersion,
                                                           final String driverVersion) {
        final Path binariesCacheDir = Paths.get(SystemUtils.getUserDir().toString(), "target", ".cache");
        WebDriverManager webDriverManager = WebDriverManager.getInstance(browserType);

        // resolve browser wd version, or let manager to resolve automatically for a given browser
        if (StringUtils.isNotEmpty(browserVersion)) {
            LOGGER.info("Force WebDriverManager to use proper version of WebDriver for {} {}", browserType.toString(), browserVersion);
            webDriverManager.browserVersion(browserVersion);
        } else if (StringUtils.isNotEmpty(driverVersion)) {
            LOGGER.info("Force WebDriverManager to use {} WebDriver version for {}", driverVersion, browserType.toString());
            webDriverManager.driverVersion(driverVersion);
        }

        LOGGER.info("WebDriver binaries cache dir: {}", binariesCacheDir.toString());

        File cacheDir = new File(binariesCacheDir.toUri());
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        webDriverManager.cachePath(cacheDir.getAbsolutePath());

        return webDriverManager;
    }

    private static WebDriver createBrowserWebDriver(DriverManagerType browserType) {
        LOGGER.info("Creating WebDriver for {} browser...", browserType.toString());

        switch (browserType) {
            case CHROME:
                ChromeConfig chromeConfig = ConfigManager.getChromeConfig();

                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setHeadless(chromeConfig.headless());
                if (chromeConfig.args() != null) chromeOptions.addArguments(chromeConfig.args());

                return new ChromeDriver(chromeOptions);
            case FIREFOX:
                FirefoxConfig firefoxConfig = ConfigManager.getFirefoxConfig();

                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setHeadless(firefoxConfig.headless());
                if (firefoxConfig.args() != null) firefoxOptions.addArguments(firefoxConfig.args());

                WebDriver driver = new FirefoxDriver(firefoxOptions);
                if (firefoxConfig.maximise()) driver.manage().window().maximize();

                return driver;
            case SAFARI:
                SafariOptions safariOptions = new SafariOptions();
                safariOptions.setCapability("safari.cleanSession", true);

                return new SafariDriver(safariOptions);
            default:
                throw new IllegalArgumentException(browserType + " browser isn't allowed for using");
        }
    }

    private static String resolveWebDriverSystemPropertyKey(DriverManagerType browserType) {
        switch (browserType) {
            case CHROME:
                return ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY;
            case FIREFOX:
                return GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY;
            case SAFARI:
                return "webdriver.safari.driver";
        }
        throw new IllegalArgumentException(browserType + " browser isn't allowed for using");
    }
}
