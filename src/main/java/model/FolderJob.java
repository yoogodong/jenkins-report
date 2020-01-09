package model;

import lombok.extern.slf4j.Slf4j;
import utils.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class FolderJob extends Job {

    private List<FolderJob> folders = new ArrayList<>();
    private List<WorkflowJob> workflowJobs = new ArrayList<>();

    public FolderJob(Map<String, String> desc) {
        super(desc);
        log.info("请求:{}", url);
        List<Map> jobs_desc = Utils.jsonPath(url).getList("jobs", Map.class);
        for (Map<String, String> job_desc : jobs_desc) {
            String _class = job_desc.get("_class");
            if (_class.endsWith("Folder")) {
                folders.add(new FolderJob(job_desc));
            } else if (_class.endsWith("WorkflowJob")) {
                workflowJobs.add(new WorkflowJob(job_desc));
            }
        }
    }


    @Override
    public int buildTimeSum() {
        AtomicInteger sum = new AtomicInteger();
        workflowJobs.forEach(workflowJob -> sum.addAndGet(workflowJob.buildTimeSum()));
        folders.forEach(folderJob -> sum.getAndAdd(folderJob.buildTimeSum()));
        return sum.get();
    }

    @Override
    public Long earliest() {
        ArrayList<Long> earliestOfEachJob = new ArrayList<>();
        folders.forEach(folderJob -> {
            if (folderJob.earliest() != null)
                earliestOfEachJob.add(folderJob.earliest());
        });
        workflowJobs.forEach(workflowJob -> {
            if (workflowJob.earliest() != null)
                earliestOfEachJob.add(workflowJob.earliest());
        });
        if (earliestOfEachJob.size() == 0) return null;
        earliestOfEachJob.sort(Comparator.comparingLong(x -> x));
        return earliestOfEachJob.get(0);
    }

    @Override
    public Long last() {
        return null;
    }

    public String report() {
        return null;
    }
}
