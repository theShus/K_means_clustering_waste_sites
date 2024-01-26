package scene;

import javax.swing.*;
import java.awt.*;

public class WindowFrame extends JFrame {



    public WindowFrame() throws HeadlessException {
        setBounds(200, 300, 800, 600);
        setResizable(false);//todo
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("K-means clustering");

        MapViewPanel mapViewPanel = new MapViewPanel();
        getContentPane().add(mapViewPanel);

        setVisible(true);
    }

//    private void populateWindow() {
////        scene.setBackground(Color.OPAQUE);
//        scene.setPreferredSize(new Dimension(getWidth() - 20, getHeight() - 45));
//        centrePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
//        centrePanel.add(scene);
//        scene.requestFocus();
//
//        add(centrePanel, BorderLayout.CENTER);
//    }
}
