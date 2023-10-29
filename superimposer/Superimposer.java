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

    public static int w;
    public static int h;

    record Unit(BufferedImage image) {}

    static class Association {
        int x, y, vx, vy, s;
        BufferedImage image;
        Unit[] units;
        public Association(int x, int y, int vx, int vy, int s, BufferedImage image, Unit... units) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.s = s;
            this.image = image;
            this.units = units;
        }
        public void update() {
            if (x + vx + s < Superimposer.w && x + vx > 0) { x += vx; } else { vx = -vx; };
            if (y + vy + s < Superimposer.h && y + vy > 0) { y += vy; } else { vy = -vy; };
        }
    }

    static ArrayList<Unit> units = new ArrayList<>();
    static ArrayList<Association> associations = new ArrayList<>();

    // AN

    public static void main(String[] args) throws IOException {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        w = (int) size.getWidth();
        h = (int) size.getHeight();

        new Bitmapper();
        new Keyboard();
        new IDE();

        //new UnitEnvironment();

        /*
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        w = (int) size.getWidth();
        h = (int) size.getHeight();

        units.add(new Unit(bimage("obj003.jpg"))); // 0
        units.add(new Unit(bimage("obj004.jpg"))); // 1
        units.add(new Unit(bimage("obj005.jpg"))); // 2
        units.add(new Unit(bimage("obj006.jpg"))); // 3
        units.add(new Unit(bimage("obj007.jpg"))); // 4

        associations.add(new Association(300, h/2 - 300, 0, 0, 300, bimage("obj001.jpg"), units.get(3)));
        associations.add(new Association(600, h/2 - 300, 0, 0, 300, bimage("obj011.jpg"), units.get(3), units.get(2)));
        associations.add(new Association(100, 0, 1, 0, 400, bimage("obj012.jpg"), units.get(3)));
        associations.add(new Association(w/2 + w/4, h/2 - 600, 1, 0, 800, bimage("obj009.jpg"), units.get(3), units.get(1)));

        InteractiveEnvironment environment = new InteractiveEnvironment(800, 800) {
            @Override
            public void draw() {
                for (Association association : associations) {
                    association.update();
                    image(association.image, association.x, association.y, association.s, association.s);
                }
                color(Color.WHITE);
                rect(0, Superimposer.h/2, Superimposer.w, Superimposer.h/2);
            }
        };
*/
        /*
        DevelopmentEnvironment ide1 = new DevelopmentEnvironment(width/2, height/8);

        final int rows = height/25;
        final int cols = width/25;
        final int cellSize = 25;

        Noise noise = new Noise(1);
        noise.SetNoiseType(Noise.NoiseType.Perlin);
        Noise noise2 = new Noise(2);
        noise2.SetNoiseType(Noise.NoiseType.OpenSimplex2S);
        SwingUtilities.invokeLater(() -> {
            InteractiveEnvironment env = new InteractiveEnvironment(500, 500) {
                @Override
                public void draw() {
                    int sx = getLocationOnScreen().x;
                    int sy = getLocationOnScreen().y;

                    color(Color.WHITE);

                    for (int row = 0; row < rows; row++) {
                        for (int col = 0; col < cols; col++) {
                            int x = col * cellSize;
                            int y = row * cellSize;
                            double noiseValue = noise.GetNoise(x, y);
                            double noiseValue2 = noise2.GetNoise(x, y);

                            if (noiseValue > 0.1 && noiseValue2 < 0.5) {
                                font(25);
                                text("🌲", x - sx, y - sy);
                            }

                            if (noiseValue2 > 0.8) {
                                font(25);
                                text("\uD83D\uDC80", x - sx, y - sy);
                            }
                        }
                    }
                }
            };
        });
        */
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
