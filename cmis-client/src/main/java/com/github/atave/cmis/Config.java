package com.github.atave.cmis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * A {@link Properties} loader.
 */
public class Config {

    static Properties properties = new Properties();

    private static InputStream getResource(String name) {
        return Config.class.getClass().getResourceAsStream(name);
    }

    static {
        // Load global.properties
        Properties globalProperties = new Properties();
        try {
            globalProperties.load(getResource("/global.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load local.properties
        try {
            properties.load(getResource("/local.properties"));
        } catch (IOException e) {
            properties = globalProperties;
        }

        // Set defaults
        if(globalProperties != properties) {
            for(String property : globalProperties.stringPropertyNames()) {
                if(properties.getProperty(property) == null) {
                    String value = globalProperties.getProperty(property);
                    properties.setProperty(property, value);
                }
            }
        }
    }

    public static String get(String name) {
        return properties.getProperty(name);
    }
}
