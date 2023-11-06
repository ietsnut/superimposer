package superimposer;

import superimposer.windows.Bitmapper;
import superimposer.windows.Code;
import superimposer.windows.Keyboard;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Superimposer {

    public static Superimposer instance;

    public static Bitmapper bitmapper;
    public static Keyboard keyboard;
    public static Code code;

    public Superimposer() {
        instance = this;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                bitmapper = new Bitmapper();
                keyboard = new Keyboard();
                code = new Code();
            }
        });
    }

    public static void main(String[] args) {
        new Superimposer();
    }

    public static BufferedImage bimage(String path) {
        try {
            BufferedImage img = ImageIO.read(ClassLoader.getSystemResource(path));
            return img;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
