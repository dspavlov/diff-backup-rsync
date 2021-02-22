package org.dpavlov.rsync.server;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import static org.dpavlov.rsync.server.DiscoServer.MAX_PACKET_SIZE;

public class DiscoClient {

    private static final Logger logger = Logger.getLogger("root");

    private static final int TIMEOUT_MS = 5000;

    public static void main(String[] args) throws IOException {

        // InetAddress addr = InetAddress.getByName("0.0.0.0");
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);

        socket.setSoTimeout(TIMEOUT_MS);


        InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");

        byte[] packetData = "Disco".getBytes(StandardCharsets.UTF_8);
        int discoveryPort = DiscoServer.DISCOVERY_PORT;
        DatagramPacket packet = new DatagramPacket(packetData,
                packetData.length, broadcastAddress, discoveryPort);

        socket.send(packet);
        logger.info(String.format("Sent packet to %s:%d",
                broadcastAddress.getHostAddress(), discoveryPort));

        byte[] recvBuf = new byte[MAX_PACKET_SIZE];

        DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
        // wait for reply
        socket.receive(receivePacket);
        logger.info("Received reply from "
                + receivePacket.getAddress().getHostAddress());
        String reply = new String(receivePacket.getData());
        logger.info("Reply data: " + reply);
    }
}
