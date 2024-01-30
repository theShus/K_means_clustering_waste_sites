package scene;

import data.TestResult;
import javax.swing.*;
import java.awt.*;


public class WindowFrame extends JFrame {


    public WindowFrame(TestResult testResult) throws HeadlessException {
        setBounds(200, 300, 800, 600);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("K-means clustering");

        MapViewPanel mapViewPanel = new MapViewPanel();
        getContentPane().add(mapViewPanel);

        //todo pokazi i sliku pre i sliku posle

        mapViewPanel.addDots(testResult.getSites(), testResult.getClusterNo(), testResult.getCentroids() );

        setVisible(true);
    }

}
