package superimposer;

import superimposer.network.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

public class Superimposer {

    public Relay relay;
    public ArrayList<Node> nodes;

    public Superimposer(String[] args) {
        /*
        this.nodes = new ArrayList<>();
        for (String mode : args) {
            if (mode.equalsIgnoreCase("relay")) {
                this.relay = new Relay();
            }
            if (mode.equalsIgnoreCase("node")) {
                this.nodes.add(new Node());
            }
        }
         */
        //load();
        //save();
    }

    public static void main(String[] args) {
        new Superimposer(args);
    }

    public static ImageIcon icon(String name) {
        return new ImageIcon(image(name));
    }

    public static BufferedImage image(String name) {
        Image image = new ImageIcon("resource/" + name + ".jpg").getImage();
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        int top = 0, bottom = bufferedImage.getHeight() - 1, left = 0, right = bufferedImage.getWidth() - 1;
        searchTop:
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                if (bufferedImage.getRGB(x, y) != Color.WHITE.getRGB()) {
                    top = y;
                    break searchTop;
                }
            }
        }
        searchBottom:
        for (int y = bufferedImage.getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                if (bufferedImage.getRGB(x, y) != Color.WHITE.getRGB()) {
                    bottom = y;
                    break searchBottom;
                }
            }
        }
        searchLeft:
        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                if (bufferedImage.getRGB(x, y) != Color.WHITE.getRGB()) {
                    left = x;
                    break searchLeft;
                }
            }
        }
        searchRight:
        for (int x = bufferedImage.getWidth() - 1; x >= 0; x--) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                if (bufferedImage.getRGB(x, y) != Color.WHITE.getRGB()) {
                    right = x;
                    break searchRight;
                }
            }
        }
        int newWidth = right - left + 1;
        int newHeight = bottom - top + 1;
        BufferedImage trimmedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_BINARY);
        trimmedImage.getGraphics().drawImage(bufferedImage.getSubimage(left, top, newWidth, newHeight), 0, 0, null);
        return trimmedImage;
    }

    private void load() {
        for (Node node : nodes) {
            try {
                FileInputStream fi = new FileInputStream("node_" + nodes.indexOf(node));
                ObjectInputStream oi = new ObjectInputStream(fi);
                node.state = (State) oi.readObject();
                oi.close();
                fi.close();
            } catch (FileNotFoundException e) {
                System.out.println("Node file not found");
            } catch (IOException e) {
                System.out.println("Error initializing stream");
            } catch (ClassNotFoundException e) {
                throw new ClassCastException();
            }
        }
    }

    private void save() {
        try {
            for (Node node : nodes) {
                FileOutputStream f = new FileOutputStream("node_" + nodes.indexOf(node));
                ObjectOutputStream o = new ObjectOutputStream(f);
                o.writeObject(node.state);
                o.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}