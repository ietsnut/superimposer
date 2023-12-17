package superimposer.vision;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.VolatileImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.*;

import superimposer.Superimposer;
import superimposer.library.Image;

public class Bartender extends Perspective {

    ArrayList<VolatileImage> drinks = new ArrayList<>();
    ArrayList<VolatileImage> qrs = new ArrayList<>();
    VolatileImage current;
    int data = 0;
    private Perspective fourier;
    private Perspective drink;
    private Perspective meta;
    private Perspective qr;
    byte[] audioBuffer;
    boolean done = false;
    String drinkname = "";
    Font font;
    Font font2;

    public Bartender() {
        super(1000, 250, 2);
        InputStream is = Superimposer.class.getResourceAsStream("/resource/font.ttf");
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        font = font.deriveFont(30f);
        font2 = font.deriveFont(60f);
        for (int i = 0; i < 5; i++) {
            drinks.add(new Image("relation/" + i + ".png").scale(230, 230, RenderingHints.VALUE_INTERPOLATION_BILINEAR).dither().render());
            qrs.add(new Image("qr" + i + ".jpg").scale(350, 350, RenderingHints.VALUE_INTERPOLATION_BILINEAR).render());
        }
        this.drink = new Perspective(464, 367, 6) {
            @Override
            public void draw(Graphics2D graphics) {
                if (current != null) {
                    graphics.drawImage(current, 110, 90, null);
                }

            }
        };
        this.qr = new Perspective(920, 730, 5) {
            @Override
            public void draw(Graphics2D graphics) {
                this.setFocusable(false);
                this.setAlwaysOnTop(true);
                if (done && current != null) {
                    graphics.setColor(Color.BLACK);
                    graphics.fillRect(200, 150, 500, 450);
                    graphics.drawImage(qrs.get(drinks.indexOf(current)), 250, 190, null);
                }
            }
        };
        this.meta = new Perspective(740, 340, 3) {
            @Override
            public void draw(Graphics2D graphics) {

                FontMetrics metrics = graphics.getFontMetrics(font);
                int yy = 0;
                yy -= metrics.getHeight();
                graphics.setColor(Color.RED);
                for (String line : drinkname.split("\n")) {



                    // Determine the X coordinate for the text
                    int x = (740 - metrics.stringWidth(line)) / 2;
                    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
                    int y = ((340 - metrics.getHeight()) / 2) + metrics.getAscent() + yy;
                    graphics.setFont(font);
                    graphics.drawString(line, x, y);
                    yy+=metrics.getHeight();
                }
            }
        };
        this.fourier = new Perspective(660, 240, 1) {
            private final int WIDTH = 480;
            private final int HEIGHT = 120;
            private final int AMPLITUDE = 50; // Amplitude of the sine wave
            private final int PERIOD = 500; // Period of the sine wave
            private final int PHASE_SHIFT = 0; // Phase shift of the sine wave
            private final int POINTS_PER_CYCLE = 1000; // Number of points to draw per cycle
            @Override
            public void draw(Graphics2D graphics) {
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                graphics.setColor(Color.BLACK);
                graphics.fillRect(70, 40, 460, 170);
                graphics.setColor(Color.LIGHT_GRAY);
                graphics.setStroke(new BasicStroke(3));

                int numWaves; // Number of sine waves to combine
                if (current == null) {
                    numWaves = 0;
                } else {
                    numWaves = drinks.indexOf(current);
                }

                audioBuffer = new byte[2 * numWaves];

                int xPrev = 0;
                int yPrev = getHeight() / 2;

                for (int i = 0; i <= 500; i++) { // Adjust the loop range as needed
                    int x = i * WIDTH / 500; // Adjust the width as needed
                    double y = 0;
                    double sample = 0;
                    for (int j = 1; j <= numWaves; j++) {
                        double amplitude = (double) 25 / (double) (data + 1); // Change amplitude for each wave
                        double phaseShift = Math.PI / 2 * j; // Change phase shift for each wave
                        y += amplitude * Math.sin(numWaves * 2 * i * 2 * Math.PI / 500 + phaseShift);
                        sample = amplitude * Math.sin(2 * Math.PI * numWaves * i / 44100 + phaseShift);
                        short sampleValue = (short) (sample * Short.MAX_VALUE);
                        audioBuffer[2 * (j-1)] = (byte) (sampleValue & 0xFF);
                        audioBuffer[2 * (j-1) + 1] = (byte) ((sampleValue >> 8) & 0xFF);
                    }
                    Random rand = new Random();
                    int s = rand.nextInt(4) + 1;
                    if (s == 1) {
                        graphics.setColor(Color.RED);
                    } else if (s == 2) {
                        graphics.setColor(Color.ORANGE);
                    } else if (s == 3) {
                        graphics.setColor(Color.YELLOW);
                    } else {
                        graphics.setColor(Color.WHITE);
                    }

                    graphics.drawLine(xPrev + 50 + rand.nextInt(-5, 5), yPrev + 70 + rand.nextInt(-5, 5),
                            x + 50 + rand.nextInt(-5, 5), (int) (HEIGHT / 2 + y) + 70 + rand.nextInt(-5, 5));

                    xPrev = x;
                    yPrev = (int) (HEIGHT / 2 + y);
                }
            }
        };
    }

