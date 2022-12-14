package algorithm;

import lombok.Data;
import problem.Edge;
import problem.Graph;

import java.util.Random;

@Data
public class Ant implements Runnable {
    private AntColony colony;
    private int ID;
    private Random random;
    private Graph graph;
    private int currentID;
    private int counter;
    private double distance;
    private boolean[] visited;
    private Edge[] routes;

    public Ant(AntColony colony, int ID) {
        this.colony = colony;
        this.ID = ID;
    }

    @Override
    public void run() {
        graph = colony.graph;
        visited = new boolean[graph.size + 1];
        routes = new Edge[graph.size];
        currentID = random.nextInt(graph.size) + 1;
        counter = 0;
        distance = 0;
        visited[currentID] = true;
        while (counter < graph.size - 1) {
            int nextID = chooseNext();
            visited[nextID] = true;
            routes[counter] = graph.edge(currentID, nextID);
            distance += routes[counter].getDistance();
            currentID = nextID;
            counter++;
        }
        routes[counter] = graph.edge(currentID, routes[0].getFrom().nodeId());
    }

    private int chooseNext() {
        double[] probability = new double[graph.size + 1];
        double totalProb = 0;
        probability[0] = 0;
        for (int destID = 1; destID <= graph.size; destID++) {
            if (destID == currentID || visited[destID])
                probability[destID] = 0;
            else {
                Edge edge = graph.edge(currentID, destID);
                double prob = Math.pow(colony.pheromone(edge), colony.ALPHA) *
                              Math.pow(1 / edge.getDistance(), colony.BETA);
                probability[destID] += prob;
                totalProb += prob;
            }
        }
        int decision;
        double roll = random.nextDouble(totalProb);
        for (decision = 1; decision < graph.size; decision++) {
            if (roll < probability[decision])
                break;
            else roll -= probability[decision];
        }
        return decision;
    }
}
