import algorithm.AntColony;
import problem.TSPProblem;

import java.util.Scanner;

/**
 * A Java implementation of the Ant-Colony algorithm for TSP (Travelling Salesman Problem)
 *
 * @author PEI Yuxing 20083743d
 * @since 14/12/2022
 */
public class MiniProject {
    public static void main(String[] args) {
        System.out.print("""
                Which dataset do you want to use?
                1: ch150.tsp
                2: country-capitals.json
                ENTER 1/2:""");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        TSPProblem tspProblem;
        while (!input.equals("1") && !(input.equals("2"))) {
            System.out.println("Please ENTER 1 or 2:");
            input = scanner.nextLine();
        }
        if (input.equals("1"))
            tspProblem = TSPProblem.useOfficial("ch150.tsp");
        else
            tspProblem = TSPProblem.useRealWorld("country-capitals.json");
        AntColony antColony = new AntColony(tspProblem);
        antColony.solve();
    }
}