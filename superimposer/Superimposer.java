package superimposer;

import superimposer.windows.Bitmapper;
import superimposer.windows.IDE;
import superimposer.windows.Keyboard;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class Superimposer {

    public static Superimposer instance;

    public static int w;
    public static int h;

    public Bitmapper bitmapper;
    public Keyboard keyboard;
    public IDE ide;

    public Superimposer() {
        instance = this;
        this.bitmapper = new Bitmapper();
        this.keyboard = new Keyboard();
        this.ide = new IDE();
    }

    public static void main(String[] args) throws IOException {
        new Superimposer();
    }

    static BufferedImage bimage(String path) {
        try {
            BufferedImage img = ImageIO.read(Superimposer.class.getResource(path));
            return img;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
