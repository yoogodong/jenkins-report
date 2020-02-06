package spdb.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 代表故障周期列表
 * 提供计算总故障时间，平均故障时长（错误恢复时长）
 * 列表的每个元素包含两个构建，一个是失败的head, 一个是成功的tail. 如果没有tail, 则是不完整的。
 * 列表评估构建并设置周期的起点或者终点
 */
public class BuildFailurePeriodList {
    private final List<BuildFailurePeriod> buildFailurePeriods = new ArrayList<>();


    /**
     * 评估一个构建是否可以作为失败区间的起点或者终点，还是直接抛弃
     */
    public void assessBuild(Build build) {
        couldBeHead(build);
        couldBeTail(build);
    }

    /*评估并设置head*/
    private void couldBeHead(Build build) {
        if (build.isFailure() && isCompleted()) {
            buildFailurePeriods.add(new BuildFailurePeriod(build, null));
        }
    }

    /*评估并设置tail*/
    private void couldBeTail(Build build) {
        if (build.isSuccess() && !isCompleted()) {
            BuildFailurePeriod lastFailurePeriod = buildFailurePeriods.get(buildFailurePeriods.size() - 1);
            lastFailurePeriod.setSuccessBuild(build);
        }
    }

    /**
     * 评估列表是否是完整的
     */
    private boolean isCompleted() {
        if (buildFailurePeriods.size() == 0) return true;
        BuildFailurePeriod lastFailurePeriod = buildFailurePeriods.get(buildFailurePeriods.size() - 1);
        return lastFailurePeriod.getSuccessBuild() != null;
    }

    public Long sumDuration() {
        return buildFailurePeriods.stream()
                .map(BuildFailurePeriod::getDuration)
                .reduce(Long::sum).orElse(0L);
    }


    public int size() {
        return buildFailurePeriods.size();
    }
}
