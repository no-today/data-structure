package data.structure.graph;

import data.structure.Graph;
import data.structure.List;

import java.util.function.Consumer;

/**
 * @author no-today
 * @date 2024/04/01 16:58
 */
public class GraphAdjList<E> implements Graph<E> {

    @Override
    public void addVertex(E vertex) {

    }

    @Override
    public void addEdge(E source, E destination) {

    }

    @Override
    public void removeVertex(E vertex) {

    }

    @Override
    public void removeEdge(E source, E destination) {

    }

    @Override
    public boolean containsVertex(E vertex) {
        return false;
    }

    @Override
    public boolean containsEdge(E source, E destination) {
        return false;
    }

    @Override
    public List<E> getNeighbors(E vertex) {
        return null;
    }

    @Override
    public int getDegree(E vertex) {
        return 0;
    }

    @Override
    public void depthFirstForeach(E startVertex, Consumer<E> consumer) {

    }

    @Override
    public void breadthFirstForeach(E startVertex, Consumer<E> consumer) {

    }

    @Override
    public boolean hasPath(E source, E destination) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void print() {

    }
}
