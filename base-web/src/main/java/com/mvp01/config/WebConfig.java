package com.mvp01.config;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by wenjie on 16/4/29.
 */
public class WebConfig {
    private static final Properties PROPERTIES = loadProperties();

    private static Properties loadProperties() {
        try {
            return PropertiesLoaderUtils.loadProperties(new DefaultResourceLoader().getResource("classpath:web.properties"));
        } catch (IOException e) {
            return new Properties();
        }
    }

    public static final boolean DEBUG = true;
}
