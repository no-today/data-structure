package data.structure;

import java.util.function.Consumer;

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
     */
    void depthFirstForeach(E startVertex, Consumer<E> consumer);

    /**
     * 广度优先搜索
     */
    void breadthFirstForeach(E startVertex, Consumer<E> consumer);

    /**
     * 是否可达
     */
    boolean hasPath(E source, E destination);

    /**
     * 获取顶点数量
     */
    int size();

    /**
     * 打印邻接表
     */
    void print();
}
