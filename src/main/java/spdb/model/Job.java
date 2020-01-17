package spdb.model;

import io.restassured.path.json.JsonPath;
import lombok.Data;
import spdb.utils.Utils;

import java.util.Map;

@Data
public abstract class Job {
    protected String name;
    protected String url;
    protected JsonPath jsonPath;


    public Job(Map<String, String> desc) {
        url = desc.get("url");
        name = desc.get("name");
        jsonPath = Utils.jsonPath(url);
        String fullName = jsonPath.getString("fullName");
        if (fullName != null) {
            name = fullName;
        }
    }


    /**
     * 构建次数总和
     */
    public abstract int buildCount();

    public Float buildPerDay() {
        if (earliest() == null) return 0f;
        int millisPerDay = 1000 * 60 * 60 * 24;
        long timeSpan = last() - earliest();
        float days = timeSpan / (float) millisPerDay;
        return buildCount() / days;
    }

    /**
     * 构建时长总和
     */
    public abstract Long buildDurationSum();

    /**
     * 成功构建的平均构建时长
     */
    public float meanBuildTimeInMinutes() {
        float million = successBuildDurationSum() / successBuildCount();
        return million / 1000 / 60;
    }

    public abstract Long successBuildDurationSum();

    /**
     * 成功构建的数量
     */
    public abstract int successBuildCount();

    /**
     * 失败构建的数量
     */
    public abstract int failureBuildCount();

    /**
     * 最早的构建时间
     */
    public abstract Long earliest();

    /**
     * 最后的构建时间
     */
    public abstract Long last();

    /**
     * 生成报告的格式化的文本：
     * 对于非文件夹的job, 只需要输出当前job 的构建数据
     * 对于文件夹，需要
     * 1。 当前文件件的统计数据（这部分与非文件夹的的统计数据相同）
     * 2。 递归子文件夹/子任务的统计数据
     */
    public StringBuilder report() {
        StringBuilder doc = new StringBuilder("\n");
        doc.append(name).append(">>\n");
        if (buildCount() == 0)
            return doc.append("\t从未构建");
        doc.append("\t最早的构建开始于:").append(Utils.formatTimestamp(earliest()));
        doc.append("\t最后的构建结束于:").append(Utils.formatTimestamp(last()));
        int buildCount = buildCount();
        doc.append("\t构建次数:").append(buildCount);
        doc.append("\t平均构建频率:").append(buildPerDay()).append("次/天");
        doc.append("\t平均构建时长:").append(meanBuildTimeInMinutes()).append("分钟");
        int suc = successBuildCount();
        doc.append("\t成功次数:").append(suc);
        doc.append("\t失败次数:").append(failureBuildCount());
        doc.append("\t成功率:").append(100 * suc / (float) buildCount).append("%");
        return doc;
    }
}
