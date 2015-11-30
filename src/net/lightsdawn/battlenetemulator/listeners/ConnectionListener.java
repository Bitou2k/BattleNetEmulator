package net.lightsdawn.battlenetemulator.listeners;

import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionListener implements Runnable {

    private int sessionCounter = 0;
    private volatile boolean running = true;
    private int socketTimeoutTimeMs = 15 * 60 * 1000;

    public ConnectionListener() {
    }

    @Override
    public void run() {
        try {
            final ServerSocket serverSocket = new ServerSocket(1119);
            Socket socket = null;
            while (running && (socket = serverSocket.accept()) != null) {
                socket.setSoTimeout(socketTimeoutTimeMs);
                Thread t = new Thread(new Session(socket.getInputStream()));
                t.setName("Session-" + ++sessionCounter);
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        running = false;
    }
}
