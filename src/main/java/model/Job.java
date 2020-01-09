package model;

import lombok.Data;

import java.util.Map;

@Data
public abstract class Job {
    protected String name;
    protected String url;


    public Job(Map<String, String> desc) {
        url = desc.get("url");
        name = desc.get("name");
    }


    /**
     * 构建次数总和
     */
    public abstract int buildTimeSum();

    /**
     * 最早的构建时间
     */
    public abstract Long earliest();

    /**
     * 最后的构建时间
     */
    public abstract Long last();
}
