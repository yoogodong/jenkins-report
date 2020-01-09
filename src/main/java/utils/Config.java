package utils;

import io.restassured.RestAssured;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class Config {

    private  Properties properties;
    private static Config config;

    public static synchronized Config instance() throws IOException {
        if (config==null) {
            config = new Config();
        }
        return config;
    }

    private Config() throws IOException {
        properties = new Properties();
        @Cleanup
        InputStream resourceAsStream = Config.class.getClassLoader().getResourceAsStream("config.properties");
        properties.load(resourceAsStream);

        log.info("初始化配置: {}",properties);

        log.info("disable url encoding");
        RestAssured.urlEncodingEnabled=false;
    }

    public String getJenkinsIndexUrl(){
        return properties.getProperty("jenkins_index_url");
    }

    public String getUser(){
        return properties.getProperty("user");
    }

    public String getPassword(){
        return properties.getProperty("password");
    }
}
