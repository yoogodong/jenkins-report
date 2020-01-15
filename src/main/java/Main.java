import lombok.extern.slf4j.Slf4j;
import model.FolderJob;
import utils.Config;
import utils.Utils;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
public class Main {

    public static void main(String[] args) throws IOException {
        Config config = Config.instance();
        HashMap<String, String> desc = new HashMap<>();
        desc.put("url",config.getJenkinsIndexUrl());
        desc.put("name","");
        FolderJob index = new FolderJob(desc);
        System.out.println(index.report());
    }
}

