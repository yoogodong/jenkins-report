package spdb.utils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Utils {


    private static Map<String, String> cookies;

    public static String formatTimestamp(Long timestamp) {
        if (timestamp == null) return "无";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp));
    }

    public static JsonPath jsonPath(String url) {
        return RestAssured.given().cookies(getCookies()).get(url + Const.suffix).jsonPath();
    }

    private static synchronized Map<String, String> getCookies() {
        if (cookies == null) {
            String user = Config.instance().getUser();
            String pword = Config.instance().getPassword();
            if (pword == null || pword.length() < 1)
                return new HashMap<>();
            Response response = RestAssured.given().formParam("j_username", user).formParam("j_password", pword).post("http://localhost:8080/j_acegi_security_check");
            if (response.header("Location").endsWith("loginError"))
                throw new RuntimeException("认证错误");
            cookies = response.cookies();
        }
        return cookies;
    }


}
