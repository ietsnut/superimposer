package superimposer.vision;


import superimposer.library.Image;
import superimposer.notation.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

import com.fazecast.jSerialComm.*;

public class Developer extends Perspective {

    public Unit association;

    private Thread keyboard;
    private Perspective units;
    private Perspective fourier;

    private int data;

    public Developer() {
        super(1200, 1000, 2);
        this.units = new Perspective(240, 660, 0) {
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
                if (scroll < 0 || scroll > units.size() * 160) {
                    scroll = 0;
                }
            }
        };
        this.fourier = new Perspective(660, 240, 1) {
            private final int WIDTH = 480;
            private final int HEIGHT = 120;
            private final int AMPLITUDE = 50; // Amplitude of the sine wave
            private final int PERIOD = 50; // Period of the sine wave
            private final int PHASE_SHIFT = 0; // Phase shift of the sine wave
            private final int POINTS_PER_CYCLE = 1000; // Number of points to draw per cycle
            @Override
            public void draw(Graphics2D graphics) {
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                graphics.setColor(Color.BLACK);
                graphics.fillRect(70, 40, 460, 170);
                graphics.setColor(Color.LIGHT_GRAY);
                graphics.setStroke(new BasicStroke(3));
                int xPrev = 0;
                int yPrev = getHeight() / 2;
                for (int i = 0; i <= (data + 1); i++) {
                    double theta = (double) i / (data + 1) * 2 * Math.PI;
                    int x = i * WIDTH / (data + 1);
                    int y = (int) (HEIGHT / 2 + AMPLITUDE * Math.sin(((double) data /100) * theta + PHASE_SHIFT));
                    Random rand = new Random();
                    int s = rand.nextInt(4) + 1;
                    if (s == 1) {
                        graphics.setColor(Color.LIGHT_GRAY);
                    } else if (s == 2) {
                        graphics.setColor(Color.DARK_GRAY);
                    } else if (s == 3) {
                        graphics.setColor(Color.WHITE);
                    } else {
                        graphics.setColor(Color.GRAY);
                    }
                    graphics.drawLine(xPrev + 50 + rand.nextInt(-5, 5), yPrev + 70 + rand.nextInt(-5, 5), x + 50 + rand.nextInt(-5, 5), y + 70 + rand.nextInt(-5, 5));
                    xPrev = x;
                    yPrev = y;
                }
            }
        };

        this.keyboard = new Thread(new Keyboard());
        this.keyboard.start();
    }

    class Keyboard implements Runnable {
        long time;
        SerialPort[] ports;
        void open() {
            System.out.println("Probing...");
            ports = SerialPort.getCommPorts();
            //ports = new SerialPort[] { SerialPort.getCommPort("COM5") };
            for (SerialPort port : ports) {
                System.out.println("Opening port...");
                System.out.println(port.getSystemPortName());
                this.time = System.nanoTime();
                port.openPort();
                port.flushIOBuffers();
                port.flushDataListener();
                this.time = System.nanoTime();
                port.addDataListener(new SerialPortDataListener() {
                    @Override
                    public void serialEvent(SerialPortEvent e) {
                        if (e.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                            return;
                        }
                        int size = e.getSerialPort().bytesAvailable();
                        System.out.println("Size: " + size);
                        byte[] buffer = new byte[size];
                        int bytes = e.getSerialPort().readBytes(buffer, size);
                        System.out.println("Bytes: " + bytes);
                        String s = new String(buffer, StandardCharsets.UTF_8);
                        System.out.println("Data: " + s);
                        data = Integer.parseInt(s);
                        time = System.nanoTime();
                        e.getSerialPort().flushIOBuffers();
                        e.getSerialPort().flushDataListener();
                    }
                    @Override
                    public int getListeningEvents() {
                        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
                    }
                });
            }
            this.time = System.nanoTime();
        }
        void close() {
            for (SerialPort port : ports) {
                port.closePort();
                port.removeDataListener();
                port.clearBreak();
                port.clearDTR();
                port.clearRTS();
            }
        }
        @Override
        public void run() {
            open();
            while (keyboard.isAlive()) {
                if (((System.nanoTime() - time) / 1000000) > 10000) {
                    this.time = System.nanoTime();
                    close();
                    open();
                }
            }
        }
    }

}
