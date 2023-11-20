package superimposer.network;

import java.io.*;
import java.net.*;
import java.util.*;

public class Relay extends Thread {

    public static final int PORT = 8888;
    public static final String ADDRESS = "localhost";

    private final ArrayList<Node> nodes;
    private final ServerSocket socket;

    public Relay() {
        try (ServerSocket socket = new ServerSocket(PORT)) {
            this.socket = socket;
            this.nodes = new ArrayList<>();
            this.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (isAlive()) {
            try (Socket socket = this.socket.accept()) {
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Node node = new Node(socket, in, out);
                nodes.add(node);
                node.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    class Node extends Thread {

        protected final Socket socket;
        private final ObjectOutputStream out;
        private final ObjectInputStream in;

        public Node(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
            this.socket = socket;
            this.out = out;
            this.in = in;
        }

        @Override
        public void run() {
            while (!socket.isClosed() && isAlive()) {
                try {
                    Object object = in.readObject();
                    if (object instanceof superimposer.network.State state) {
                        state.id = nodes.indexOf(this);
                        for (Node node : nodes) {
                            if (!node.socket.equals(this.socket)) {
                                out.writeObject(state);
                                out.flush();
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}