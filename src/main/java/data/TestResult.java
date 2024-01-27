package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestResult {

    private int numberOfLoops;
    private Map<Integer, Integer> clusterSizeCounter;
    private List<Centroid> centroids;

    public TestResult(int numberOfLoops, Map<Integer, Integer> clusterSizeCounter, List<Centroid> centroids) {
        this.numberOfLoops = numberOfLoops;
        this.clusterSizeCounter = clusterSizeCounter;
        this.centroids = centroids;
    }

    //Get avg results from 3 tests ran
    public TestResult(TestResult testResult1, TestResult testResult2, TestResult testResult3) {
        numberOfLoops = (testResult1.numberOfLoops + testResult2.numberOfLoops + testResult3.numberOfLoops) / 3;

        //in case something breaks and list are not same size
        if (testResult1.clusterSizeCounter.size() != testResult2.clusterSizeCounter.size() && testResult2.clusterSizeCounter.size() != testResult3.clusterSizeCounter.size())
            clusterSizeCounter = null;
        else clusterSizeCounter = calculateMapMean(testResult1.clusterSizeCounter, testResult2.clusterSizeCounter, testResult3.clusterSizeCounter);

        if (testResult1.centroids.size() != testResult2.centroids.size() && testResult2.centroids.size() != testResult3.centroids.size())
            centroids = null;
        else centroids = calculateListMean(testResult1.centroids, testResult2.centroids, testResult3.centroids);
    }

    private Map<Integer, Integer> calculateMapMean(Map<Integer, Integer> map1, Map<Integer, Integer> map2, Map<Integer, Integer> map3) {
        Map<Integer, Integer> meanMap = new HashMap<>();

        for (int key : map1.keySet()) {
            int meanValue = (map1.getOrDefault(key, 0) + map2.getOrDefault(key, 0) + map3.getOrDefault(key, 0)) / 3;
            meanMap.put(key, meanValue);
        }
        return meanMap;
    }

    private static List<Centroid> calculateListMean(List<Centroid> list1, List<Centroid> list2, List<Centroid> list3) {
        List<Centroid> meanList = new ArrayList<>();

        for (int i = 0; i < list1.size(); i++) {
            Centroid centroid1 = list1.get(i);
            Centroid centroid2 = list2.get(i);
            Centroid centroid3 = list3.get(i);
            double meanLatitude = (centroid1.getLatitude() + centroid2.getLatitude() + centroid3.getLatitude()) / 3.0;
            double meanLongitude = (centroid1.getLongitude() + centroid2.getLongitude() + centroid3.getLongitude()) / 3.0;
            Centroid meanCentroid = new Centroid(meanLatitude, meanLongitude);
            meanList.add(meanCentroid);
        }
        return meanList;
    }

    public int getNumberOfLoops() {
        return numberOfLoops;
    }

    public void setNumberOfLoops(int numberOfLoops) {
        this.numberOfLoops = numberOfLoops;
    }

    public Map<Integer, Integer> getClusterSizeCounter() {
        return clusterSizeCounter;
    }

    public void setClusterSizeCounter(Map<Integer, Integer> clusterSizeCounter) {
        this.clusterSizeCounter = clusterSizeCounter;
    }

    public List<Centroid> getCentroids() {
        return centroids;
    }

    public void setCentroids(List<Centroid> centroids) {
        this.centroids = centroids;
    }

    public void print(){
        System.out.println("\n ------ Loops: " + numberOfLoops + " ------");

        System.out.println("*** Cluster Counts: ***");
        for (Map.Entry<Integer, Integer> entry : clusterSizeCounter.entrySet())
            System.out.println("Cluster " + entry.getKey() + ": " + entry.getValue() + " points");

        System.out.println("*** Centroids: ***");
        for (Centroid centroid : centroids) System.out.println(centroid);
    }
}
