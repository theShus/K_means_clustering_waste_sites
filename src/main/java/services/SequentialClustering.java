package services;

import data.Centroid;
import data.DataSet;
import data.Site;
import data.TestResult;
import java.text.DecimalFormat;
import java.util.*;

public class SequentialClustering implements ClusteringService {

    //Starting vars
    private final DataSet data;
    private final ServiceType testingType;
    private int numberOfClusters;
    private int numberOfSites;

    //Testing vars
    private final Map<Integer, TestResult> resultMap = new HashMap<>();
    private List<Site> sites;

    //Set up vars
    private static final Double PRECISION = 0.0;
    private static final int NUM_SITES_TO_INCREASE = 500;
    private static final int NUM_CLUSTERS_TO_INCREASE = 5;
    private static final double RUN_TIME_BLOCK = 10.0;//sec

    public SequentialClustering(DataSet data, ServiceType testingType) {
        this.data = data;
        this.testingType = testingType;
    }

    @Override
    public void runTesting(int numberOfClusters, int numberOfSites) {
        this.numberOfClusters = numberOfClusters;
        this.numberOfSites = numberOfSites;
        int testsCycledCounter = 0;
        double startTime, totalRunTime = 0.0;
        sites = data.getNSites(this.numberOfSites);

        System.out.println("Running tests...");

        while (true) {
            startTime = System.currentTimeMillis();

            TestResult testResult1 = calculateClusters(sites);
            TestResult testResult2 = calculateClusters(sites);
            TestResult testResult3 = calculateClusters(sites);
            TestResult avgResult = new TestResult(testResult1, testResult2, testResult3);
            resultMap.put(testsCycledCounter++, avgResult);

            totalRunTime += (System.currentTimeMillis() - startTime) / 1000.0;

            if (totalRunTime > RUN_TIME_BLOCK) {
                System.err.println("BREAK DUE TO EXCEEDING TIME LIMIT");
                break;
            }

            if (testingType == ServiceType.LOCKED_CLUSTERS) {
                this.numberOfSites += NUM_SITES_TO_INCREASE;// test demands we increase the num of sites by N every test cycle
                sites = data.getNSites(this.numberOfSites);
            }
            else if (testingType == ServiceType.LOCKET_SITES) {
                this.numberOfClusters += NUM_CLUSTERS_TO_INCREASE;// test demands we increase the num of sites by 500 every test cycle
                if (numberOfClusters >= numberOfSites / 3) {
                    System.err.println("BREAK DUE TO EXCEEDING CLUSTER LIMIT");
                    break;
                }
            }
        }
        printTestResults(testsCycledCounter, totalRunTime);
    }

    @Override
    public TestResult calculateKMeans(int numberOfClusters, int numberOfSites) {
        this.numberOfClusters = numberOfClusters;
        this.numberOfSites = numberOfSites;
        System.out.println("Calculating...");
        sites = data.getNSites(this.numberOfSites);
        return calculateClusters(sites);
    }


    public TestResult calculateClusters(List<Site> sites) {
        //data vars
        double SSE = Double.MAX_VALUE;
        List<Centroid> centroids = calculateCentroids(sites); //using different centroids in all 3 runs in a test

        //helper vars
        int cycleCounter = 0;
        long startTime = System.currentTimeMillis();
        Map<Integer, Integer> clusterSizeCounter = new HashMap<>();
        for (int i = 0; i < numberOfClusters; i++) clusterSizeCounter.put(i, 0);
        double minDist, dist, newSSE;

        while (true) {
            for (Site site : sites) {
                minDist = Double.MAX_VALUE;
                // find the centroid at a minimum distance from it and add the site to its cluster
                for (int i = 0; i < centroids.size(); i++) {
                    dist = DataSet.euclideanDistance(site, centroids.get(i));
                    if (dist < minDist) {
                        minDist = dist;
                        site.setClusterNo(i);
                    }
                }
            }

            // recompute centroids according to new cluster assignments
            centroids = DataSet.recomputeCentroids(numberOfClusters, sites);

            // exit condition, SSE changed less than PRECISION parameter
            newSSE = DataSet.calculateTotalSSE(centroids, sites);
            if (SSE - newSSE <= PRECISION) break;
            SSE = newSSE;

            cycleCounter++;
        }

        for (Site site : sites) {
            clusterSizeCounter.put(site.getClusterNo(), clusterSizeCounter.get(site.getClusterNo()) + 1);
        }

        long totalTime = System.currentTimeMillis() - startTime;
        //return a result of testing (time is parsed do its not 10 decimals long)

        return new TestResult(cycleCounter, clusterSizeCounter, centroids, this.numberOfClusters, totalTime / 1000.0, sites);
    }

    private List<Centroid> calculateCentroids(List<Site> sites) {
        List<Centroid> centroids = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numberOfClusters; i++) {
            while (true) {
                int randomIndex = random.nextInt(sites.size());
                Site randomSite = sites.get(randomIndex);
                Centroid randomCentroid = new Centroid(randomSite.getLatitude(), randomSite.getLongitude());

                // Check if centroids list already contains the generated centroid
                if (!centroids.contains(randomCentroid)) {
                    centroids.add(randomCentroid);
                    break; // Break out of the while loop if the centroid is unique
                }
                // If the generated centroid already exists, generate a new one
            }
        }

        return centroids;
    }


    private void printTestResults(int testsCycledCounter, double totalRunTime) {//todo check for remaining -1
        DecimalFormat df = new DecimalFormat("#.#####");
        String cyanColorCode = "\u001B[36m", resetColorCode = "\u001B[0m";

        //Print results
        for (Map.Entry<Integer, TestResult> entry : resultMap.entrySet()) {
            System.out.println("\n====== Test No: " + entry.getKey() + " ======");
            entry.getValue().printData();
            System.out.println("==================");
        }

        System.out.println(cyanColorCode + "\n* TEST RESULTS *\n" + resetColorCode);
        System.out.println("Total run time (sec): " + Double.parseDouble(df.format((totalRunTime))));
        System.out.println("Total tests done: " + testsCycledCounter);
        System.out.println("Current number of clusters: " + this.numberOfClusters);
        System.out.println("Current number of sites: " + this.numberOfSites);
        System.out.println("Coordinates of centroids and sizes of clusters are visible in the last test cycle");

    }

}
