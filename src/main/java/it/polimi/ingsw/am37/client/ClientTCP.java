package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.common.messages.*;
import it.polimi.ingsw.am37.common.messages.MessageId;

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
        System.out.println("Connection established on port: " + port);

        final ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
        socketOut.flush();
        final Scanner stdin = new Scanner(System.in);
        final ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());

        try {
            while (true) {
                System.out.println("create or join? (terminate)");
                final String inputLine = stdin.nextLine();
                MessageId id;
                if (inputLine.equals("terminate"))
                    break;
                else if (inputLine.equals("create"))
                    id = MessageId.CREATE;
                else
                    id = MessageId.JOIN;

                System.out.println("Nickname: ");
                final String inputLine2 = stdin.nextLine();

                Message message;

                if(id.equals(MessageId.CREATE)) {
                    System.out.println("Num: ");
                    final int inputNum = stdin.nextInt();
                    stdin.nextLine();
                    message = new CreationMessage(id, inputLine2, inputNum);
                } else
                    message = new JoinMessage(id, inputLine2);


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
