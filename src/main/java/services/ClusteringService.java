package services;

import data.TestResult;

public interface ClusteringService {

    void runTesting(int numberOfClusters, int numberOfSites);

    TestResult calculateKMeans (int numberOfClusters, int numberOfSites);
}
