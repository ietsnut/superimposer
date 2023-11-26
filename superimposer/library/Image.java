package superimposer.library;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class Image {

    private static final GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    private BufferedImage image;

    public Image(String name) {
        java.awt.Image resource = new ImageIcon("resource/" + name + ".jpg").getImage();
        this.image = new BufferedImage(resource.getWidth(null), resource.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = this.image.createGraphics();
        g2.drawImage(resource, 0, 0, null);
        g2.dispose();
    }

    public Image threshold () {
        BufferedImage copy = new BufferedImage(this.image.getWidth(), this.image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2 = copy.createGraphics();
        g2.drawImage(this.image, 0, 0, null);
        g2.dispose();
        this.image = copy;
        return this;
    }

    public Image scale(float s, Object value) {
        int w = (int) (this.image.getWidth() * s);
        int h = (int) (this.image.getHeight() * s);
        BufferedImage resized = new BufferedImage(w, h, this.image.getType());
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, value);
        g.drawImage(this.image, 0, 0, w, h, 0, 0, this.image.getWidth(), this.image.getHeight(), null);
        g.dispose();
        this.image = resized;
        return this;
    }

    public Image scale(int w, int h, Object value) {
        BufferedImage resized = new BufferedImage(w, h, this.image.getType());
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, value);
        g.drawImage(this.image, 0, 0, w, h, 0, 0, this.image.getWidth(), this.image.getHeight(), null);
        g.dispose();
        this.image = resized;
        return this;
    }

    public Image dither() {
        Dither.floydSteinbergDithering(this.image);
        return this;
    }

    public Image trim() {
        int top = 0, bottom = this.image.getHeight() - 1, left = 0, right = this.image.getWidth() - 1;
        searchTop:
        for (int y = 0; y < this.image.getHeight(); y++) {
            for (int x = 0; x < this.image.getWidth(); x++) {
                if (this.image.getRGB(x, y) != Color.WHITE.getRGB()) {
                    top = y;
                    break searchTop;
                }
            }
        }
        searchBottom:
        for (int y = this.image.getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x < this.image.getWidth(); x++) {
                if (this.image.getRGB(x, y) != Color.WHITE.getRGB()) {
                    bottom = y;
                    break searchBottom;
                }
            }
        }
        searchLeft:
        for (int x = 0; x < this.image.getWidth(); x++) {
            for (int y = 0; y < this.image.getHeight(); y++) {
                if (this.image.getRGB(x, y) != Color.WHITE.getRGB()) {
                    left = x;
                    break searchLeft;
                }
            }
        }
        searchRight:
        for (int x = this.image.getWidth() - 1; x >= 0; x--) {
            for (int y = 0; y < this.image.getHeight(); y++) {
                if (this.image.getRGB(x, y) != Color.WHITE.getRGB()) {
                    right = x;
                    break searchRight;
                }
            }
        }
        int newWidth = right - left + 1;
        int newHeight = bottom - top + 1;
        BufferedImage trimmedImage = new BufferedImage(newWidth, newHeight, image.getType());
        Graphics2D g2 = trimmedImage.createGraphics();
        g2.drawImage(this.image.getSubimage(left, top, newWidth, newHeight), 0, 0, null);
        g2.dispose();
        this.image = trimmedImage;
        return this;
    }

    public VolatileImage render() {
        VolatileImage render = gc.createCompatibleVolatileImage(this.image.getWidth(), this.image.getHeight(), Transparency.TRANSLUCENT);
        Graphics2D g = render.createGraphics();
        g.drawImage(this.image, 0, 0, null);
        g.dispose();
        return render;
    }

    public BufferedImage image() {
        return this.image;
    }

    public ImageIcon icon() {
        return new ImageIcon(this.image);
    }

}
