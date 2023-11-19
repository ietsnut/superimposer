package superimposer;

import superimposer.network.*;
import superimposer.notation.Unit;
import superimposer.notation.Wave;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Superimposer {

    Server server;
    Peer peer1;
    Peer peer2;
    Peer peer3;

    public Superimposer() {
        this.server = new Server();
        this.peer1 = new Peer();
        this.peer2 = new Peer();
        this.peer3 = new Peer();

        Unit unit = new Unit(null, new Wave(0, 1), new Wave(0, 1), new Wave(0, 1), new Wave(0, 1), new Wave(0, 1), new Wave(0, 1));

        peer1.out(new PeerData(10, 20, unit, unit));
        peer2.out(new PeerData(50, 20, unit, unit));
        peer3.out(new PeerData(150, 20, unit, unit));
        peer3.out(new PeerData(100, 20, unit, unit));

        new Test().start();
    }

    class Test extends Thread {
        @Override
        public void run() {
            while(isAlive()) {
                //System.out.println( peer1.getPeers().size());
            }
        }
    }

    public static void main(String[] args) {
        new Superimposer();
    }


    public static BufferedImage image(String path) {
        try {
            return ImageIO.read(ClassLoader.getSystemResource(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
