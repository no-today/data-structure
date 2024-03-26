package data.structure.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 读取文件
 *
 * @author cheng
 * @date 2018/10/1
 * @time 0:45
 */
public class ArticleReader {

    private static final Pattern WORDS = Pattern.compile("\\b\\w+\\b");

    /**
     * 读取信息到容器
     *
     * @param consumer
     * @param resources
     * @throws Exception
     */
    public static void read(Consumer<String> consumer, String... resources) {
        for (String resource : resources) {
            try {
                File file = new File(ArticleReader.class.getClassLoader().getResource("").getPath(), resource);
                BufferedReader reader = new BufferedReader(new java.io.FileReader(file));

                String line;
                while ((line = reader.readLine()) != null) {
                    tokenize(line, consumer);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void tokenize(String text, Consumer<String> consumer) {
        Matcher matcher = WORDS.matcher(text);
        while (matcher.find()) {
            consumer.accept(matcher.group());
        }
    }
}
