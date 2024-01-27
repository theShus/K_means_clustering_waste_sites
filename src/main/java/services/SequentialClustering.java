package services;

import data.Centroid;
import data.DataSet;
import data.Site;
import data.TestResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SequentialClustering implements ClusteringService {

    //Starting vars
    private final DataSet data;
    private final ServiceType testingType;
    private int numberOfClusters;
    private int numberOfSites;

    //Testing vars
    private static final Double PRECISION = 0.0;
    private final Map<Integer, TestResult> resultMap = new HashMap<>();

    public SequentialClustering(DataSet data, int numberOfClusters, int numberOfSites, ServiceType testingType) {
        this.data = data;
        this.numberOfClusters = numberOfClusters;
        this.numberOfSites = numberOfSites;
        this.testingType = testingType;

        runTesting();
    }

    private void runTesting(){
        int testsRanCounter = 0;

        if (testingType == ServiceType.LOCKED_CLUSTERS){

            while (true){
//                long startTime = System.currentTimeMillis();

                TestResult testResult1 = calculateClusters();
                TestResult testResult2 = calculateClusters();
                TestResult testResult3 = calculateClusters();
                TestResult avgResult = new TestResult(testResult1, testResult2, testResult3);

                resultMap.put(testsRanCounter++, avgResult);

                if (avgResult.getNumberOfLoops() > 200){
                    break;
                }

//            todo enable time check
//        long totalTime = System.currentTimeMillis() - startTime;
//        System.err.println(totalTime);
            }

//            testResult1.print();
//            testResult2.print();
//            testResult3.print();
//
//            System.out.println("AVG");
//            avgResult.print();

        }
        else if (testingType == ServiceType.LOCKET_SITES){
            System.out.println("TODO");
        }
    }


    public TestResult calculateClusters() {
        //helper vars
        int loopCounter = 0;
        Map<Integer, Integer> clusterSizeCounter = new HashMap<>();
        for (int i = 0; i < numberOfClusters; i++) clusterSizeCounter.put(i, 0);

        //data vars
        double SSE = Double.MAX_VALUE;
        List<Centroid> centroids = calculateCentroids();
        List<Site> sites = this.data.getSites();

        while (true) {
            // for each record
            for (var site : sites) {
                double minDist = Double.MAX_VALUE;
                // find the centroid at a minimum distance from it and add the site to its cluster
                for (int i = 0; i < centroids.size(); i++) {
                    double dist = DataSet.euclideanDistance(site, centroids.get(i));
                    if (dist < minDist) {
                        minDist = dist;
                        site.setClusterNo(i);
                    }
                }
            }


            // recompute centroids according to new cluster assignments
            centroids = DataSet.recomputeCentroids(numberOfClusters, sites);

            // exit condition, SSE changed less than PRECISION parameter
            double newSSE = DataSet.calculateTotalSSE(centroids, sites);
            if (SSE - newSSE <= PRECISION) break;
            SSE = newSSE;

            loopCounter++;

//            print(loopCounter, centroids, SSE);//todo improve print
        }

        for (Site site : sites) clusterSizeCounter.put(site.getClusterNo(), clusterSizeCounter.get(site.getClusterNo()) + 1);
        return new TestResult(loopCounter, clusterSizeCounter, centroids);
    }

//    private void print(int loopCounter, List<Centroid> centroids, Double SSE){
//        System.out.println("------ Loop:" + loopCounter + " ------");
//        System.out.println("Cluster Counts:");
//        for (Map.Entry<Integer, Integer> entry : clusterSizeCounter.entrySet()) {
//            System.out.println("Cluster " + entry.getKey() + ": " + entry.getValue() + " points");
//        }
//        System.out.println("Centroids");
//        for (Centroid centroid : centroids) System.out.println(centroid);
//
//        System.out.println("SSE: " + SSE);
//        for (int i = 0; i < numberOfClusters; i++) clusterSizeCounter.put(i, 0);
//    }


    private List<Centroid> calculateCentroids() {
        List<Centroid> centroids = new ArrayList<>();

        Centroid newCentroid = data.getRandomDataPoint();
        centroids.add(newCentroid);
        data.getCentroids().add(newCentroid);

        for (int i = 0; i < numberOfClusters - 1; i++) {//todo stavi ili random ili weighted
            //weighted
//            newCentroid = data.calculateWeightedCentroid();
//            if (newCentroid != null) centroids.add(data.calculateWeightedCentroid());
//            else System.err.println("Failed to calculate centroid");

            //random
            newCentroid = data.getRandomDataPoint();
            centroids.add(newCentroid);
            data.getCentroids().add(newCentroid);
        }
        return centroids;
    }


}
