import data.DataSet;
import data.Site;
import services.ClusteringService;
import services.SequentialClustering;
import services.ServiceType;

import java.util.List;

public class Main {

    //Data
    static final String filePath = "data/germany.json";
    //Services
    public static ClusteringService clusteringService;


    //Spec
    // >>>> CHANGE THESE PARAMETERS TO MODIFY THE PROGRAM <<<<
    static final ServiceType serviceType = ServiceType.SEQUENTIAL; // SEQUENTIAL / PARALLEL / DISTRIBUTED
    static final ServiceType testingType = ServiceType.LOCKED_CLUSTERS; // LOCKED_CLUSTERS / LOCKET_SITES
    static final int numberOfClusters = 20;
    static final int numberOfSites = 30000;

    public static void main(String[] args) {

        System.out.println("Loading data set");
        DataSet data = new DataSet(filePath);

        System.out.println("Loading service");
        if (serviceType == ServiceType.SEQUENTIAL)
            clusteringService = new SequentialClustering(data, testingType);

        if (testingType == ServiceType.LOCKED_CLUSTERS) clusteringService.runTesting(numberOfClusters, 500);
        if (testingType == ServiceType.LOCKET_SITES) clusteringService.runTesting(5, numberOfSites);




//        System.out.println("Starting map");
//        SwingUtilities.invokeLater(WindowFrame::new);
    }
}