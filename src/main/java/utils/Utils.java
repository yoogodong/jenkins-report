package utils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {



    public static String formatTimestamp(long timestamp){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp));
    }

    public static JsonPath jsonPath(String url){
        return RestAssured.get(url+Const.suffix).jsonPath();
    }


}
