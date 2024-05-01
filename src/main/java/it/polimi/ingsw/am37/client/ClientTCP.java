package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.messages.*;

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
                String inputLine;
                Message message;
                Message response = null;

                while (true) {
                    System.out.println("create or join? (terminate)");
                    inputLine = stdin.nextLine();

                    if (inputLine.equalsIgnoreCase("join"))
                        message = joinRequest();
                    else if (inputLine.equalsIgnoreCase("create")) {
                        message = creationMessage(stdin);
                    } else {
                        break;
                    }

                    socketOut.writeObject(message);
                    socketOut.flush();

                    final Message reply = (Message) socketIn.readObject();
                    System.out.println(reply);
                    response = reply;

                    if (!reply.getId().equals(MessageId.ERROR)) break;
                }

                if (inputLine.equalsIgnoreCase("join")) {
                    while (true) {
                        message = joinMessage(stdin);

                        socketOut.writeObject(message);
                        socketOut.flush();
                        final Message reply2 = (Message) socketIn.readObject();
                        System.out.println(reply2);
                        response = reply2;

                        if (!reply2.getId().equals(MessageId.ERROR)) break;
                    }
                }

                if (response != null &&
                        ((UpdateLobbyMessage) response).getJoinedPlayer() < ((UpdateLobbyMessage) response).getTotalPlayers()) {
                    System.out.println("Now waiting for all players.");
                    while (true) {
                        final Message reply3 = (Message) socketIn.readObject();
                        System.out.println(reply3);
                        if (((UpdateLobbyMessage) reply3).getJoinedPlayer() == ((UpdateLobbyMessage) reply3).getTotalPlayers())
                            break;
                    }
                }
                break;
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

    private Message creationMessage (Scanner stdin) {
        System.out.println("Nickname: ");
        final String inputLine2 = stdin.nextLine();

        System.out.println("Num: ");
        final int inputNum = stdin.nextInt();
        stdin.nextLine();

        return new CreationMessage(MessageId.CREATE, inputLine2, inputNum);
    }

    private Message joinRequest() {
        return new LobbyRequest(MessageId.REQUEST_LOBBY);
    }

    private Message joinMessage(Scanner stdin) {
        System.out.println("Choose hash: ");
        final int inputNum2 = stdin.nextInt();
        stdin.nextLine();
        System.out.println("Nickname: ");
        final String inputLine2 = stdin.nextLine();

        return new JoinMessage(MessageId.JOIN, inputNum2, inputLine2);
    }
}
