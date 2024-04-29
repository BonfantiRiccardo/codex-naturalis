package it.polimi.ingsw.am37.server;

import it.polimi.ingsw.am37.common.MessageDecoder;
import it.polimi.ingsw.am37.common.messages.Message;
import it.polimi.ingsw.am37.common.messages.MessageId;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler {
    private final Socket socket;
    private final MessageDecoder decoder;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    public ClientHandler(Socket socket, MessageDecoder decoder) {
        this.socket = socket;
        this.decoder = decoder;
    }

    public void handle() {
        try {
            try {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

            System.out.println("Waiting for client input...");
            Message message;

            try {
                assert in != null;
                while ( (message = (Message) in.readObject()) != null ) {
                    System.out.println(message);

                    if (message.getId().equals(MessageId.TERMINATE))
                        break;
                    else
                        decoder.decodeAndExecute(this, message);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }

            in.close();
            out.close();
            socket.close();
        } catch (final IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void send(Message m) {
        assert out != null;

        try {
            out.writeObject(m);
            out.flush();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
