package superimposer.windows;

import superimposer.Superimposer;
import superimposer.notation.Unit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Bitmapper extends Window {

    int prevX, prevY;
    boolean drawing, drew = false;
    ArrayList<Line> lines = new ArrayList<>();

    public Bitmapper() {
        super(500, 500, new Ellipse2D.Double(0,0,500,500));
        graphics.setBackground(Color.BLACK);
    }

    @Override
    public void draw() {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (Line line : lines) {
            graphics.setColor(Color.WHITE);
            graphics.setStroke(new BasicStroke(15, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            graphics.drawLine(line.from_x, line.from_y, line.to_x, line.to_y);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
        lines.clear();
        graphics.setBackground(Color.BLACK);
        graphics.clearRect(0, 0, getWidth(), getHeight());
        drew = false;
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        if (drew) {
            Superimposer.code.units.add(new Unit(clone(canvas)));
            Superimposer.code.repaint();
        }
        lines.clear();
        graphics.setBackground(Color.BLACK);
        graphics.clearRect(0, 0, getWidth(), getHeight());

        drew = false;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        if (SwingUtilities.isLeftMouseButton(e)) {
            prevX = e.getX();
            prevY = e.getY();
            drawing = true;
            drew = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        if (SwingUtilities.isLeftMouseButton(e)) {
            drawing = false;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (drawing) {
                int x = e.getX();
                int y = e.getY();
                lines.add(new Line(prevX, prevY, x, y));
                prevX = x;
                prevY = y;
                repaint();
            }
        }
    }

    record Line(int from_x, int from_y, int to_x, int to_y) {}

    public BufferedImage clone(BufferedImage image) {
        BufferedImage clone = new BufferedImage(image.getWidth(),
                image.getHeight(), image.getType());
        Graphics2D g2d = clone.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return clone;
    }

}
