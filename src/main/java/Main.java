import data.DataSet;
import data.TestResult;
import scene.WindowFrame;
import services.ClusteringService;
import services.SequentialClustering;
import services.TestingType;

import javax.swing.*;
import java.util.Scanner;

public class Main {

    //Data
    static final String filePath = "data/germany.json";

    //Services
    public static ClusteringService clusteringService;

    //
    private static final Scanner scanner = new Scanner(System.in);


    //Spec
    // >>>> CHANGE THESE PARAMETERS TO MODIFY THE PROGRAM <<<<
//    static final TestingType TESTING_TYPE = TestingType.SEQUENTIAL; // SEQUENTIAL / PARALLEL / DISTRIBUTED
//    static final TestingType testingType = TestingType.LOCKED_CLUSTERS; // LOCKED_CLUSTERS / LOCKET_SITES
//    static int numberOfClusters = 20;
//    static int numberOfSites = 30000;

    public static void main(String[] args) {
        DataSet data = new DataSet(filePath);
        int parameter;

        System.out.println("\nWelcome to k-means waste facility clustering simulation.\nPlease select you desired simulation parameters.\n");
        System.out.println("""
                Select desired processing type (insert number):
                    1. Sequential
                    2. Parallel
                    3. Distributed""");
        parameter = scanner.nextInt();

        System.out.println("Loading desired processing type...\n");
        switch (parameter){
            case 1 -> clusteringService = new SequentialClustering(data);
            case 2 -> System.out.println("Work in progress...");
            case 3 -> System.out.println("Work in progress...");
        }

        System.out.println("""
                Select desired work (insert number):
                    1. Testing the algorithm
                    2. Running the visual simulation""");
        parameter = scanner.nextInt();

        if (parameter == 1) {
            System.out.println("""
                    Select desired testing type (insert number):
                        1. Testing with LOCKED CLUSTERS and ramping sites
                        2. Testing with LOCKED SITES and ramping clusters""");
            parameter = scanner.nextInt();
            if (parameter == 1) {
                System.out.println("Please insert the desired number of clusters");
                clusteringService.setNumberOfClustersAndSites(scanner.nextInt(), 500);
                clusteringService.setTestingType(TestingType.LOCKED_CLUSTERS);
            } else if (parameter == 2) {
                System.out.println("Please insert the desired number of sites");
                clusteringService.setNumberOfClustersAndSites(5, scanner.nextInt());
                clusteringService.setTestingType(TestingType.LOCKET_SITES);
            }
            clusteringService.runTesting();
        }
        else if (parameter == 2) {
            System.out.println("Please insert the desired number of clusters");
            int clusters = scanner.nextInt();
            System.out.println("Please insert the desired number of sites (above 11k are randomly generated)");
            clusteringService.setNumberOfClustersAndSites(clusters, scanner.nextInt());

            TestResult testResult = clusteringService.calculateKMeans();
            testResult.printData();
            SwingUtilities.invokeLater(() -> new WindowFrame(testResult));
        }
    }
}