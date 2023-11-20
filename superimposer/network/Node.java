package superimposer.network;

import superimposer.notation.Association;

import java.io.*;
import java.net.*;
import java.util.*;

public class Node {

    private final Socket socket;
    private final In in;
    private final Out out;

    public final HashMap<Integer, State> states;
    public State state;

    public Node() {
        try (Socket socket = new Socket(Relay.ADDRESS, Relay.PORT)) {
            this.socket = socket;
            this.states = new HashMap<>();
            this.in = new In(new ObjectInputStream(socket.getInputStream()));
            this.out = new Out(new ObjectOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    class In extends Thread {
        private final ObjectInputStream in;
        protected In(ObjectInputStream in) {
            this.in = in;
            this.start();
        }
        @Override
        public void run() {
            while (!socket.isClosed() && isAlive()) {
                try {
                    Object object = in.readObject();
                    if (object instanceof superimposer.network.State state) {
                        states.put(state.id, state);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    class Out extends Thread {
        private final ObjectOutputStream out;
        protected Out(ObjectOutputStream out) {
            this.out = out;
            this.start();
        }
        @Override
        public void run() {
            while (!socket.isClosed() && isAlive()) {
                try {
                    out.writeObject(state);
                    out.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
