package data.structure;

import data.structure.skiplist.SkipList;
import org.junit.jupiter.api.Test;

import static data.structure.utils.CollectionTests.testsCollection;

/**
 * @author no-today
 * @date 2024/03/22 10:24
 */
class SkipListTest {

    @Test
    void skiplist() {
        testsCollection(new SkipList<>());
    }
}