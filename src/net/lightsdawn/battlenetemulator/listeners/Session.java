package net.lightsdawn.battlenetemulator.listeners;

import net.lightsdawn.battlenetemulator.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Session implements Runnable {

    private InputStream stream = null;

    public Session(InputStream inputStream) {
        this.stream = inputStream;
    }

    @Override
    public void run() {
        try {
            while (stream != null) {

                while (stream.available() > 0) {
                    byte[] buffer = new byte[255];
                    while (stream.read(buffer) > -1) {
                        int packet = getPacketOpcode(buffer);
                        Log.debug("Got packet: %d", false, packet);
                        Log.debug("Packet contents: %s", false, bytesToHex(buffer));
                    }
                }
            }
        } catch (Exception e) {
            Log.log(e.getMessage(), true);
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }
        Log.debug("Shutting down connection: %s", false, Thread.currentThread().getName());
    }

    private int getPacketOpcode(byte[] buffer) {
        ByteBuffer bb = ByteBuffer.wrap(buffer);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.get();
    }

    final protected static char[] hexArray = new String("0123456789ABCDEF").toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; ++j) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
