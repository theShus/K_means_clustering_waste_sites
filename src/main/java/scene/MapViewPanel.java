package scene;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;

public class MapViewPanel extends JPanel {

    private final JXMapViewer mapViewer;

    public MapViewPanel() {
        setLayout(new BorderLayout());

        mapViewer = new JXMapViewer();
        initMap();
        add(new JScrollPane(mapViewer), BorderLayout.CENTER);
    }


    public void initMap() {
        //set up initial location and zoom of the map
        mapViewer.setTileFactory(new DefaultTileFactory(new OSMTileFactoryInfo("", "https://tile.openstreetmap.de")));
        mapViewer.setAddressLocation(new GeoPosition(50.132298, 8.679860));
        mapViewer.setZoom(13);

        //set up mouse listener so we can move the map
        MouseInputListener mouseListener = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mouseListener);
        mapViewer.addMouseMotionListener(mouseListener);
        //set up scroll listener for zoom/un-zoom map
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));
    }

    //todo https://www.youtube.com/watch?v=A2LCUbvJEZw


}
