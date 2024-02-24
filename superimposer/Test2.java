package superimposer;

import org.jdesktop.swingx.geom.Morphing2D;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.*;

import org.w3c.dom.NodeList;

public class Test2 extends JPanel {
    private Shape startShape;
    private Shape endShape;
    private Morphing2D currentShape;
    private float currentStep = 0;

    public Test2() {
        startShape = createCustomShape1(); // Replace this with your start shape
        endShape = createCustomShape2(); // Replace this with your end shape
        currentShape = new Morphing2D(startShape, endShape);;

        Timer timer = new Timer(1, e -> {
            if (currentStep <= 1) {
                morphShapes();
                repaint();
            }
        });
        timer.start();
    }

    private void morphShapes() {
        currentShape.setMorphing(currentStep);
        currentStep += 0.01F;
    }

    private Shape createCustomShape1() {
        // Define your custom starting shape here
        return new Ellipse2D.Double(50, 50, 100, 100);
    }

    private Shape createCustomShape2() {
        // Define your custom ending shape here
        return new Rectangle2D.Double(50, 50, 100, 100);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.BLUE);
        g2d.fill(currentShape);

        g2d.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Shape Morphing");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new Test2());
            frame.setSize(250, 250);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

}
