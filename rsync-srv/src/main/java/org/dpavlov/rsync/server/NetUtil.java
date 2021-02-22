package org.dpavlov.rsync.server;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;

public class NetUtil {
    public static ServerSocket bindFreePort(int startPort) {
        return bindFreePort(startPort, 10);
    }

    public static ServerSocket bindFreePort(int startPort, int rangeScan) {
        for (int port= startPort ; port < startPort + rangeScan; port++) {

            try {
                return new ServerSocket(port);
            } catch (IOException ex) {
                continue; // try next port
            }
        }

        // if the program gets here, no port in the range was found
        throw new UncheckedIOException(new IOException("no free port found"));
    }
}
