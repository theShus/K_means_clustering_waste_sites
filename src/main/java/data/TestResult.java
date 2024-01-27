package data;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestResult {

    private int numberOfCycles;
    private Map<Integer, Integer> clusterSizeCounter;
    private List<Centroid> centroids;
    private int clusterNo;
    private int siteNo;
    private double runtime;

    boolean clusterFailed = false;//todo remove
    boolean centroidFailed = false;//todo remove

    public TestResult(int numberOfCycles, Map<Integer, Integer> clusterSizeCounter, List<Centroid> centroids, int clusterNo, int siteNo, double runtime) {
        this.numberOfCycles = numberOfCycles;
        this.clusterSizeCounter = clusterSizeCounter;
        this.centroids = centroids;
        this.clusterNo = clusterNo;
        this.siteNo = siteNo;
        this.runtime = runtime;
    }

    //Get avg results from 3 tests ran
    public TestResult(TestResult testResult1, TestResult testResult2, TestResult testResult3) {
        DecimalFormat df = new DecimalFormat("#.#####");

        numberOfCycles = (testResult1.numberOfCycles + testResult2.numberOfCycles + testResult3.numberOfCycles) / 3;

        //in case something breaks and list are not same size
        if (testResult1.clusterSizeCounter.size() != testResult2.clusterSizeCounter.size() && testResult2.clusterSizeCounter.size() != testResult3.clusterSizeCounter.size()) {
            clusterFailed = true;
            this.clusterSizeCounter = null;
        }
        else this.clusterSizeCounter = calculateMapMean(testResult1.clusterSizeCounter, testResult2.clusterSizeCounter, testResult3.clusterSizeCounter);

        if (testResult1.centroids.size() != testResult2.centroids.size() && testResult2.centroids.size() != testResult3.centroids.size()) {
            centroidFailed = true;
            this.centroids = null;
        }
        else this.centroids = calculateListMean(testResult1.centroids, testResult2.centroids, testResult3.centroids);

        this.clusterNo = (testResult1.clusterNo + testResult2.clusterNo + testResult3.clusterNo) / 3;
        this.siteNo = (testResult1.siteNo + testResult2.siteNo + testResult3.siteNo) / 3;
        this.runtime = Double.parseDouble(df.format((testResult1.runtime + testResult2.runtime + testResult3.runtime) / 3));
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


//            if (Double.isNaN(meanCentroid.getLatitude()) || Double.isNaN(meanCentroid.getLongitude()) ){
//                System.out.println("KURAC");
//                System.out.println(centroid1);
//                System.out.println(centroid2);
//                System.out.println(centroid3);
//                System.out.println(meanCentroid);
//            }

            meanList.add(meanCentroid);
        }
        return meanList;
    }

    public void print() {
        System.out.println("Cycles: " + numberOfCycles + " | Clusters: " + clusterNo + " | Sites: " + siteNo + " | Runtime: " + runtime);

        System.out.println("Cluster failed: " + clusterFailed + " centroid failed: " + centroidFailed);

        System.out.println("\n*** Cluster Counts: ***");
        for (Map.Entry<Integer, Integer> entry : clusterSizeCounter.entrySet())
            System.out.println("Cluster " + entry.getKey() + ": " + entry.getValue() + " sites");

        System.out.println("\n*** Centroids: ***");
        for (int i = 0; i < centroids.size(); i++)
            System.out.println("Centroid " + i + " : " + centroids.get(i));
    }

    public int getNumberOfCycles() {
        return numberOfCycles;
    }

    public void setNumberOfCycles(int numberOfCycles) {
        this.numberOfCycles = numberOfCycles;
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

    public int getClusterNo() {
        return clusterNo;
    }

    public void setClusterNo(int clusterNo) {
        this.clusterNo = clusterNo;
    }

    public int getSiteNo() {
        return siteNo;
    }

    public void setSiteNo(int siteNo) {
        this.siteNo = siteNo;
    }

    public double getRuntime() {
        return runtime;
    }

    public void setRuntime(double runtime) {
        this.runtime = runtime;
    }

}
