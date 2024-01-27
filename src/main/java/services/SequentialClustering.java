package services;

import com.sun.tools.javac.Main;
import data.Centroid;
import data.DataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SequentialClustering implements ClusteringService{

    DataSet data;
    int numberOfClusters = 0;

    public SequentialClustering(DataSet data, int numberOfClusters) {
        this.data = data;
        this.numberOfClusters = numberOfClusters;

        calculateClusters();
    }

    //get centroids
    //  get first random centorid
    //  calculate rest of weighted centroids

    //loop

    public void calculateClusters(){
        List <Centroid> centroids = calculateCentroids();

        for (Centroid centroid: centroids) System.out.println(centroid);



    }



    private List<Centroid> calculateCentroids(){
        List <Centroid> centroids = new ArrayList<>();

        Centroid newCentroid = data.getRandomDataPoint();
        centroids.add(newCentroid);
        data.getCentroids().add(newCentroid);

        for (int i = 0; i < numberOfClusters - 1; i++) {
            newCentroid = data.calculateWeightedCentroid();
            if (newCentroid != null) centroids.add(data.calculateWeightedCentroid());
            else System.err.println("Failed to calculate centroid");
//            newCentroid = data.getRandomDataPoint(); //todo probaj sa random umesto weighted
//            centroids.add(newCentroid);
//            data.getCentroids().add(newCentroid);
        }

        return centroids;
    }




}
