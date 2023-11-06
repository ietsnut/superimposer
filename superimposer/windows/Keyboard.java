package superimposer.windows;

import superimposer.Superimposer;
import superimposer.notation.Cardinality;
import superimposer.notation.Preposition;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Keyboard extends Window {

    ArrayList<Preposition> prepositions = new ArrayList<>();

    public Keyboard() {
        super(1000, 500, new RoundRectangle2D.Double(0, 0, 1000, 500, 100, 100));
        for (Cardinality cardinality : Cardinality.values()) {
            prepositions.add(new Preposition(Superimposer.bimage(cardinality.name().toLowerCase() + ".png").getScaledInstance(300, 150, Image.SCALE_SMOOTH), cardinality));
        }
    }

    @Override
    public void draw() {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setBackground(Color.BLACK);
        graphics.clearRect(0, 0, getWidth(), getHeight());
        graphics.setColor(Color.WHITE);
        int x = 25;
        int y = 25;
        for (Preposition preposition : prepositions) {
            graphics.drawImage(preposition.image, x, y, 200, 100, this);
            x += 250;
            if (x > 800) {
                y += 125;
                x = 25;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        int x = 25;
        int y = 25;
        for (Preposition preposition : prepositions) {
            if (e.getX() > x && e.getX() < x + 200 && e.getY() > y && e.getY() < y + 100) {
                if (!Superimposer.code.units.isEmpty() && !(Superimposer.code.units.get(Superimposer.code.units.size() - 1) instanceof Preposition)) {
                    Superimposer.code.units.add(preposition);
                    Superimposer.code.repaint();
                    break;
                }
            }
            x += 250;
            if (x > 800) {
                y += 125;
                x = 25;
            }
        }
    }




}
