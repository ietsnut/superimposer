package superimposer.network;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server extends Thread {

    public static final int PORT = 8888;
    public static final String ADDRESS = "localhost";

    private final ArrayList<PeerHandler> peers = new ArrayList<>();
    private final ServerSocket socket;

    public Server() {
        try {
            this.socket = new ServerSocket(PORT);
            this.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (isAlive()) {
            try {
                peers.add(new PeerHandler(this.socket.accept()));
                System.out.println("SERVER: GOT A PEER!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sync(PeerHandler handler, PeerData peerData) {
        for (PeerHandler peer : peers) {
            if (!peer.getSocket().equals(handler.getSocket())) {
                peer.out(peerData);
            }
        }
    }

    public class PeerHandler extends Thread {

        private final Socket socket;
        private ObjectOutputStream out;
        private ObjectInputStream in;

        public PeerHandler(Socket socket) {
            this.socket = socket;
            this.start();
        }

        public void out(PeerData peerData) {
            try {
                out.writeObject(peerData);
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                while (isAlive()) {
                    try {
                        PeerData peerData = (PeerData) in.readObject();
                        sync(this, peerData.setId(peers.indexOf(this)));
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public Socket getSocket() {
            return socket;
        }
    }

}
