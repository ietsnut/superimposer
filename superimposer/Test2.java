package superimposer;

import org.jdesktop.swingx.geom.Morphing2D;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.*;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.anim.dom.SVGOMPolygonElement;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

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

    private Shape shape(String name) {
        File file = new File("resource/" + name + ".svg");
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
        SVGDocument svgDoc = null;
        try {
            svgDoc = (SVGDocument) factory.createDocument(file.toURI().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        NodeList pathList = svgDoc.getElementsByTagName("polygon");
        SVGOMPolygonElement pathElement = (SVGOMPolygonElement) pathList.item(0);
        String pathData = pathElement.getAttribute("points");
        System.out.println(pathData);
        Path2D path = new Path2D.Double();
        String[] tokens = pathData.split("\\s+");
        for (String token : tokens) {
            String[] coordinates = token.split(",");
            double x = Double.parseDouble(coordinates[0]) * 2.1;
            double y = Double.parseDouble(coordinates[1]) * 2.1;

            if (path.getCurrentPoint() == null) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }

        path.closePath();
        path.trimToSize();
        System.out.println(path.getBounds2D());
        return path;
    }
}
