import data.DataSet;
import services.ClusteringService;
import services.SequentialClustering;
import services.ServiceType;

public class Main {

    //Services
    static ClusteringService clusteringService;

    //Data
    static final String filePath = "data/germany.json";

    //Spec
    static final int numberOfClusters = 4;
    static final int numberOfSites = 500;


    static final ServiceType serviceType = ServiceType.SEQUENTIAL;
//    static final services.ServiceType serviceType = services.ServiceType.PARALLEL;
//    static final services.ServiceType serviceType = services.ServiceType.DISTRIBUTED;

    static final ServiceType testingType = ServiceType.LOCKED_CLUSTERS;
//    static final services.ServiceType testingType = services.ServiceType.LOCKET_SITES;



    public static void main(String[] args) {

        System.out.println("Loading data set");
        DataSet data = new DataSet(filePath);

        if (serviceType == ServiceType.SEQUENTIAL) clusteringService = new SequentialClustering(data, numberOfClusters, numberOfSites, testingType);
//        if (serviceType == services.ServiceType.PARALLEL) clusteringService = new SequentialClustering(data);
//        if (serviceType == services.ServiceType.DISTRIBUTED) clusteringService = new SequentialClustering(data);




//        System.out.println("Starting map");
//        SwingUtilities.invokeLater(WindowFrame::new);

    }
}