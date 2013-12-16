package com.github.atave.cmis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * A {@link Properties} loader.
 */
public class Config {

    static Logger logger = LoggerFactory.getLogger(Config.class);

    static Properties properties = new Properties();

    private static InputStream getResource(String name) {
        return Config.class.getClass().getResourceAsStream(name);
    }

    static {
        // Load global.properties
        Properties globalProperties = new Properties();

        try (InputStream inputStream = getResource("/global.properties")) {
            if (inputStream != null) {
                globalProperties.load(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load local.properties
        try (InputStream inputStream = getResource("/local.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                properties = globalProperties;
            }
        } catch (IOException e) {
            properties = globalProperties;
        }

        // Set defaults
        if (properties != globalProperties) {
            for (String property : globalProperties.stringPropertyNames()) {
                if (properties.getProperty(property) == null) {
                    String value = globalProperties.getProperty(property);
                    properties.setProperty(property, value);
                }
            }
        }
    }

    /**
     * @see java.util.Properties#getProperty(String)
     */
    public static String get(String name) {
        String property = properties.getProperty(name);

        if (property == null) {
            logger.error("Property not found: {}", name);
        }

        return property;
    }
}
