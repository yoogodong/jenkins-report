package spdb.utils;

import io.restassured.RestAssured;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Slf4j
public class Config {

    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static Config config;
    private final List<String> propertyList;
    private Properties properties;


    public static synchronized Config instance() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }

    private Config() {
        properties = new Properties();
        propertyList = Arrays.asList("url", "user", "password", "from", "to");
        loadInnerConfig();
        loadOuterConfig();
        loadCommandLineConfig();
        log.info("初始化配置: {}", properties);

        verifyConfig();
        log.info("disable url encoding");
        RestAssured.urlEncodingEnabled = false;
    }

    public String getUrl() {
        return properties.getProperty("url");
    }

    public String getUser() {
        return properties.getProperty("user");
    }

    public String getPassword() {
        return properties.getProperty("password");
    }

    @SneakyThrows
    public Long getFrom() {
        String from = properties.getProperty("from");
        return parseTime(from);
    }

    @SneakyThrows
    public Long getTo() {
        Long to = parseTime(properties.getProperty("to"));
        if (to == 0L)
            to = new Date().getTime();
        return to;
    }

    @SneakyThrows
    private void loadOuterConfig() {
        File file = new File("config.properties");
        if (file.exists()) {
            @Cleanup
            FileInputStream fileInputStream = new FileInputStream(file);
            properties.load(fileInputStream);
        } else {
            log.warn("没有找到外部的配置文件 {}", "config.properties");
        }
    }

    @SneakyThrows
    private void loadInnerConfig() {
        @Cleanup
        InputStream resourceAsStream = Config.class.getClassLoader().getResourceAsStream("config.properties");
        properties.load(resourceAsStream);
    }

    /*加载命令行配置*/
    private void loadCommandLineConfig() {
        System.getProperties().entrySet().stream()
                .filter(e -> propertyList.contains(e.getKey()))
                .forEach(e -> properties.put(e.getKey(), e.getValue()));
    }

    /*检查属性的格式*/
    private void verifyConfig() {
        String msg = null;
        if (getUrl().endsWith("//")) {
            msg = "url 结尾只能有一个/";
        } else if (!getUrl().endsWith("/")) {
            msg = "url 结尾必须有一个/";
        }
        if (msg != null) {
            throw new RuntimeException(msg);
        }
    }


    private Long parseTime(String from) throws ParseException {
        DateFormat format = DATE_FORMAT;
        if (from == null || from.length() == 0) {
            return 0L;
        } else if (from.contains(":")) {
            format = DATE_TIME_FORMAT;
        }
        return format.parse(from).getTime();
    }
}
