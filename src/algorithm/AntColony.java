package algorithm;

import lombok.SneakyThrows;
import problem.Edge;
import problem.Graph;
import problem.Node;
import problem.TSPProblem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class AntColony {
    final int ANT_NUM = 200;
    final double ALPHA = 2.0;
    final double BETA = 3.0;
    final double RHO = 0.5;
    final double Q = 1.0;
    private final double[][] pheromone;
    Ant[] ants;
    Graph graph;

    public AntColony(TSPProblem problem) {
        this.graph = problem.getGraph();
        this.pheromone = new double[graph.size][graph.size];
        this.ants = new Ant[ANT_NUM];
        Random random = new Random();
        for (int i = 0; i < ANT_NUM; i++) {
            this.ants[i] = new Ant(this, i);
            this.ants[i].setRandom(random);
        }
        for (Node from : graph.nodes) {
            for (Node to : graph.nodes) {
                pheromone[from.nodeId() - 1][to.nodeId() - 1] = 1.0;
            }
        }
    }

    public void solve() {
        double ans = Double.MAX_VALUE;
        Edge[] routes = new Edge[graph.size];
        for (int i = 0; i < 100; i++) {
            startAll();
            for (Ant ant : ants) {
                if (ant.getDistance() < ans) {
                    ans = ant.getDistance();
                    routes = Arrays.copyOf(ant.getRoutes(), routes.length);
                }
            }
        }
        System.out.println(ans);
        System.out.println("Full report can be viewed in 'report.txt'");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("report.txt"));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Report given by the Ant-Colony algorithm\n");
            stringBuilder.append(String.format("\nTotal Cost: %f\n", ans));
            stringBuilder.append(String.format("Number of Nodes: %d\n", graph.size));
            stringBuilder.append(String.format("""
                    Ant Number: %d
                    ALPHA:      %f
                    BETA:       %f
                    RHO:        %f
                    Q:          %f
                    """, ANT_NUM, ALPHA, BETA, RHO, Q));
            stringBuilder.append("\nTravelling Plan").append("-".repeat(100)).append("\n");
            for (int i = 0; i < routes.length; i++) {
                stringBuilder.append(String.format("Step %-4d: FROM %-35s TO %-35s COST %-20f\n",
                        i + 1, routes[i].getFrom().name() + " - " + routes[i].getFrom().note(),
                        routes[i].getTo().name() + " - " + routes[i].getTo().note(), routes[i].getDistance()));
            }
            writer.write(stringBuilder.toString());
            writer.close();
        } catch (IOException e) {
            System.err.println("Report fails!");
        }
    }

    @SneakyThrows
    public void startAll() {
        Thread[] threads = new Thread[ANT_NUM];
        for (int i = 0; i < ANT_NUM; i++) {
            threads[i] = new Thread(ants[i]);
            threads[i].start();
        }
        for (int i = 0; i < ANT_NUM; i++) {
            threads[i].join();
        }
        updatePheromone();
    }

    private void updatePheromone() {
        for (Node from : graph.nodes) {
            for (Node to : graph.nodes) {
                pheromone[from.nodeId() - 1][to.nodeId() - 1] *= (1 - RHO);
            }
        }
        for (Ant ant : ants) {
            for (Edge edge : ant.getRoutes()) {
                double increment = Q / ant.getDistance();
                pheromone[edge.getFrom().nodeId() - 1][edge.getTo().nodeId() - 1] += increment;
            }
        }
    }

    public double pheromone(Edge edge) {
        return pheromone[edge.getFrom().nodeId() - 1][edge.getTo().nodeId() - 1];
    }
}
