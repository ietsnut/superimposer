package superimposer.windows;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;

public class Window extends JFrame implements MouseListener, MouseMotionListener {

    protected BufferedImage canvas;
    protected Graphics2D graphics;
    protected int x, y;

    public Window(int w, int h, int screen, Shape shape) {
        setUndecorated(true);
        setShape(new Area(shape));
        setSize(new Dimension(w, h));
        setPreferredSize(new Dimension(w, h));
        pack();
        Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[screen].getDefaultConfiguration().getBounds();
        setLocation(bounds.x + bounds.width / 2 - w / 2, bounds.y + bounds.height / 2 - h / 2);
        this.canvas = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        this.graphics = canvas.createGraphics();
        this.graphics.setColor(Color.BLACK);
        this.graphics.fillRect(0, 0, w, h);
        addMouseListener(this);
        addMouseMotionListener(this);
        toFront();
        setFocusable(true);
        setFocusableWindowState(true);
        repaint();
        setVisible(true);
    }

    public void draw() {

    }

    @Override
    public void paint(Graphics g) {
        draw();
        g.drawImage(this.canvas, 0, 0, this);
        Toolkit.getDefaultToolkit().sync();
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

}
