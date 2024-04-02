package data.structure;

import data.structure.hash.HashMap;
import data.structure.hash.HashSet;
import data.structure.list.LinkedList;
import data.structure.queue.LinkedQueue;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 可以理解为由一组顶点和一组边组成: 将顶点看作节点, 将边看作连接各个顶点的指针引用
 * <p>
 * 根据边是否具有方向，可分为:
 * 1. 无向图(undirected graph): 边表示两顶点直接的“双向”连接
 * 2. 有向图(directed graph): 边是单向的
 * <p>
 * 根据所有顶点是否连通, 可分为:
 * 1. 连通图(connected graph): 从某个顶点出发, 可以到达其余任意节点
 * 2. 非连通图(disconnected graph): 至少有一个节点无法到达, 即孤岛
 * <p>
 * 图数据结构包含以下常用术语:
 * 1. 邻接(adjacency): 当两个顶点之间存在边里相连时，称这两顶点“邻接”
 * 2. 路径(path): 从顶点A到顶点B经过的边构成的序列
 * 3. 度(degree): 一个顶点拥有的边(指向)数量。对于有向图, 入度(in-degree)表示被多少个顶点指向, 出度(out-degree)表示指向多少个其他顶点
 *
 * @author no-today
 * @date 2024/04/01 10:54
 */
public interface Graph<E> {

    IllegalArgumentException VERTEX_NOT_EXISTS = new IllegalArgumentException("Vertex does not exist");

    /**
     * 添加顶点
     */
    void addVertex(E vertex);

    /**
     * 添加边
     */
    void addEdge(E source, E destination);

    /**
     * 删除顶点
     */
    void removeVertex(E vertex);

    /**
     * 删除边
     */
    void removeEdge(E source, E destination);

    /**
     * 查询顶点是否存在
     */
    boolean containsVertex(E vertex);

    default void assertsVertexExists(E vertex) {
        if (!containsVertex(vertex)) throw VERTEX_NOT_EXISTS;
    }

    /**
     * 查询边是否存在
     */
    boolean containsEdge(E source, E destination);

    /**
     * 获取指定顶点的邻居顶点列表
     */
    List<E> getNeighbors(E vertex);

    /**
     * 获取指定顶点的度
     */
    int getDegree(E vertex);

    /**
     * 深度优先搜索
     * <p>
     * 走到头才回头, 再走到头才返回,
     * 以此类推...直至完成遍历
     */
    default void depthFirstForeach(E startVertex, Consumer<E> consumer) {
        assertsVertexExists(startVertex);

        dfs(startVertex, new HashSet<>(), e -> {
            consumer.accept(e);
            return false;
        });
    }

    /**
     * 深度优先遍历
     *
     * @param node     起始节点
     * @param function 消费遍历到的元素, 返回 true 结束遍历
     */
    default void dfs(E node, Set<E> visited, Function<E, Boolean> function) {
        if (!visited.add(node)) return;
        if (function.apply(node)) return;

        List<E> neighbors = getNeighbors(node);
        for (int i = 0; i < neighbors.size(); i++) {
            dfs(neighbors.get(i), visited, function);
        }
    }

    /**
     * 广度优先搜索
     * <p>
     * 由近及远、层层扩张
     */
    default void breadthFirstForeach(E startVertex, Consumer<E> consumer) {
        assertsVertexExists(startVertex);

        bfs(startVertex, e -> {
            consumer.accept(e);
            return false;
        });
    }

    /**
     * 广度优先遍历
     *
     * @param node     起始节点
     * @param function 消费遍历到的元素, 返回 true 结束遍历
     */
    default void bfs(E node, Function<E, Boolean> function) {
        Set<E> visited = new HashSet<>();
        Queue<E> queue = new LinkedQueue<>();

        visited.add(node);
        queue.enqueue(node);

        while (!queue.isEmpty()) {
            E item = queue.dequeue();
            if (function.apply(item)) break;

            List<E> neighbors = getNeighbors(item);
            for (int i = 0; i < neighbors.size(); i++) {
                E neighbor = neighbors.get(i);
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.enqueue(neighbor);
                }
            }
        }
    }

    /**
     * 判断从源点到目标点是否可达
     * <p>
     * 需要找到一条路径而不是最短路径 深度优先(DFS)是一个不错的选择
     */
    default boolean hasPath(E source, E destination) {
        assertsVertexExists(source);
        assertsVertexExists(destination);

        boolean[] hasPath = {false};
        dfs(source, new HashSet<>(), node -> {
            // 遍历的过程中遇到目标元素 说明可达
            if (node.equals(destination)) {
                hasPath[0] = true;
                return true; // 返回 true 中止遍历
            }
            return false;
        });

        return hasPath[0];
    }

    /**
     * 找到从源点到目标节点到最短路径
     * 如果不存在则返回 NULL
     * <p>
     * 广度优先(BFS)会先访问离源节点最近的节点 然后逐层向外扩展 适合用于找最短路径
     */
    default List<E> shortestPath(E source, E destination) {
        assertsVertexExists(source);
        assertsVertexExists(destination);

        Set<E> visited = new HashSet<>();
        Queue<E> queue = new LinkedQueue<>();

        visited.add(source);
        queue.enqueue(source);

        /*
         * Key 为子节点, Val 为父节点
         * 当找到目标节点时, 从目标元素开始向上回溯出最短路径
         */
        Map<E, E> parents = new HashMap<>();
        parents.put(source, null);

        while (!queue.isEmpty()) {
            E cur = queue.dequeue();
            if (cur.equals(destination)) {
                return buildShortestPath(parents, destination);
            }

            List<E> neighbors = getNeighbors(cur);
            for (int i = 0; i < neighbors.size(); i++) {
                E neighbor = neighbors.get(i);
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);

                    // 查找场景为 根据子查找父
                    parents.put(neighbor, cur);
                }
            }
        }

        return null;
    }

    default List<E> buildShortestPath(Map<E, E> parents, E destination) {
        LinkedList<E> paths = new LinkedList<>();
        E cur = destination;
        while (cur != null) {
            // 从目标节点逆向构建路径
            paths.addFirst(cur);
            cur = parents.get(cur);
        }
        return paths;
    }


    /**
     * 获取顶点数量
     */
    int size();

    /**
     * 打印邻接表
     */
    void print();
}
