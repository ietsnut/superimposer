package superimposer.vision;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.anim.dom.SVGOMPolygonElement;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGPathElement;
import superimposer.library.Image;
import java.util.StringTokenizer;

public class Perspective extends JFrame implements MouseListener, MouseMotionListener, KeyListener, MouseWheelListener {

    protected BufferStrategy buffer;
    protected VolatileImage border;
    protected int x, y;

    public Perspective(int w, int h, String border, String shape) {
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("sun.java2d.uiScale", "1");
        setUndecorated(true);
        setBackground(Color.BLACK);
        this.border = new Image(border).scale(w, h, RenderingHints.VALUE_INTERPOLATION_BILINEAR).render();
        setShape(new Area(shape(shape)));
        //setShape(new Ellipse2D.Double(0, 0, w, h));
        setSize(new Dimension(w, h));
        setPreferredSize(new Dimension(w, h));
        pack();
        setLocationRelativeTo(null);
        createBufferStrategy(2);
        this.buffer = getBufferStrategy();
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        addMouseWheelListener(this);
        toFront();
        setFocusable(true);
        setFocusableWindowState(true);
        setVisible(true);
        repaint();
    }

    public void draw(Graphics graphics) {
        
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphics = (Graphics2D) buffer.getDrawGraphics();
        try {
            graphics.setBackground(Color.BLACK);
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, getWidth(), getHeight());
            draw(graphics);
            graphics.drawImage(border, 0, 0, getWidth(), getHeight(), this);
        } finally {
            graphics.dispose();
        }
        buffer.show();
        Toolkit.getDefaultToolkit().sync();
        //repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            x = e.getX();
            y = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            x = e.getX();
            y = e.getY();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            int x2 = e.getX() - x;
            int y2 = e.getY() - y;
            setLocation(getLocation().x + x2, getLocation().y + y2);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

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

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

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
