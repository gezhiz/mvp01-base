package com.mvp01.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AuthConfig {
    public static Logger logger = LoggerFactory.getLogger(AuthConfig.class);
    public static final String appid;
    public static final String appsecret;

    public static final String weibo_client_id;
    public static final String weibo_client_secret;


    private AuthConfig() {
    }

    static {
        Properties prop = new Properties();
        InputStream in = AuthConfig.class.getClassLoader().getResourceAsStream("authConfig.properties");
        try {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("配置文件读取错误", e.getMessage());
        }
        appid = prop.getProperty("wechat_appid");
        appsecret = prop.getProperty("wechat_appsecret");
        weibo_client_id = prop.getProperty("weibo_client_id");
        weibo_client_secret = prop.getProperty("weibo_client_secret");
    }

}
