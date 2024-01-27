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
//        for (data.WWSite site : sites) {
//            System.out.println(site.getLatitude() + " - " + site.getLongitude());
//        }
    }

    //CHAT GPT generated ( was too lazy to write it myself :) )
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
                        // Check if each field is present before retrieving its value
                        String name = jsonNode.has("name") ? jsonNode.get("name").asText() : "";
                        double capacity = jsonNode.has("capacity") ? jsonNode.get("capacity").asDouble() : 0.0;
                        double latitude = jsonNode.has("la") ? jsonNode.get("la").asDouble() : 0.0;
                        double longitude = jsonNode.has("lo") ? jsonNode.get("lo").asDouble() : 0.0;

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
    public Centroid getRandomDataPoint(){
        int randomIndex = random.nextInt(sites.size());
        WWSite randomSIte = sites.get(randomIndex);
        return new Centroid(randomSIte.getLatitude(), randomSIte.getLongitude());
    }

    public Double euclideanDistance(WWSite site, Centroid centroid){
        double sum = Math.pow(site.getLongitude() - site.getLatitude(), 2) + Math.pow(centroid.getLongitude() - centroid.getLatitude(), 2);
        return Math.sqrt(sum);
    }

    public Centroid calculateWeightedCentroid() {
        double sum = 0.0;

        // Calculate the sum of all minDistances to the nearest centroid
        for (WWSite site : sites) {
            if (isSiteCentroid(site)) continue; // Skip if the site is already a centroid (todo: improve this search mechanism)
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


    private boolean isSiteCentroid(WWSite site){
        for (Centroid centroid: centroids) {
            if (centroid.getLatitude() == site.getLatitude() && centroid.getLongitude() == site.getLongitude()) return true;
        }
        return false;
    }


    public void calculateCentroid(){

    }

    public void calculateClusterSSE(){

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
