import data.DataSet;
import scene.WindowFrame;
import services.ClusteringService;
import services.SequentialClustering;

import javax.swing.*;

public class Main {

    //Services
    static ClusteringService clusteringService;

    //Data
    static final String filePath = "data/germany.json";

    //Spec
    static final int numberOfClusters = 4;
    static final ServiceType serviceType = ServiceType.SEQUENTIAL;
//    static final ServiceType serviceType = ServiceType.PARALLEL;
//    static final ServiceType serviceType = ServiceType.DISTRIBUTED;



    public static void main(String[] args) {

        System.out.println("Loading data set");
        DataSet data = new DataSet(filePath);

        if (serviceType == ServiceType.SEQUENTIAL) clusteringService = new SequentialClustering(data, numberOfClusters);
//        if (serviceType == ServiceType.PARALLEL) clusteringService = new SequentialClustering(data);
//        if (serviceType == ServiceType.DISTRIBUTED) clusteringService = new SequentialClustering(data);




//        System.out.println("Starting map");
//        SwingUtilities.invokeLater(WindowFrame::new);

    }
}