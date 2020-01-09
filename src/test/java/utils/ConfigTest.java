package utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

    @Test
    void testConfig() throws IOException {
        Config config = Config.instance();
        String jenkinsIndexUrl = config.getJenkinsIndexUrl();
        System.out.println("jenkinsIndexUrl = " + jenkinsIndexUrl);
        assertThat(jenkinsIndexUrl).startsWith("http://");
    }
}