package problem;

import java.util.ArrayList;

public class Graph {
    public ArrayList<Node> nodes = new ArrayList<>();
    public int size;
    private Edge[][] edges;
    private final String kind;

    public Graph(String kind) {
        this.kind = kind;
    }

    public void initEdges() {
        size = nodes.size();
        edges = new Edge[nodes.size()][nodes.size()];
        for (Node from : nodes) {
            for (Node to : nodes) {
                edges[from.nodeId() - 1][to.nodeId() - 1] = new Edge(from, to, kind);
            }
        }
    }

    public Edge edge(int fromID, int toID) {
        return edges[fromID - 1][toID - 1];
    }

    @Override
    public String toString() {
        return String.format("Graph: %d nodes", nodes.size());
    }
}
