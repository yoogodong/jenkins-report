package model;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import utils.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


/**
 * 代表 workflow 或 folder
 */
@ToString
@Slf4j
public class WorkflowJob extends Job {
    private List<Build> builds = new ArrayList<>();

    public WorkflowJob(Map<String, String> job_desc) {
        super(job_desc);
        List<String> build_urls = jsonPath.getList("builds.url", String.class);
        build_urls.forEach(url -> builds.add(new Build(url)));
        builds.sort(Comparator.comparingLong(b->b.getTimestamp()));
    }

    @Override
    public int buildCount() {
        return builds.size();
    }

    @Override
    public int successBuildCount(){
        return (int) builds.stream().filter(build -> build.isSuccess()).count();
    }

    @Override
    public int failureBuildCount(){
        return (int) builds.stream().filter(build -> build.isFailure()).count();
    }


    @Override
    public Long earliest() {
        if (buildCount() == 0) {
            return null;
        }
        Build build = builds.get(0);
        Long timestamp = build.getTimestamp();
        return timestamp;

    }

    /**
     * 最后一次构建+构建时长
     */
    @Override
    public Long last() {
        if (buildCount() == 0) return null;
        Build lastBuild = builds.get(builds.size() - 1);
        return lastBuild.getTimestamp() + lastBuild.getDuration();
    }


}

