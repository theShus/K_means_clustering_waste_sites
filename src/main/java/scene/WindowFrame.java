package scene;

import data.Site;
import data.TestResult;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class WindowFrame extends JFrame {


    public WindowFrame(TestResult testResult) throws HeadlessException {
        setBounds(200, 300, 800, 600);
        setResizable(false);//todo
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("K-means clustering");

        MapViewPanel mapViewPanel = new MapViewPanel();
        getContentPane().add(mapViewPanel);

        mapViewPanel.addDots(testResult.getSites(), testResult.getClusterNo(), testResult.getCentroids() );

        setVisible(true);
    }

}
