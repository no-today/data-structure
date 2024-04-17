package data.structure.graph;

import data.structure.Graph;
import data.structure.List;
import data.structure.list.ArrayList;

/**
 * 基于邻接矩阵实现的无向图
 * <p>
 * 设图的顶点数量为 n, 邻接矩阵(adjacency matrix)使用一个 n*n 大小的矩阵来表示图
 * 每一行(列)代表一个顶点, 矩阵元素代表边, 用 0 或 1 表示是否连接
 *
 * @author no-today
 * @date 2024/04/01 11:28
 */
public class GraphAdjMat<E> implements Graph<E> {

    /**
     * 顶点
     */
    private final List<E> vertices;

    /**
     * 邻接矩阵 维护顶点之间的指向
     */
    private final List<List<Integer>> adjMat;

    public GraphAdjMat() {
        vertices = new ArrayList<>();
        adjMat = new ArrayList<>();
    }

    @Override
    public void addVertex(E vertex) {
        if (vertices.contains(vertex)) return;

        int n = size();

        // 添加顶点
        vertices.add(vertex);

        // 初始化行
        List<Integer> newRow = new ArrayList<>(n);
        for (int i = 0; i < n; i++) newRow.add(0);
        adjMat.add(newRow);

        // 初始化列
        for (int i = 0; i < adjMat.size(); i++) adjMat.get(i).add(0);
    }

    @Override
    public void addEdge(E source, E destination) {
        int sIndex = vertices.indexOf(source);
        int dIndex = vertices.indexOf(destination);
        if (sIndex == dIndex || sIndex == -1 || dIndex == -1) throw VERTEX_NOT_EXISTS;

        adjMat.get(sIndex).set(dIndex, 1);
        adjMat.get(dIndex).set(sIndex, 1);
    }

    @Override
    public void removeVertex(E vertex) {
        int index = vertices.indexOf(vertex);
        if (index == -1) throw VERTEX_NOT_EXISTS;

        vertices.remove(index);
        adjMat.remove(index);
        for (int i = 0; i < adjMat.size(); i++) {
            adjMat.get(i).remove(index);
        }
    }

    @Override
    public void removeEdge(E source, E destination) {
        int sIndex = vertices.indexOf(source);
        int dIndex = vertices.indexOf(destination);
        if (sIndex == -1 || dIndex == -1)
            throw VERTEX_NOT_EXISTS;

        adjMat.get(sIndex).set(dIndex, 0);
        adjMat.get(dIndex).set(sIndex, 0);
    }

    @Override
    public boolean containsVertex(E vertex) {
        return vertices.contains(vertex);
    }

    @Override
    public boolean containsEdge(E source, E destination) {
        int sIndex = vertices.indexOf(source);
        int dIndex = vertices.indexOf(destination);
        if (sIndex == -1 || dIndex == -1) throw VERTEX_NOT_EXISTS;

        return adjMat.get(sIndex).get(dIndex) != 0;
    }

    @Override
    public List<E> getNeighbors(E vertex) {
        int index = vertices.indexOf(vertex);

        ArrayList<E> neighbors = new ArrayList<>();
        List<Integer> refs = adjMat.get(index);
        for (int i = 0; i < refs.size(); i++) {
            if (refs.get(i) != 0) neighbors.add(vertices.get(i));
        }
        return neighbors;
    }

    @Override
    public int getDegree(E vertex) {
        int index = vertices.indexOf(vertex);
        if (index == -1) throw VERTEX_NOT_EXISTS;

        int count = 0;
        List<Integer> refs = adjMat.get(index);
        for (int i = 0; i < refs.size(); i++) {
            if (refs.get(i) != 0) count++;
        }
        return count;
    }

    @Override
    public int size() {
        return vertices.size();
    }

    @Override
    public void print() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Vertices with index:\n");
        sb.append("Ves\t");

        for (int i = 0; i < vertices.size(); i++) {
            sb.append(vertices.get(i));
            if (i < vertices.size() - 1) sb.append("\t");
        }
        sb.append("\n");

        sb.append("Idx\t");
        for (int i = 0; i < adjMat.size(); i++) {
            sb.append(i);
            if (i < adjMat.size() - 1) sb.append("\t");
        }
        sb.append("\n");

        for (int i = 0; i < size(); i++) {
            sb.append(i).append("\t");

            List<Integer> rows = adjMat.get(i);
            for (int j = 0; j < rows.size(); j++) {
                Integer cols = rows.get(j);
                sb.append(cols);
                if (j < rows.size() - 1) sb.append("\t");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
