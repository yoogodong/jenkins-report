package model;

import lombok.ToString;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 代表 workflow 或 folder
 */
@ToString
public class WorkflowJob extends Job {
    private List<Build> builds = new ArrayList<>();

    public WorkflowJob(Map<String, String> job_desc) {
        super(job_desc);
        List<String> build_urls = Utils.jsonPath(url).getList("builds.url", String.class);
        build_urls.forEach(url -> builds.add(new Build(url)));
        System.out.println("builds = " + builds);
    }

    @Override
    public int buildTimeSum() {
        return builds.size();
    }


    @Override
    public Long earliest() {
        if (buildTimeSum() == 0) return null;
        return builds.get(0).getTimestamp();
    }

    /**
     * 最后一次构建+构建时长
     */
    @Override
    public Long last() {
        if (buildTimeSum() == 0) return null;
        Build lastBuild = builds.get(builds.size() - 1);
        return lastBuild.getTimestamp() + lastBuild.getDuration();
    }
}

