package superimposer.vision;

import superimposer.notation.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.IOException;

public class Perception extends Perspective {

    BufferedImage background;
    BufferedImage border;

    public Perception(int w, int h, String border, ImageIcon background) throws IOException {
        super(w, h, border);
        this.border = null;
        this.background = (BufferedImage) background.getImage();
    }

    @Override
    public void draw(Graphics2D graphics) {

    }

}
