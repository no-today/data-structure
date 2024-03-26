package data.structure;


import data.structure.list.ArrayList;
import data.structure.list.LinkedList;
import org.junit.jupiter.api.Test;

import static data.structure.utils.CollectionTests.testsCollection;

public class ListTest {

    @Test
    public void array() {
        ArrayList<String> list = new ArrayList<>();
        testsCollection(list);
        System.out.println("Resize count: " + list.getResizeCount());
    }

    @Test
    public void linked() {
        testsCollection(new LinkedList<>());
    }
}
