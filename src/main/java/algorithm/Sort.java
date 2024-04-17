package algorithm;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @author no-today
 * @date 2023/02/28 15:26
 * @see <a href="https://www.toptal.com/developers/sorting-algorithms">Sorting-Algorithms-Animations</a>
 */
public class Sort {

    private static <T extends Comparable<T>> void swap(T[] array, int i, int j) {
        T t = array[i];
        array[i] = array[j];
        array[j] = t;
    }

    /**
     * 冒泡排序 O(n^2)
     * <p>
     * 从第一个元素开始，相邻的元素两两比较，如果前面的元素大于后面的元素，则交换它们的位置，一轮下来，最后一个元素一定是最大的，然后再对前面的元素进行相同的操作。
     */
    public static <T extends Comparable<T>> void bubble(T[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j].compareTo(array[j + 1]) > 0) {
                    swap(array, j, j + 1);
                }
            }
        }
    }

    /**
     * 选择排序 O(n^2)
     * <p>
     * 从未排序的序列中找到最小的元素，放到已排序序列的末尾。一次遍历后，未排序序列中最小的元素已经排在了序列的最前面，然后再对剩下的元素进行相同的操作。
     */
    public static <T extends Comparable<T>> void selection(T[] array) {
        for (int i = 0; i < array.length; i++) {
            int minIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[minIndex].compareTo(array[j]) > 0) {
                    minIndex = j;
                }
            }

            swap(array, i, minIndex);
        }
    }

    /**
     * 插入排序 O(n^2)
     * <p>
     * 将未排序的元素插入到已排序的序列中，从后往前遍历已排序的序列，找到插入位置。
     * 一轮下来，未排序的元素中最小的元素已经排在了序列的最前面，然后再对剩下的元素进行相同的操作。
     */
    public static <T extends Comparable<T>> void insertion(T[] array) {
        for (int i = 1; i < array.length; i++) {
            T cur = array[i];
            int j = i - 1;

            /*
             * 左边都是排好序的，假如当前元素比左侧的元素小，那么有序就被破坏了
             * 2 4 6 3 5
             * 依次将比它大的元素向后移动，腾出它应该插入的位置。
             * 2 3 4 6 5
             */
            while (j >= 0 && array[j].compareTo(cur) > 0) {
                array[j + 1] = array[j];
                j--;
            }

            array[j + 1] = cur;
        }
    }

    /**
     * 希尔排序 O(nlogn) ~ O(n^2)
     * <p>
     * 将序列分成若干个子序列，对每个子序列进行插入排序，然后缩小子序列的范围，再进行插入排序，直到子序列的长度为1，最终得到有序序列。
     */
    public static <T extends Comparable<T>> void shell(T[] array) {
        /*
         * 当您要对一组数进行排序时，希尔排序算法会将这组数按照一定间隔（称为间隔序列）进行分组，对每个分组分别进行插入排序。随着算法的进行，
         * 间隔序列不断缩小，直到为 1，此时整个数组变为一个分组，最后进行一次插入排序。
         *
         * 例如，考虑下面这个数组 [8, 4, 1, 6, 2, 7, 3, 5]，我们可以选择一个间隔序列为 [4, 2, 1]，这是希尔排序算法中的一种常用序列。
         *
         * 在第一轮排序时，我们将整个数组分成了 4 个子数组，分别为：
         * [8, 2]
         * [4, 7]
         * [1, 3]
         * [6, 5]
         *
         * 对于这些子数组，我们分别进行插入排序。例如，对第一个子数组 [8, 2] 进行插入排序，我们可以得到 [2, 8]，对第二个子数组 [4, 7] 进
         * 行插入排序，得到 [4, 7]，对第三个子数组 [1, 3] 进行插入排序，得到 [1, 3]，对第四个子数组 [6, 5] 进行插入排序，得到 [5, 6]。
         * 现在数组变成了：
         * [2, 4, 1, 5, 8, 7, 3, 6]
         *
         * 在第二轮排序时，我们将整个数组分成了 2 个子数组，分别为：
         * [2, 1, 8, 3]
         * [4, 5, 7, 6]
         *
         * 对于这些子数组，我们分别进行插入排序。例如，对第一个子数组 [2, 1, 8, 3] 进行插入排序，我们可以得到 [1, 2, 3, 8]，对第二个子数
         * 组 [4, 5, 7, 6] 进行插入排序，得到 [4, 5, 6, 7]。现在数组变成了：
         * [1, 2, 3, 4, 5, 6, 7, 8]
         *
         * 在第三轮排序时，我们将整个数组分成了 1 个子数组，也就是整个数组本身。对这个子数组进行插入排序，得到最终的有序数组：
         * [1, 2, 3, 4, 5, 6, 7, 8]
         *
         * 可以看到，希尔排序算法通过不断缩小间隔序列，将整个数组分成了多个子数组，对每个子数组进行插入排序，最终得到了有序数组。这个过程中，
         * 每次排序都可以减少一些逆序对的数量，从而加速了整个排序的效率。由于希尔排序算法中的插入排序是对子数组进行的，因此其比插入排序要快一
         * 些，但由于需要多次排序，因此其比较次数和移动次数也会更多一些。
         *
         * 希尔排序算法的时间复杂度取决于间隔序列的选择，目前还没有找到一种最优的间隔序列，因此其时间复杂度也无法准确地估计。一般来说，希尔排
         * 序算法的时间复杂度在 O(n log n) 到 O(n²) 之间，实际上取决于数组的大小和间隔序列的选择。
         *
         * 希尔排序算法的空间复杂度为 O(1)，因为它只需要常量级的额外空间来存储一些临时变量。因此，希尔排序算法是一种稳定的原址排序算法，适合
         * 于需要排序的数组比较大的情况。
         */

        int h = array.length / 2;
        while (h >= 1) {
            insertion(array, h);
            h /= 2;
        }
    }

    /**
     * 对间隔(分组)元素进行插入排序
     */
    private static <T extends Comparable<T>> void insertion(T[] array, int gap) {
        for (int group = 0; group < gap; group++) { // 遍历N个分组
            for (int memberR = group + gap; memberR < array.length; memberR += gap) { // 遍历组里的成员
                T r = array[memberR];

                int i = memberR;
                while (i - gap >= 0 && r.compareTo(array[i - gap]) < 0) { // 检测并将当前成员插入到有序序列
                    array[i] = array[i - gap];
                    i -= gap;
                }

                array[i] = r;
            }
        }
    }

    /**
     * 归并排序 O(nlogn)
     * <p>
     * 归并排序的核心思路是将一个大问题分成若干个小问题解决，然后将小问题的解合并起来，最终得到大问题的解。
     */
    public static <T extends Comparable<T>> T[] merge(T[] array) {
        int mid = array.length / 2;

        // 递归终止: 分裂到只剩下一个元素
        if (mid == 0) return array;

        T[] left = Arrays.copyOfRange(array, 0, mid);
        T[] right = Arrays.copyOfRange(array, mid, array.length);

        left = merge(left);
        right = merge(right);

        return mergeArray(left, right);
    }

    /**
     * 合并两个有序数组，并返回新的数组
     */
    public static <T extends Comparable<T>> T[] mergeArray(T[] arr1, T[] arr2) {
        T[] result = (T[]) Array.newInstance(arr1.getClass().getComponentType(), arr1.length + arr2.length);

        int i = 0, j = 0, k = 0;
        while (i < arr1.length && j < arr2.length) {
            if (arr1[i].compareTo(arr2[j]) < 0) result[k++] = arr1[i++];
            else result[k++] = arr2[j++];
        }

        while (i < arr1.length) result[k++] = arr1[i++];
        while (j < arr2.length) result[k++] = arr2[j++];

        return result;
    }

    /**
     * 合并一个数组中的两个相邻的有序区间
     * <p>
     * l       m     r
     * 2 4 6 8 1 3 5 7
     *
     * @param array     array
     * @param leftStart leftStart index, inclusive
     * @param middle    midIndex, AS leftEnd(exclusive) AS rightStart(inclusive)
     * @param rightEnd  rightEnd index, exclusive
     * @param <T>       element type
     */
    public static <T extends Comparable<T>> void mergeArray(T[] array, int leftStart, int middle, int rightEnd) {
        // 由于数据会被覆盖，所以需要备份需要用到的的区间段
        T[] temp = Arrays.copyOfRange(array, leftStart, rightEnd);

        int index = leftStart;
        int leftIndex = leftStart;
        int rightIndex = middle;

        while (index < rightEnd) {
            // 左侧区间已结束
            if (leftIndex >= middle) {
                array[index] = temp[rightIndex - leftStart];
                index++;
                rightIndex++;
                continue;
            }
            // 右侧区间已结束
            if (rightIndex >= rightEnd) {
                array[index] = temp[leftIndex - leftStart];
                index++;
                leftIndex++;
                continue;
            }

            // 使用临时数组进行比较，需要进行索引对齐
            // 临时数组的 0 == 主数组的 leftStart，所以主数组的 (leftIndex - leftStart)
            T left = temp[leftIndex - leftStart];
            T right = temp[rightIndex - leftStart];

            if (left.compareTo(right) < 0) {
                array[index] = left;
                leftIndex++;
            } else {
                array[index] = right;
                rightIndex++;
            }
            index++;
        }
    }

    /**
     * 归并排序 O(nlogn)
     * <p>
     * 原地排序
     */
    public static <T extends Comparable<T>> void mergeSort(T[] array) {
        mergeSort(array, 0, array.length);
    }

    private static <T extends Comparable<T>> void mergeSort(T[] array, int leftStart, int rightEnd) {
        if (rightEnd - leftStart <= 1) return;

        int mid = (leftStart + rightEnd) / 2;
        mergeSort(array, leftStart, mid);
        mergeSort(array, mid, rightEnd);

        mergeArray(array, leftStart, mid, rightEnd);
    }
}
