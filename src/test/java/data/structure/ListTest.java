package data.structure;


import data.structure.list.ArrayList;
import data.structure.list.LinkedList;
import org.junit.jupiter.api.Test;

import static data.structure.utils.DST.collection;

public class ListTest {

    @Test
    public void array() {
        collection(new ArrayList<>());
    }

    @Test
    public void linked() {
        collection(new LinkedList<>());
    }
}
