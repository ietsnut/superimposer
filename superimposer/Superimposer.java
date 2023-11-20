package superimposer;

import superimposer.network.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class Superimposer {

    public Relay relay;
    public ArrayList<Node> nodes;

    public Superimposer(String[] args) {
        this.nodes = new ArrayList<>();
        for (String mode : args) {
            if (mode.equalsIgnoreCase("relay")) {
                this.relay = new Relay();
            }
            if (mode.equalsIgnoreCase("node")) {
                this.nodes.add(new Node());
            }
        }
        //load();
        //save();
    }

    public static void main(String[] args) {
        new Superimposer(args);
    }

    public ImageIcon image(String path) {
        return new ImageIcon(getClass().getResource(path));
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