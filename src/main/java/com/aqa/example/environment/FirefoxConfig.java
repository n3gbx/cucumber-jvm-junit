package com.aqa.example.environment;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:options/firefox.properties"})
public interface FirefoxConfig extends Config {

    @Separator(";")
    @Key("ff.args")
    String[] args();

    @DefaultValue("false")
    @Key("ff.headless")
    boolean headless();

    @DefaultValue("true")
    @Key("ff.maximise")
    boolean maximise();
}
