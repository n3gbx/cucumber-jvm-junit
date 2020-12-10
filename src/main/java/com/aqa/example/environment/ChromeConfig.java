package com.aqa.example.environment;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:options/chrome.properties"})
public interface ChromeConfig extends Config {

    @Separator(";")
    @Key("chrome.args")
    String[] args();

    @DefaultValue("false")
    @Key("chrome.headless")
    boolean headless();
}
