package superimposer.network;

import java.io.*;
import java.net.*;
import java.util.*;

public class Peer extends Thread {

    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final HashMap<Integer, PeerData> peers = new HashMap<>();

    public Peer() {
        try {
            this.socket = new Socket(Server.ADDRESS, Server.PORT);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            this.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (isAlive()) {
            try {
                PeerData data = (PeerData) in.readObject();
                System.out.println("PEER: IN " + data.getId());
                peers.put(data.getId(), data);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void out(PeerData peerData) {
        try {
            out.writeObject(peerData);
            out.flush();
            System.out.println("PEER: OUT " + peerData.getX());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<Integer, PeerData> getPeers() {
        return peers;
    }

    public PeerData getPeer(int id) {
        return peers.get(id);
    }
}

