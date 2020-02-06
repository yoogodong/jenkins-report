package spdb.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import spdb.utils.Config;

/**
 * 故障周期，起点是失败的构建，终点是其后第一个成功的构建。中间其他失败的构建全部跳过
 */
@ToString
@Getter
public class BuildFailurePeriod {

    private Build failureBuild;

    @Setter
    private Build successBuild;

    /**
     * 成功的构建可以为null
     */
    public BuildFailurePeriod(Build failureBuild, Build successBuild) {
        this.failureBuild = failureBuild;
        this.successBuild = successBuild;
    }

    /**
     * 如果成功的构建为null, 则采用统计的截止时间作为成功的时间
     */
    public Long getDuration() {
        Long end = null;
        if (successBuild == null) {
            end = Config.instance().getTo();
        } else {
            end = successBuild.getTimestamp() + successBuild.getDuration();
        }
        Long begin = failureBuild.getTimestamp();

        return end - begin;
    }
}
