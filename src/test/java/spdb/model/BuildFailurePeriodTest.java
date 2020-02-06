package spdb.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@MockitoSettings
class BuildFailurePeriodTest {

    @Mock
    Build failureBuild;
    @Mock
    Build successBuild;

    @BeforeEach
    void setUp() {
        when(failureBuild.getTimestamp()).thenReturn(0L);
    }


    @Test
    void testOpenPeriod() {
        BuildFailurePeriod openPeriod = new BuildFailurePeriod(failureBuild, null);
        Long duration = openPeriod.getDuration();
        assertEquals(0, (System.currentTimeMillis() - duration) / 10);
    }

    @DisplayName("故障时长=故障构建开始，到成功构建结束的总时长")
    @Test
    void testCompltedPeriod() {
        when(successBuild.getTimestamp()).thenReturn(10000L);
        when(successBuild.getDuration()).thenReturn(200L);
        BuildFailurePeriod completedPeriod = new BuildFailurePeriod(failureBuild, successBuild);
        assertEquals(10200L, completedPeriod.getDuration());
    }
}