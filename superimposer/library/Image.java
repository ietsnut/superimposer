package superimposer.library;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.IOException;

public class Image {

    private BufferedImage image;

    public Image(String name) {
        try {
            this.image = ImageIO.read(getClass().getResource("/resource/" + name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Image threshold () {
        BufferedImage copy = new BufferedImage(this.image.getWidth(), this.image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2 = copy.createGraphics();
        g2.drawImage(this.image, 0, 0, null);
        g2.dispose();
        this.image = copy;
        return this;
    }

    public Image scale(int w, Object value) {
        double ratio = (double) this.image.getHeight() / this.image.getWidth();
        int h = (int) (w * ratio);
        BufferedImage resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, value);
        g.drawImage(this.image, 0, 0, w, h, 0, 0, this.image.getWidth(), this.image.getHeight(), null);
        g.dispose();
        this.image = resized;
        return this;
    }

    public Image scale(int w, int h, Object value) {
        BufferedImage resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, value);
        g.drawImage(this.image, 0, 0, w, h, 0, 0, this.image.getWidth(), this.image.getHeight(), null);
        g.dispose();
        this.image = resized;
        return this;
    }

    public Image pad(int size, int padding, Object value) {
        if (this.image.getWidth() > size - padding || this.image.getHeight() > size - padding) {
            scale(size - padding, value);
        }
        BufferedImage resultImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resultImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, size, size);
        int drawX = (size - this.image.getWidth()) / 2;
        int drawY = (size - this.image.getHeight()) / 2;
        g.drawImage(this.image, drawX, drawY, null);
        g.dispose();
        this.image = resultImage;
        return this;
    }

    public Image invert() {
        int width = this.image.getWidth();
        int height = this.image.getHeight();
        BufferedImage inverted = new BufferedImage(width, height, this.image.getType());
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgba = this.image.getRGB(x, y);
                Color originalColor = new Color(rgba, true);

                int red = 255 - originalColor.getRed();
                int green = 255 - originalColor.getGreen();
                int blue = 255 - originalColor.getBlue();

                Color invertedColor = new Color(red, green, blue, originalColor.getAlpha());
                inverted.setRGB(x, y, invertedColor.getRGB());
            }
        }
        this.image = inverted;
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
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
        VolatileImage volatileImage = gc.createCompatibleVolatileImage(this.image.getWidth(), this.image.getHeight(), Transparency.TRANSLUCENT);
        Graphics2D g2d = volatileImage.createGraphics();
        g2d.drawImage(this.image, 0, 0, null);
        g2d.dispose();
        return volatileImage;
    }

    public BufferedImage image() {
        return this.image;
    }

    public ImageIcon icon() {
        return new ImageIcon(this.image);
    }

}
