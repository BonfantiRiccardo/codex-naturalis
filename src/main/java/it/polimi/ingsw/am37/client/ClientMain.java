package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.view.TUIView;
import it.polimi.ingsw.am37.view.ViewState;

import java.io.IOException;
import java.rmi.RemoteException;

public class ClientMain {
    public static void main(String[] args) throws RemoteException {

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        String view = args[2];
        String protocol = args[3];

        ClientConnectionInterface client;

        if (protocol.equalsIgnoreCase("tcp")) {
            if (view.equalsIgnoreCase("tui"))
                client = new ClientTCP(hostName, portNumber, new TUIView(ViewState.CREATE_JOIN));
            else
                client = new ClientTCP(hostName, portNumber, new TUIView(ViewState.CREATE_JOIN)); //GUI
        } else if (protocol.equalsIgnoreCase("rmi")) {
            if (view.equalsIgnoreCase("tui"))
                client = new ClientRMI(hostName, portNumber, new TUIView(ViewState.CREATE_JOIN));
            else
                client = new ClientRMI(hostName, portNumber, new TUIView(ViewState.CREATE_JOIN)); //GUI
        } else {
            System.out.println("Wrong protocol system, starting default configuration");
            client = new ClientTCP(hostName, portNumber, new TUIView(ViewState.CREATE_JOIN)); //GUI
        }

        try {
            client.startClient();
        } catch (final IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
