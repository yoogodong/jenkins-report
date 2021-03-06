package spdb.model;

import io.restassured.path.json.JsonPath;
import lombok.Data;
import spdb.utils.Utils;

@Data
public class Build {
    private final String url;
    private String id;
    private Long duration;
    private String result;
    private Long timestamp;


    public Build(String url) {
        this.url = url;
        JsonPath jsonPath = Utils.jsonPath(url);
        duration=jsonPath.getLong("duration");
        id=jsonPath.getString("id");
        result=jsonPath.getString("result");
        timestamp=jsonPath.getLong("timestamp");
    }

    public boolean isSuccess(){
        return result != null && result.equals("SUCCESS");
    }

    public boolean isFailure(){
        return result != null && result.equals("FAILURE");
    }

    public boolean isAborted(){
        return result != null && result.equals("ABORTED");
    }


}
