package superimposer;

import superimposer.network.*;
import superimposer.vision.Bartender;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class Superimposer {

    public Relay relay;
    public ArrayList<Node> nodes;
    public static int SCALE = 1;

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
        SwingUtilities.invokeLater(Bartender::new);
    }
    /*
    public static void main(String[] args) throws IOException {
        //System.setProperty("sun.java2d.opengl", "true");
        //new Superimposer(args);
        VolatileImage image = new Image("f002").threshold().trim().scale(0.5f, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR).render();
        JFrame frame = new JFrame("Test");
        frame.add(new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
            }
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(image.getWidth(), image.getHeight());
        frame.setVisible(true);
    }*/


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