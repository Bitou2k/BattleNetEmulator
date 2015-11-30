package net.lightsdawn.battlenetemulator.main;

import net.lightsdawn.battlenetemulator.listeners.ConnectionListener;
import net.lightsdawn.battlenetemulator.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static List<Thread> activeThreads = new ArrayList<Thread>();

    public static void main(String... args) {
        if (!Log.initialise())
            return;
        Log.log("Starting Battle.net 2 server...", false);

        boolean running = true;

        try {
            activeThreads.add(startListener());

            while (running) {
                for (Thread t : activeThreads) {
                    if (t.isAlive()) {
                        Thread.sleep(5000);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Log.log("Error: " + e.getMessage(), true);
            e.printStackTrace();
        } catch (Throwable t) {
            Log.log("Error: " + t.getMessage(), true);
            t.printStackTrace();
        } finally {
            Log.log("Server terminated.", false);
            Log.close();
        }
    }

    private static Thread startListener() {
        ConnectionListener listener = new ConnectionListener();
        Thread thread = new Thread(listener);
        thread.setName("Listener");
        thread.start();
        return thread;
    }
}
