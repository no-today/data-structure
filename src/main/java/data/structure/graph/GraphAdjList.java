package data.structure.graph;

import data.structure.Graph;
import data.structure.List;
import data.structure.Map;
import data.structure.hash.HashMap;
import data.structure.list.LinkedList;

/**
 * 基于邻接表实现的无向图
 * <p>
 * 区别于邻接矩阵, 领接表(adjacency list)只记录实际存在的边(指向)
 *
 * @author no-today
 * @date 2024/04/01 16:58
 */
public class GraphAdjList<E> implements Graph<E> {

    private final Map<E, List<E>> adjList;

    public GraphAdjList() {
        this.adjList = new HashMap<>();
    }

    @Override
    public void addVertex(E vertex) {
        if (adjList.containsKey(vertex)) return;
        adjList.put(vertex, new LinkedList<>());
    }

    @Override
    public void addEdge(E source, E destination) {
        assertsVertexExists(source);
        assertsVertexExists(destination);

        adjList.get(source).add(destination);
        adjList.get(destination).add(source);
    }

    @Override
    public void removeVertex(E vertex) {
        assertsVertexExists(vertex);

        // 删除本身
        adjList.remove(vertex);
        // 删除其他节点的引用
        adjList.values().foreach((e, i) -> e.remove(vertex));
    }

    @Override
    public void removeEdge(E source, E destination) {
        assertsVertexExists(source);
        assertsVertexExists(destination);

        adjList.get(source).remove(destination);
        adjList.get(destination).remove(source);
    }

    @Override
    public boolean containsVertex(E vertex) {
        return adjList.containsKey(vertex);
    }

    @Override
    public boolean containsEdge(E source, E destination) {
        assertsVertexExists(source);
        assertsVertexExists(destination);

        return adjList.get(source).contains(destination);
    }

    @Override
    public List<E> getNeighbors(E vertex) {
        assertsVertexExists(vertex);

        return adjList.get(vertex);
    }

    @Override
    public int getDegree(E vertex) {
        assertsVertexExists(vertex);

        return adjList.get(vertex).size();
    }

    @Override
    public int size() {
        return adjList.size();
    }

    @Override
    public void print() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Ves\t\tRefs\n");
        adjList.entrySet().foreach((e, i) -> sb.append(e.getKey()).append("\t\t").append(e.getValue()).append("\n"));
        return sb.toString();
    }
}
