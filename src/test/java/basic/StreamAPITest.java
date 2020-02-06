package basic;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StreamAPITest {
    @Test
    void testSum() {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        Integer sum = integers.stream().reduce(Integer::sum).orElse(0);
        assertEquals(15, sum);
    }
}
