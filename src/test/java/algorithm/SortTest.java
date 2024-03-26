package algorithm;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.TestUtils;

import java.util.Arrays;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author no-today
 * @date 2023/02/28 15:41
 */
class SortTest {

    static final int COUNT = 50000;
    static final int BOUND = Integer.MAX_VALUE;
    static final boolean PRINT = false;

    static Integer[] ARRAY;

    @BeforeAll
    static void setup() {
        ARRAY = TestUtils.generateArray(COUNT, BOUND);
    }

    private static void assertOrderly(Integer[] array) {
        for (int i = 1; i < array.length; i++) {
            // 前面的元素必定小于后面的元素
            assertTrue(array[i - 1].compareTo(array[i]) <= 0);
        }
    }

    private static void assertSorted(Consumer<Integer[]> consumer, Integer[] array) {
        if (PRINT) System.out.println(Arrays.toString(array));
        consumer.accept(array);
        if (PRINT) System.out.println(Arrays.toString(array));
        assertOrderly(array);
    }

    private static void assertSorted(Consumer<Integer[]> consumer) {
        assertSorted(consumer, Arrays.copyOf(ARRAY, ARRAY.length));
    }

    private Integer[] concat(Integer[] arr1, Integer[] arr2) {
        Integer[] array = new Integer[arr1.length + arr2.length];

        int i = 0;
        int j = 0;

        while (j < arr1.length) array[i++] = arr1[j++];

        j = 0;
        while (j < arr2.length) array[i++] = arr2[j++];
        return array;
    }

    @Test
    void bubble() {
        assertSorted(Sort::bubble);
    }

    @Test
    void selection() {
        assertSorted(Sort::selection);
    }

    @Test
    void insertion() {
        assertSorted(Sort::insertion);
    }

    @Test
    void shell() {
        assertSorted(Sort::shell);
    }

    @Test
    void mergeArray() {
        Integer[] arr1 = TestUtils.generateArray(5, 100);
        Integer[] arr2 = TestUtils.generateArray(5, 100);
        Sort.shell(arr1);
        Sort.shell(arr2);

        Integer[] merge = Sort.mergeArray(arr1, arr2);
        System.out.println(Arrays.toString(merge));
        assertOrderly(merge);
    }

    @Test
    void merge() {
        assertOrderly(Sort.merge(TestUtils.generateArray(COUNT, BOUND)));
    }

    @Test
    void mergeArray1() {
        int count = 5;
        int bound = 100;

        Integer[] arr1 = TestUtils.generateArray(count, bound);
        Integer[] arr2 = TestUtils.generateArray(count, bound);

        Sort.shell(arr1);
        Sort.shell(arr2);

        Integer[] array = concat(arr1, arr2);
        Sort.mergeArray(array, 0, array.length / 2, array.length);
        assertOrderly(array);
    }

    @Test
    void mergeSort() {
        assertSorted(Sort::mergeSort);
    }
}