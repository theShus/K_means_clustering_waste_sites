import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

//    final String filePath = "data/germany.json";
//    List<WWSite> cities = readSitesFromFile(filePath);


    public DataLoader() {
//        System.out.println("loading data");
//        for (WWSite site : cities) {
//            System.out.println(site.toString());
//        }
    }


    //CHAT GPT generated ( was too lazy to write it myself :) )
    static List<WWSite> readSitesFromFile(String filePath) {
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
}
