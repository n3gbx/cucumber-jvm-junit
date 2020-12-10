package com.aqa.example.environment;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:general.properties"})
public interface GeneralConfig extends Config {

    @DefaultValue("30")
    @Key("screen.timeout")
    int screenTimeout();

    @DefaultValue("9")
    @Key("element.timeout")
    int elementTimeout();

    @DefaultValue("chrome")
    @Key("browser")
    String browser();

    @Key("wdm.browser.version")
    String browserVersion();

    @Key("wdm.driver.version")
    String driverVersion();

    @Key("driver.binary")
    String driverBinary();
}
