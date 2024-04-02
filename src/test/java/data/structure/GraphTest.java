package data.structure;

import data.structure.graph.GraphAdjList;
import data.structure.graph.GraphAdjMat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author no-today
 * @date 2024/04/02 10:55
 */
public class GraphTest {

    private static void testing(Graph<Integer> graph) {
        for (int i = 1; i <= 10; i++) graph.addVertex(i);
        assertEquals(10, graph.size());

        graph.addEdge(1, 3);
        graph.addEdge(3, 5);
        graph.addEdge(5, 7);
        graph.addEdge(7, 9);
        graph.addEdge(2, 4);
        graph.addEdge(4, 6);
        graph.addEdge(6, 8);
        graph.addEdge(8, 10);

        graph.addEdge(1, 5);
        graph.addEdge(5, 10);
        graph.print();

        for (int i = 1; i <= 10; i++) {
            for (int j = i; j <= 10; j++) {
                if (i == j) continue;
                assertTrue(graph.hasPath(i, j));
            }
        }

        assertTrue(graph.containsEdge(1, 3));
        assertFalse(graph.containsEdge(1, 2));
        assertEquals(2, graph.getDegree(1));
        assertEquals(2, graph.getDegree(10));

        assertArraySame(graph.getNeighbors(10), 5, 8);
        assertArrayEquals(new Integer[]{1, 5, 10}, graph.shortestPath(1, 10).toArray(new Integer[0]));

        graph.removeEdge(1, 5);
        assertArrayEquals(new Integer[]{1, 3, 5, 10}, graph.shortestPath(1, 10).toArray(new Integer[0]));
        graph.removeEdge(3, 5);

        assertNull(graph.shortestPath(1, 10));
        for (int i = 6; i <= 10; i++) graph.removeVertex(i);
        assertEquals(5, graph.size());
        assertFalse(graph.hasPath(1, 5));

        System.out.println("-----------------------------------------");
        graph.print();
    }

    @SafeVarargs
    private static <E> void assertArraySame(List<E> container, E... expected) {
        assertEquals(container.size(), expected.length);
        for (E i : expected) assertTrue(container.contains(i));
    }

    @Test
    void graphAdjMat() {
        testing(new GraphAdjMat<>());
    }

    @Test
    void graphAdjList() {
        testing(new GraphAdjList<>());
    }
}
