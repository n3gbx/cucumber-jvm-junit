package com.aqa.example.environment;

import org.aeonbits.owner.ConfigCache;

final public class ConfigManager {

    private ConfigManager() {

    }

    public static GeneralConfig getGeneralConfig() {
        return ConfigCache.getOrCreate(GeneralConfig.class);
    }

    public static ChromeConfig getChromeConfig() {
        return ConfigCache.getOrCreate(ChromeConfig.class);
    }

    public static FirefoxConfig getFirefoxConfig() {
        return ConfigCache.getOrCreate(FirefoxConfig.class);
    }
}
