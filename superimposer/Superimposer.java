package superimposer;

import superimposer.environment.Environment;
import superimposer.library.Noise;

import java.awt.*;
import java.util.*;

public class Superimposer {

    static record Object(int x, int y) {}

    static class Association {int x; int y; int s;}

    static ArrayList<Object> objects = new ArrayList<>();

    public static void main(String[] args) {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) size.getWidth();
        int height = (int) size.getHeight();

        final int rows = height/25;
        final int cols = width/25;
        final int cellSize = 25;

        Noise noise = new Noise(1);
        noise.SetNoiseType(Noise.NoiseType.Perlin);
        Noise noise2 = new Noise(2);
        noise2.SetNoiseType(Noise.NoiseType.OpenSimplex2S);

        Environment environment = new Environment(500, 500) {
            @Override
            public void draw() {
                //* GENERAL USAGE CODE *//

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
    }
}
