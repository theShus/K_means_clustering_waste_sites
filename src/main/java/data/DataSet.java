package data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Random;

public class DataSet {

    private final List<Site> sites;

    public DataSet(String filePath) {
        this.sites = readSitesFromFile(filePath);
    }

    //load all sites from file
    public List<Site> readSitesFromFile(String filePath) {
        List<Site> sites = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            ObjectMapper objectMapper = new ObjectMapper();

            while ((line = br.readLine()) != null) {
                JsonNode jsonNodeArray = objectMapper.readTree(line);

                if (jsonNodeArray.isArray()) {
                    // Iterate through each element in the array
                    for (JsonNode jsonNode : jsonNodeArray) {
                        // Check if each field is present before retrieving its value, if not generate a random value
                        String name = jsonNode.has("name") ? jsonNode.get("name").asText() : "PlaceHolder Name";
                        double capacity = jsonNode.has("capacity") ? jsonNode.get("capacity").asDouble() : generateRandomDouble(0, 116024);
                        double latitude = jsonNode.has("la") ? jsonNode.get("la").asDouble() : generateRandomDouble(47, 55);
                        double longitude = jsonNode.has("lo") ? jsonNode.get("lo").asDouble() : generateRandomDouble(5, 14);

                        Site site = new Site(name, capacity, latitude, longitude);
                        sites.add(site);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sites;
    }

    public List<Site> getNSites(int numberOfSites) {
        if (sites.size() >= numberOfSites) {
            return new ArrayList<>(sites.subList(0, numberOfSites));//todo the list can be randomised every time if needed
        }
        else {
            List<Site> generatedSites = generateNSites(numberOfSites - sites.size());
            sites.addAll(generatedSites);
            return sites;
        }
    }


    private List<Site> generateNSites(int numberToGenerate) {
        List<Site> generatedSites = new ArrayList<>();
        for (int i = 0; i < numberToGenerate; i++) { //todo generate parameters can be adjusted
            generatedSites.add(new Site("Generated Site " + i, generateRandomDouble(0, 100), generateRandomDouble(35, 55), generateRandomDouble(-10, 30)));
        }
        return generatedSites;
    }




    public static Double euclideanDistance(Site site, Centroid centroid) {
        double sum = Math.pow(Math.abs(site.getLatitude() - centroid.getLatitude()), 2) + Math.pow(Math.abs(site.getLatitude() - centroid.getLatitude()), 2);
        return Math.sqrt(sum);
    }

//    public Centroid calculateWeightedCentroid(List<Centroid> centroids) {
//        double sum = 0.0;
//
//        // Calculate the sum of all minDistances to the nearest centroid
//        for (Site site : sites) {
//            if (isSiteCentroid(site, centroids))
//                continue; // Skip if the site is already a centroid (todo: improve this search mechanism)
//            double minDist = Double.MAX_VALUE;
//            // Find the minimum distance to existing centroids for the current site
//            for (Centroid centroid : centroids) {
//                double distance = euclideanDistance(site, centroid);
//                if (distance < minDist) minDist = distance;
//            }
//            sum += minDist;
//        }
//
//        // Make a threshold that we will use for calculating a new centroid
//        double threshold = sum * random.nextDouble();
//
//        for (Site site : sites) {
//            if (isSiteCentroid(site, centroids)) continue;
//            double minDist = Double.MAX_VALUE;
//            // Find the minimum distance to existing centroids for the current site
//            for (Centroid centroid : centroids) {
//                double distance = euclideanDistance(site, centroid);
//                if (distance < minDist) minDist = distance;
//            }
//            sum += minDist;
//
//            // Check if the cumulative sum exceeds the threshold
//            if (sum > threshold) {
//                // Create a new centroid using the latitude and longitude of the selected site
//                Centroid newCentroid = new Centroid(site.getLatitude(), site.getLongitude());
//                centroids.add(newCentroid);
//                return newCentroid;
//            }
//        }
//        // Return null if no new centroid is selected
//        return null;
//    }
//
//    private boolean isSiteCentroid(Site site, List<Centroid> centroids) {
//        for (Centroid centroid : centroids) {
//            if (centroid.getLatitude() == site.getLatitude() && centroid.getLongitude() == site.getLongitude())
//                return true;
//        }
//        return false;
//    }

    public static List<Centroid> recomputeCentroids(int numberOfClusters, List<Site> sites) {
        List<Centroid> centroids = new ArrayList<>();

        for (int i = 0; i < numberOfClusters; i++) {
            centroids.add(calculateCentroid(i, sites));
        }
        return centroids;
    }

    private static Centroid calculateCentroid(int clusterNo, List<Site> sites) {
        List<Site> sitesInCluster = new ArrayList<>();

        for (Site site : sites) {
            if (site.getClusterNo() == clusterNo) {
                sitesInCluster.add(site);
            }
        }

        // Calculate new cluster position based on site coordinates
        double totalX = 0.0;
        double totalY = 0.0;

        // Calculate the sum of x and y coordinates
        for (Site site : sitesInCluster) {
            totalX += site.getLatitude();
            totalY += site.getLongitude();
        }

//        System.out.println("total for " + sitesInCluster.size());
//        System.out.println(totalX);
//        System.out.println(totalY);

        // Calculate the mean of x and y coordinates
        double meanX = totalX / (double) sitesInCluster.size();
        double meanY = totalY / (double) sitesInCluster.size();

//        if (Double.isNaN(meanX) || Double.isNaN(meanY)){
//            System.out.println("KURACCCC");
//            System.out.println(meanX);
//            System.out.println(meanY);
//            System.out.println(sitesInCluster.size());
//        }

        if (Double.isNaN(meanX)) meanX = 0.0; //todo ovo je nesto lose
        if ( Double.isNaN(meanY)) meanY = 0.0;

            // Create a new centroid with the calculated mean coordinates
        return new Centroid(meanX, meanY);
    }


    /*
    In k-means clustering, SSE (Sum of Squared Errors) is a measure of how well the data points within a cluster are grouped around their centroid.
    It is calculated as the sum of the squared distances between each data point in the cluster and its centroid.
    A lower SSE indicates that the data points within the cluster are closer to the centroid suggesting a more compact and well-defined cluster.
    */
    public static double calculateTotalSSE(List<Centroid> centroids, List<Site> sites) {
        double SSE = 0.0;

        // Iterate through each centroid and calculate SSE for its cluster
        for (int i = 0; i < centroids.size(); i++) {
            SSE += calculateClusterSSE(centroids.get(i), i, sites);
        }
        return SSE;
    }

    public static Double calculateClusterSSE(Centroid centroid, int clusterNo, List<Site> sites) {
        double SSE = 0.0;

        // Iterate through each site in the cluster and calculate squared Euclidean distance to the centroid
        for (Site site : sites) {
            if (site.getClusterNo() == clusterNo)
                SSE += Math.pow(euclideanDistance(site, centroid), 2);
        }
        return SSE;
    }

    private static double generateRandomDouble(double min, double max) {
        Random random = new Random();
        return min + (max - min) * random.nextDouble();
    }

    public List<Site> getSites() {
        return sites;
    }

}
