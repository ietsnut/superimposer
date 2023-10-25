package superimposer.environment;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public class InteractiveEnvironment extends Environment implements KeyListener {

    private Polygon polygon;

    public InteractiveEnvironment(int w, int h) {
        super(w, h);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                //setShape(new Ellipse2D.Double(0,0,w,h));
                polygon = new Polygon();
                polygon = new Polygon();
                polygon.addPoint(0, 250);
                polygon.addPoint(250, 0);
                polygon.addPoint(500, 250);
                polygon.addPoint(250, 500);


                GeneralPath path = new GeneralPath();
                path.append(polygon, true);
                setShape(path);
            }
        });
        run();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
