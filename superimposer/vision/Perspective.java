package superimposer.vision;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import superimposer.library.Image;

public class Perspective extends JFrame implements MouseListener, MouseMotionListener, KeyListener, MouseWheelListener {

    private Canvas canvas;
    protected int x, y;

    public Perspective(int w, int h, String border) {
        EventQueue.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {}
            System.setProperty("sun.java2d.opengl", "true");
            System.setProperty("sun.java2d.uiScale", "1");
            setUndecorated(true);
            this.canvas = new Canvas(this, w, h, border);
            setBackground(new Color(0, 0, 0, 0));
            setContentPane(canvas);
            setup();
            pack();
            setLocationRelativeTo(null);
            addMouseListener(this);
            addMouseMotionListener(this);
            addKeyListener(this);
            addMouseWheelListener(this);
            toFront();
            setFocusable(true);
            setFocusableWindowState(true);
            setVisible(true);
        });
    }
    public void setup() {

    }
    private static class Canvas extends JPanel implements ActionListener {
        private final BufferedImage border;
        private final Perspective instance;
        private Canvas(Perspective instance, int w, int h, String border) {
            super(true);
            this.instance = instance;
            this.border = new Image(border).scale(w, h, RenderingHints.VALUE_INTERPOLATION_BILINEAR).image();
            setOpaque(false);
        }
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            try {
                instance.draw(g2d);
                g2d.drawImage(border, 0, 0, getWidth(), getHeight(), this);
            } finally {
                g2d.dispose();
            }
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
        @Override
        public Dimension getPreferredSize() {
            return border == null ? new Dimension(instance.getWidth(), instance.getHeight()) : new Dimension(border.getWidth(), border.getHeight());
        }
    }

    public void draw(Graphics2D graphics) {
        
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
}
