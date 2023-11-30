package superimposer.vision;

import superimposer.library.Image;
import superimposer.notation.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class AssociationView extends Perspective {

    public Perspective keyboard;


    public AssociationView() {
        super(1000, 1000, "f002.png");
        this.keyboard = new Perspective(240, 660, "f001.png") {
            final ArrayList<Unit> units = new ArrayList<>();
            int scroll = 0;
            @Override
            public void setup() {
                super.setup();
                InputStream inputStream = getClass().getResourceAsStream("/resource/unit");
                if (inputStream != null) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                        String resource;
                        while ((resource = reader.readLine()) != null) {
                            units.add(new Unit(new Image(resource).threshold().pad(160, 10, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR).invert().threshold().icon()));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void draw(Graphics2D graphics) {
                BufferedImage grid = new BufferedImage(160, 450, BufferedImage.TYPE_BYTE_BINARY);
                Graphics2D g = grid.createGraphics();
                int h = 0;
                for (Unit unit : units) {
                    java.awt.Image icon = unit.icon.getImage();
                    g.drawImage(icon, 0, h - scroll, this);
                    h += icon.getHeight(this);
                }
                g.dispose();
                graphics.drawImage(grid, 50, 140, this);
            }
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
                scroll += e.getWheelRotation() * 50;
                repaint();
            }
        };
    }
/*
    @Override
    public void draw(Graphics2D graphics) {
        int i = 0;
        for (Unit unit : units) {
            if (unit instanceof Preposition) {
                graphics.drawImage(unit.icon.getImage(), i * 300, 0, this);
            } else {
                graphics.drawImage(unit.icon.getImage(), (i * 300) + 100, 25, 100, 100, this);
                //graphics.drawImage(Code.unit, i * 300, 0, this);
            }
            i++;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char keyChar = e.getKeyChar();
        if (keyChar == KeyEvent.VK_BACK_SPACE) {
            if (!units.isEmpty()) {
                units.remove(units.size() - 1);
                repaint();
            }
        }
    }
*/
}
