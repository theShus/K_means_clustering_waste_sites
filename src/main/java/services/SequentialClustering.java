package services;

import data.Centroid;
import data.DataSet;
import data.WWSite;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SequentialClustering implements ClusteringService {

    DataSet data;
    int numberOfClusters = 0;
    static final Double PRECISION = 0.0;

    public SequentialClustering(DataSet data, int numberOfClusters) {
        this.data = data;
        this.numberOfClusters = numberOfClusters;

        System.err.println(data.getSites().size());
        calculateClusters();
    }


    public void calculateClusters() {
        List<Centroid> centroids = calculateCentroids();

//        for (Centroid centroid : centroids) System.out.println(centroid);

        double SSE = Double.MAX_VALUE;


        //todo remove
        int loopCounter = 0;
        Map<Integer, Integer> clusterCounter = new HashMap<>();
        for (int i = 0; i < numberOfClusters; i++) clusterCounter.put(i, 0);


        while (true) {
            System.out.println("----------------------");
            System.out.println("LOOP " + loopCounter);
            System.out.println("Cluster Counts:");
            for (Map.Entry<Integer, Integer> entry : clusterCounter.entrySet()) {
                System.out.println("Cluster " + entry.getKey() + ": " + entry.getValue() + " points");
            }
            System.out.println("Centroids");
            for (Centroid centroid : centroids) System.out.println(centroid);
            System.out.println("SSE: " + SSE);
            loopCounter++;

            for (int i = 0; i < numberOfClusters; i++) clusterCounter.put(i, 0);



            // for each record
            for (var site : data.getSites()) {
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

            for (WWSite site: data.getSites()) {
                clusterCounter.put(site.getClusterNo(), clusterCounter.get(site.getClusterNo()) + 1);
            }


//            System.out.println("LOOP " + loopCounter);
//            System.out.println("Cluster Counts:");
//            for (WWSite site: data.getSites()) {
//                clusterCounter.put(site.getClusterNo(), clusterCounter.get(site.getClusterNo()) + 1);
//            }
//            for (Map.Entry<Integer, Integer> entry : clusterCounter.entrySet()) {
//                System.out.println("Cluster " + entry.getKey() + ": " + entry.getValue() + " points");
//            }
//            System.out.println("Centroids");
//            for (Centroid centroid : centroids) System.out.println(centroid);
//            System.out.println("SSE: " + SSE);
//            System.out.println("----------------------");


            // recompute centroids according to new cluster assignments
            centroids = data.recomputeCentroids(numberOfClusters);

            // exit condition, SSE changed less than PRECISION parameter
            double newSSE = data.calculateTotalSSE(centroids);

            System.out.println("+++++++++");
            System.out.println("RECOMPUTED");
            for (Centroid centroid : centroids) System.out.println(centroid);
            System.out.println("new SSE");
            System.out.println(newSSE);
            System.out.println("+++++++++");

            if (SSE - newSSE <= PRECISION) {
                break;
            }
            SSE = newSSE;
        }
    }


    private List<Centroid> calculateCentroids() {
        List<Centroid> centroids = new ArrayList<>();

        Centroid newCentroid = data.getRandomDataPoint();
        centroids.add(newCentroid);
        data.getCentroids().add(newCentroid);

        for (int i = 0; i < numberOfClusters - 1; i++) {//todo stavi ili random ili weighted
//            newCentroid = data.calculateWeightedCentroid();
//            if (newCentroid != null) centroids.add(data.calculateWeightedCentroid());
//            else System.err.println("Failed to calculate centroid");
            newCentroid = data.getRandomDataPoint();
            centroids.add(newCentroid);
            data.getCentroids().add(newCentroid);
        }

        return centroids;
    }


}
