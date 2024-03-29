package data.structure;


import data.structure.list.ArrayList;
import data.structure.list.LinkedList;
import org.junit.jupiter.api.Test;

import static data.structure.utils.DST.collection;

public class ListTest {

    @Test
    public void array() {
        ArrayList<String> list = new ArrayList<>();
        collection(list);
        System.out.println("Resize count: " + list.getResizeCount());
    }

    @Test
    public void linked() {
        collection(new LinkedList<>());
    }
}
