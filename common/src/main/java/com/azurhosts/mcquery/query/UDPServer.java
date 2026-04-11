package com.azurhosts.mcquery.query;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * MCQuery - Classe de gestion des instruction et packets de la query
 * Mainteneur : RedSavant, OxiWan (contact@azurhosts.com)
 * Auteur(s)   : RedSavant (rscomeback@outlook.fr)
 * Distribué sous GNU General Public License v3.0
 * Voir LICENSE, CONTRIBUTING.md pour plus de détails.
 * NOTICE (GPL v3 Section 7b) : L'attribution au mainteneur doit être conservée dans toute redistribution.
 *
 **/

public class UDPServer implements Runnable {

    private final DatagramSocket socket;
    private volatile boolean running = true;

    public UDPServer(int port) throws Exception {
        this.socket = new DatagramSocket(port);
    }

    @Override
    public void run() {
        byte[] buf = new byte[1024];
        while (running) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String instruction = new String(packet.getData(), 0, packet.getLength()).trim();
                String response = handleInstruction(instruction);

                byte[] responseBytes = response.getBytes();
                DatagramPacket reply = new DatagramPacket(
                        responseBytes, responseBytes.length,
                        packet.getAddress(), packet.getPort()
                );
                socket.send(reply);
            } catch (Exception e) {
                if (running) e.printStackTrace();
            }
        }
    }

    private String handleInstruction(String instruction) {
        return "{\"error\":\"unknown_instruction\"}";
    }

    public void stop() {
        running = false;
        socket.close();
    }
}