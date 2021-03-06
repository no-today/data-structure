package data.structure._tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.function.Consumer;

/**
 * 读取文件
 *
 * @author cheng
 * @date 2018/10/1
 * @time 0:45
 */
public class ReadFileWord {

    /**
     * 读取信息到容器
     *
     * @param consumer
     * @param resources
     * @throws Exception
     */
    public static void read(Consumer<String> consumer, String... resources) throws Exception {
        for (String resource : resources) {
            File file = new File(ReadFileWord.class.getClassLoader().getResource("").getPath(), resource);
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                consumer.accept(line);
            }
        }
    }
}
