import data.WWSite;
import scene.WindowFrame;

import javax.swing.*;
import java.util.List;

import static data.DataLoader.readSitesFromFile;

public class Main {

    public static final String filePath = "data/germany.json";


    public static void main(String[] args) {

        System.out.println("Loading data set");
        List<WWSite> cities = readSitesFromFile(filePath);

//        for (data.WWSite site : cities) {
//            System.out.println(site.toString());
//        }

        System.out.println("Starting map");
        SwingUtilities.invokeLater(WindowFrame::new);

    }

}