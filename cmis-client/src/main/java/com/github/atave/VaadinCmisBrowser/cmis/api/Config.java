package com.github.atave.VaadinCmisBrowser.cmis.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * A {@link Properties} loader.
 */
public class Config {

    private Config() {
    }

    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    static final Properties properties = new Properties();

    private static InputStream getResource(String name) {
        return Config.class.getResourceAsStream(name);
    }

    static {
        load("/global.properties");
        load("/local.properties");
    }

    /**
     * Loads the specified resource.
     *
     * @param resourceName the name of the resource to load
     * @return whether or not that resource could be loaded
     * @see Class#getResourceAsStream(String)
     */
    public static boolean load(String resourceName) {
        boolean success = false;

        Properties newProperties = new Properties();

        try (InputStream inputStream = getResource(resourceName)) {
            if (inputStream != null) {
                newProperties.load(inputStream);

                // Merge new properties
                for (String property : newProperties.stringPropertyNames()) {
                    properties.setProperty(property, newProperties.getProperty(property));
                }

                success = true;
            }
        } catch (IOException e) {
            // ignore
        }

        return success;
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
