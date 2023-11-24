package superimposer.vision;

import superimposer.notation.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;

public abstract class Perception extends Perspective {

    Association association;
    BufferedImage background;
    BufferedImage border;

    public Perception(int w, int h, Shape shape, ImageIcon background) {
        super(w, h, shape);
        // USE VolatileImage
        this.border = null;
        this.background = (BufferedImage) background.getImage();
    }

    @Override
    public void draw() {
        graphics.drawImage(border, 0, 0, this);
        int x = (int) Math.min(getLocationOnScreen().getX(), background.getWidth(this) - getWidth());
        int y = (int) Math.min(getLocationOnScreen().getY(), background.getHeight(this) - getHeight());
        graphics.drawImage(background.getSubimage(x, y, getWidth(), getHeight()), 0, 0, null);
        for (Unit child : association.units) {
            AffineTransform transform = new AffineTransform();
            transform.translate(child.x, child.y);
            /*
            transform.rotate(child.r());
            transform.scale(child.s(), child.s());
             */
            graphics.drawImage(child.icon.getImage(), transform, this);
        }
    }

}
