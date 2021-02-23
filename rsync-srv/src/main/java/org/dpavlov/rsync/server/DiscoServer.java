package org.dpavlov.rsync.server;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class DiscoServer implements Runnable {

    public static final int MAX_PACKET_SIZE = 2048;
    private static final Logger logger = Logger.getLogger("root");
    public static final int DISCOVERY_PORT = NetConfig.BASE_PORT;
    private DatagramSocket socket;

    public static void main(String[] args) {
        DiscoServer server = new DiscoServer();
        server.run();
    }

    @Override
    public void run() {
        // quit if we get this many consecutive receive errors.
        // reset the counter after successfully receiving a packet.
        final int max_errors = 5;
        int errorCount = 0;

        // this is weak - address could be null or wrong address
       // final String MY_IP = NetworkUtil.getMyAddress().getHostAddress();
       // logger.info("My IP Address " + MY_IP);

        try {
            InetAddress addr = InetAddress.getByName("0.0.0.0");
            socket = new DatagramSocket(DISCOVERY_PORT, addr);
            socket.setBroadcast(true);
        } catch (Exception ex) {
            String msg = "Could not create UDP socket on port " + DISCOVERY_PORT;
            logger.log(Level.SEVERE, msg);
            System.err.println(msg);  // delete this after testing (redundant)
            return;
        }

        System.out.printf("Server listening on port %d\n", DISCOVERY_PORT);

        while (true) {
            // Receive a packet
            byte[] recvBuf = new byte[MAX_PACKET_SIZE];
            DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
            try {
                // wait for a packet
                socket.receive(packet);
            } catch (IOException ioe) {
                logger.log(Level.SEVERE, ioe.getMessage(), ioe);
                // this is to avoid infinite loops when exception is raised.
                errorCount++;
                if (errorCount >= max_errors) return;
                // try again
                continue;
            }

            // Packet received
            errorCount = 0;    // reset error counter
            InetAddress clientAddress = packet.getAddress();
            int clientPort = packet.getPort();

            logger.info(String.format("Packet received from %s:%d",
                    clientAddress.getHostAddress(), clientPort));

            logger.info("Received data: " + new String(packet.getData()));

            // See if the packet holds the correct signature string
            String message = new String(packet.getData()).trim();
            if (message.startsWith("Disco")) {
                String reply = "DISCOVERY_REPLY";
                byte[] sendData = reply.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData,
                        sendData.length, clientAddress, clientPort);
                try {
                    socket.send(sendPacket);
                    logger.info(String.format("Reply sent to %s:%d",
                            clientAddress.getHostAddress(), clientPort));
                } catch (IOException ioe) {
                    logger.log(Level.SEVERE, "IOException sending service reply", ioe);
                }
            } else {
                logger.info(String.format("Packet from %s:%d not a discovery packet",
                        clientAddress.getHostAddress(), clientPort));
            }
        }
    }
}
