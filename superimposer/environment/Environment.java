package superimposer.environment;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;

public class Environment extends JFrame {

    private Graphics2D graphics;
    private BufferedImage canvas;
    private int x, y;

    public Environment(int w, int h) {
        super();
        Environment env = this;

        add(new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                canvas = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                graphics = (Graphics2D) canvas.getGraphics();
                setBackground(Color.BLACK);
                graphics.setColor(Color.BLACK);
                graphics.fillRect(0, 0, w, h);
                draw();
                graphics.drawImage(canvas, 0, 0, this);
                repaint();
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int deltaX = e.getX() - x;
                int deltaY = e.getY() - y;
                setLocation(getLocation().x + deltaX, getLocation().y + deltaY);
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setShape(new Ellipse2D.Double(0,0,getWidth(),getHeight()));
            }
        });
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setSize(w, h);
        setVisible(true);
    }

    public void draw() {

    }

    public void color(Color color) {
        graphics.setColor(color);
    }

    public void circle(int size, int x, int y) {
        graphics.fillOval(x, y, size, size);
    }

    public void image(String img, int x, int y) {
        Image image = Toolkit.getDefaultToolkit().getImage(img);
        graphics.drawImage(image, x, y, this);
    }

    public void font(int size) {
        graphics.setFont(new Font("serif", Font.PLAIN, size));
    }

    public void text(String text, int x, int y) {
        graphics.drawString(text, x, y);
    }

}
