package scene;

import java.awt.*;

public class Window extends Frame {


    private final Scene scene = new Scene();
    Panel centrePanel = new Panel();


    public Window() throws HeadlessException {
        setBounds(200, 300, 800, 600);
        setResizable(false);//todo
        setTitle("K-means clustering");
        populateWindow();

        //extra

        setVisible(true);
    }

    private void populateWindow() {
        scene.setBackground(Color.GRAY);
        scene.setPreferredSize(new Dimension(getWidth() - 20, getHeight() - 45));
        centrePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        centrePanel.add(scene);
        scene.requestFocus();

        add(centrePanel, BorderLayout.CENTER);
    }
}
