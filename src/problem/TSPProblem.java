package problem;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

@Data
public class TSPProblem {
    private Graph graph;

    private TSPProblem(String kind) {
        graph = new Graph(kind);
    }

    public static TSPProblem useOfficial(String fileName) {
        TSPProblem tspProblem = new TSPProblem("EUC_2D");
        URL res = TSPProblem.class.getClassLoader().getResource(fileName);
        try (FileInputStream fileInputStream = new FileInputStream(Objects.requireNonNull(res).getFile())) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                Scanner scanner = new Scanner(line);
                String firstWord = scanner.next();
                try {
                    int index = Integer.parseInt(firstWord);
                    double xCord = scanner.nextDouble();
                    double yCord = scanner.nextDouble();
                    tspProblem.graph.nodes.add(new Node(index, String.valueOf(index), xCord, yCord, ""));
                } catch (NumberFormatException numberFormatException) {
                    System.out.println(line);
                } catch (InputMismatchException inputMismatchException) {
                    System.err.println("File Broken!");
                    System.exit(-1);
                }
            }
            tspProblem.graph.initEdges();
            reader.close();
        } catch (NullPointerException | IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("You are using TSP official dataset, benchmarks for TSP can be found in ⬇");
        System.out.println("http://staff.iiar.pwr.wroc.pl/wojciech.bozejko/Benchmarks/TSP/ALL_tsp.tar.gz");
        System.out.printf("The optimal solution is stored in '%s'\n", fileName.replace(".tsp", ".opt.tour"));
        System.out.println("================================================================================");
        System.out.println(tspProblem.graph + " initialized");
        return tspProblem;
    }

    public static TSPProblem useRealWorld(String fileName) {
        TSPProblem tspProblem = new TSPProblem("LAT_LONG");
        URL res = TSPProblem.class.getClassLoader().getResource(fileName);

        try (FileInputStream fileInputStream = new FileInputStream(Objects.requireNonNull(res).getFile())) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line);
            JSONArray jsonArray = JSONArray.parseArray(stringBuilder.toString());
            HashSet<String> record = new HashSet<>();
            int index = 1;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String countryName = jsonObject.getString("CountryName");
                String capitalName = jsonObject.getString("CapitalName");
                double latitude = jsonObject.getDouble("CapitalLatitude");
                double longitude = jsonObject.getDouble("CapitalLongitude");
                if ((latitude == 0 && longitude == 0) || record.contains(capitalName))
                    continue;
                record.add(capitalName);
                tspProblem.graph.nodes.add(new Node(index++, capitalName,  longitude, latitude, countryName));
            }
            tspProblem.graph.initEdges();
        } catch (NullPointerException | IOException e) {
            System.err.println("File Broken!");
            System.exit(-1);
        }
        System.out.println("================================================================================");
        System.out.println(tspProblem.graph + " initialized");
        return tspProblem;
    }
}
