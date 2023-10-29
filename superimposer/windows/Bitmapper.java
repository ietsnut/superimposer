package superimposer.windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

public class Bitmapper extends Window {

    int prevX, prevY;
    boolean drawing;

    public Bitmapper() {
        super(500, 500, new Ellipse2D.Double(0,0,500,500));
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setColor(Color.WHITE);
        graphics.fillOval(50,50, getWidth() - 100, getHeight()- 100);
        graphics.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.drawOval(25, 25, getWidth() - 50, getHeight()- 50);
    }

    private void drawLine(int x1, int y1, int x2, int y2) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.drawLine(x1, y1, x2, y2);
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        if (SwingUtilities.isLeftMouseButton(e)) {
            prevX = e.getX();
            prevY = e.getY();
            drawing = true;
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
                drawLine(prevX, prevY, x, y);
                prevX = x;
                prevY = y;
            }
        }
    }

}
