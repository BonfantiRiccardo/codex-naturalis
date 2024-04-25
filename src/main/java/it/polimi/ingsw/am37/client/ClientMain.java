package it.polimi.ingsw.am37.client;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) {
        final ClientTCP client = new ClientTCP("127.0.0.1", 3456);      //NO PORT HARDCODING
        try {
            client.startClient();
        } catch (final IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
