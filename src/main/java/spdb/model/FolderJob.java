package spdb.model;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class FolderJob extends Job {

    private List<Job> jobs = new ArrayList<>();

    public FolderJob(Map<String, String> desc) {
        super(desc);
        List<Map> jobs_desc = jsonPath.getList("jobs", Map.class);
        jobs_desc.forEach(job_desc -> {
            String _class = (String) job_desc.get("_class");
            if (_class.endsWith("Folder")) {
                jobs.add(new FolderJob(job_desc));
            } else if (_class.endsWith("WorkflowJob")) {
                jobs.add(new WorkflowJob(job_desc));
            }
        });
    }


    @Override
    public int buildCount() {
        return jobs.stream().map(Job::buildCount).reduce(0, Integer::sum);
    }

    @Override
    public Long buildDurationSum() {
        return jobs.stream().map(Job::buildDurationSum).reduce(0L, Long::sum);
    }

    @Override
    public Long successBuildDurationSum() {
        return jobs.stream().map(Job::successBuildDurationSum).reduce(0L, Long::sum);
    }

    @Override
    public int successBuildCount() {
        return jobs.stream().map(Job::successBuildCount).reduce(0, Integer::sum);
    }

    @Override
    public int failureBuildCount() {
        return jobs.stream().map(Job::failureBuildCount).reduce(0, Integer::sum);
    }


    @Override
    public Long earliest() {
        List<Long> earliestOfEachJob = jobs.stream().map(Job::earliest)
                .filter(x->x!=null).collect(Collectors.toList());
        if (earliestOfEachJob.size() == 0) return null;
        earliestOfEachJob.sort(Comparator.comparingLong(x -> x));
        return earliestOfEachJob.get(0);
    }

    @Override
    public Long last() {
        ArrayList<Long> lastOfEachJob = new ArrayList<>();
        jobs.forEach(job -> {
            if (job.last() != null)
                lastOfEachJob.add(job.last());
        });
        if (lastOfEachJob.size() == 0) return null;
        lastOfEachJob.sort(Comparator.comparingLong(x -> -x));
        return lastOfEachJob.get(0);
    }

    @Override
    public StringBuilder report() {
        StringBuilder currentFolderReport = super.report();
        Optional<StringBuilder> subJobsReport = jobs.stream().map(Job::report).reduce((x, y) -> x.append(y));
        return currentFolderReport.append(subJobsReport.orElse(new StringBuilder()));
    }
}
