package data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataSet {
    private final List<WWSite> sites;
    private List<Centroid> centroids;
    private static final Random random = new Random();

    public DataSet(String filePath) {
        this.sites = readSitesFromFile(filePath);
        centroids = new ArrayList<>();
//        for (data.WWSite site : sites) System.out.println(site.getLatitude() + " - " + site.getLongitude());
    }


    //load all sites from file
    public List<WWSite> readSitesFromFile(String filePath) {
        List<WWSite> sites = new ArrayList<>();

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

                        WWSite site = new WWSite(name, capacity, latitude, longitude);
                        sites.add(site);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sites;
    }


    //finds a random site for the first centroid (rest are found using weighted centroid calculation)
    public Centroid getRandomDataPoint() {
        int randomIndex = random.nextInt(sites.size());
        WWSite randomSIte = sites.get(randomIndex);
        return new Centroid(randomSIte.getLatitude(), randomSIte.getLongitude());
    }

    public static Double euclideanDistance(WWSite site, Centroid centroid) {
        double sum = Math.pow(Math.abs(site.getLatitude() - centroid.getLatitude()), 2) + Math.pow(Math.abs(site.getLatitude() - centroid.getLatitude()), 2);
        return Math.sqrt(sum);
    }

    public Centroid calculateWeightedCentroid() {
        double sum = 0.0;

        // Calculate the sum of all minDistances to the nearest centroid
        for (WWSite site : sites) {
            if (isSiteCentroid(site))
                continue; // Skip if the site is already a centroid (todo: improve this search mechanism)
            double minDist = Double.MAX_VALUE;
            // Find the minimum distance to existing centroids for the current site
            for (Centroid centroid : centroids) {
                double distance = euclideanDistance(site, centroid);
                if (distance < minDist) minDist = distance;
            }
            sum += minDist;
        }

        // Make a threshold that we will use for calculating a new centroid
        double threshold = sum * random.nextDouble();

        for (WWSite site : sites) {
            if (isSiteCentroid(site)) continue;
            double minDist = Double.MAX_VALUE;
            // Find the minimum distance to existing centroids for the current site
            for (Centroid centroid : centroids) {
                double distance = euclideanDistance(site, centroid);
                if (distance < minDist) minDist = distance;
            }
            sum += minDist;

            // Check if the cumulative sum exceeds the threshold
            if (sum > threshold) {
                // Create a new centroid using the latitude and longitude of the selected site
                Centroid newCentroid = new Centroid(site.getLatitude(), site.getLongitude());
                centroids.add(newCentroid);
                return newCentroid;
            }
        }
        // Return null if no new centroid is selected
        return null;
    }


    private boolean isSiteCentroid(WWSite site) {
        for (Centroid centroid : centroids) {
            if (centroid.getLatitude() == site.getLatitude() && centroid.getLongitude() == site.getLongitude())
                return true;
        }
        return false;
    }

    public List<Centroid> recomputeCentroids(int numberOfClusters) {
        List<Centroid> centroids = new ArrayList<>();

        for (int i = 0; i < numberOfClusters; i++) {
            centroids.add(calculateCentroid(i));
        }
        return centroids;
    }


    private Centroid calculateCentroid(int clusterNo) {
        List<WWSite> sitesInCluster = new ArrayList<>();

        for (WWSite site: sites){
            if (site.getClusterNo() == clusterNo){
                sitesInCluster.add(site);
            }
        }

        // Calculate new cluster position based on site coordinates
        double totalX = 0.0;
        double totalY = 0.0;

        // Calculate the sum of x and y coordinates
        for (WWSite site : sitesInCluster) {
            totalX += site.getLatitude();
            totalY += site.getLongitude();
        }

        // Calculate the mean of x and y coordinates
        double meanX = totalX / sitesInCluster.size();
        double meanY = totalY / sitesInCluster.size();

        // Create a new centroid with the calculated mean coordinates
        return new Centroid(meanX, meanY);
    }


    /*
    In k-means clustering, SSE (Sum of Squared Errors) is a measure of how well the data points within a cluster are grouped around their centroid.
    It is calculated as the sum of the squared distances between each data point in the cluster and its centroid.
    A lower SSE indicates that the data points within the cluster are closer to the centroid suggesting a more compact and well-defined cluster.
    */
    public double calculateTotalSSE(List<Centroid> centroids) {
        double SSE = 0.0;

        // Iterate through each centroid and calculate SSE for its cluster
        for (int i = 0; i < centroids.size(); i++) {
            SSE += calculateClusterSSE(centroids.get(i), i);
        }
        return SSE;
    }

    public Double calculateClusterSSE(Centroid centroid, int clusterNo) {
        double SSE = 0.0;

        // Iterate through each site in the cluster and calculate squared Euclidean distance to the centroid
        for (WWSite site : sites) {
            if (site.getClusterNo() == clusterNo)
                SSE += Math.pow(euclideanDistance(site, centroid), 2);
        }
        return SSE;
    }

    private static double generateRandomDouble(double min, double max) {
        Random random = new Random();
        return min + (max - min) * random.nextDouble();
    }

    public List<WWSite> getSites() {
        return sites;
    }

    public List<Centroid> getCentroids() {
        return centroids;
    }

    public void setCentroids(List<Centroid> centroids) {
        this.centroids = centroids;
    }


}
