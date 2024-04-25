package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.server.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientTCP {
    private final String ip;
    private final int port;

    public ClientTCP(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void startClient() throws IOException {
        final Socket socket = new Socket(ip, port);
        System.out.println("Connection established");

        final ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
        final ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        final Scanner stdin = new Scanner(System.in);
        try {
            while (true) {
                System.out.println("Player: ");
                final String inputLine = stdin.nextLine();
                System.out.println("Num: ");
                final int inputNum = stdin.nextInt();
                stdin.nextLine();
                final Message message = new Message(inputLine, inputNum);
                socketOut.writeObject(message);
                socketOut.flush();
                final Message reply = (Message) socketIn.readObject();
                System.out.println(reply);
            }
        } catch (final NoSuchElementException | ClassNotFoundException e) {
            System.out.println("Connection closed");
        } finally {
            stdin.close();
            socketIn.close();
            socketOut.close();
            socket.close();
        }
    }
}
