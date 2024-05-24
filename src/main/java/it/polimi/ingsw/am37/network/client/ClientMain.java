package it.polimi.ingsw.am37.network.client;

import it.polimi.ingsw.am37.view.TUI.TUIView;
import it.polimi.ingsw.am37.view.ViewState;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) {
        try {
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


            client.startClient();
        } catch (final IOException e) {     //RemoteException is a subclass of IOException
            System.out.println("Disconnection");
            System.err.println(e.getMessage());
        }

        System.out.println("\nClosing application\n");
        System.exit(0);
    }
}