    @Override
    public void draw(Graphics2D graphics) {

        FontMetrics metrics = graphics.getFontMetrics(font2);
        graphics.setColor(Color.ORANGE);
        int drink = drinks.indexOf(current);
        String d = "";
        switch(drink) {
            case 0 -> d = "Beer (€ 2,5)";
            case 1 -> d = "Wine (€ 3)";
            case 2 -> d = "Cola (€ 1)";
            case 3 -> d = "Coffee (€ 1)";
            case 4 -> d = "Tea (€ 1)";
        }
        int x = (1000 - metrics.stringWidth(d)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = ((250 - metrics.getHeight()) / 2) + metrics.getAscent();
        graphics.setFont(font2);
        graphics.drawString(d, x, y);

        this.requestFocus();
        // Create an AudioFormat
        AudioFormat audioFormat = new AudioFormat(44100, 16, 1, true, true);

        try {
            // Open the audio line
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();

            // Continuously play the sound
            if (audioBuffer != null && done) {
                sourceDataLine.write(audioBuffer, 0, audioBuffer.length);
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        requestFocus();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_B && current != null) {
            current = null;
            drinkname = "";
            done = false;
            qr.setVisible(false);
        }
        if (key == KeyEvent.VK_G) {
            done = true;
            qr.setVisible(true);
        }
        if (key >= KeyEvent.VK_0 && key <= KeyEvent.VK_9 && !done) {
            data = Integer.parseInt(String.valueOf((char) key));
        }
        if (!done) {
            switch (key) {
                case KeyEvent.VK_A -> current = drinks.get(0);
                case KeyEvent.VK_I -> current = drinks.get(1);
                case KeyEvent.VK_C -> current = drinks.get(2);
                case KeyEvent.VK_D -> current = drinks.get(3);
                case KeyEvent.VK_H -> current = drinks.get(4);
            }
        }
        if (key != KeyEvent.VK_G && key != 16 && !done && current != null) {
            drinkname = FantasyDrink.generateRandomDrink();
        }
    }
    public static class FantasyDrink {
        private static final String[] adjectives = {
                "Mystical", "Enchanted", "Ethereal", "Whimsical", "Magical", "Arcane", "Otherworldly", "Celestial"
        };

        private static final String[] bases = {
                "Elixir", "Brew", "Potion", "Concoction", "Beverage", "Tonic", "Draught", "Spirit", "Libation"
        };

        private static final String[] flavors = {
                "Essence of Stardust", "Dragonfire Extract", "Moonlit Frost", "Fairy Nectar", "Phoenix Tear Infusion",
                "Unicorn Euphoria", "Sylvan Whisper", "Elven Serenade", "Mermaid's Song"
        };

        private static final String[] effects = {
                "Grants temporary flight", "Enhances mystical senses", "Induces vivid dreams",
                "Bestows temporary invisibility", "Restores magical energy", "Increases resilience to spells",
                "Promotes longevity", "Enhances luck", "Opens gates to other realms"
        };


        public static String generateRandomDrink() {
            Random rand = new Random();

            String adjective = adjectives[rand.nextInt(adjectives.length)];
            String base = bases[rand.nextInt(bases.length)];
            String flavor = flavors[rand.nextInt(flavors.length)];
            String effect = effects[rand.nextInt(effects.length)];

            return String.format("%s %s \n %s \n %s", adjective, base, flavor, effect);
        }
    }
}
