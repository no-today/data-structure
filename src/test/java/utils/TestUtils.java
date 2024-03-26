package utils;

import java.util.Random;

/**
 * @author no-today
 * @date 2023/02/28 15:42
 */
public class TestUtils {

    private static final Random R = new Random();

    public static Integer[] generateArray(int count, int bound) {
        Integer[] integers = new Integer[count];
        for (int i = 0; i < count; i++) {
            integers[i] = Math.abs(R.nextInt(bound));
        }
        return integers;
    }
}
