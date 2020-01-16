package spdb;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import spdb.model.FolderJob;
import spdb.utils.Config;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

@Slf4j
public class Main {

    public static void main(String[] args) throws IOException {
        Config config = Config.instance();
        HashMap<String, String> desc = new HashMap<>();
        desc.put("url", config.getUrl());
        desc.put("name", "");
        FolderJob index = new FolderJob(desc);
        StringBuilder report = index.report();
        System.out.println(report);

        @Cleanup FileWriter writer = new FileWriter("report.txt");
        writer.append(report);
    }
}

