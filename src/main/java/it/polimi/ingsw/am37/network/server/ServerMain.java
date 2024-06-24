package it.polimi.ingsw.am37.network.server;

import it.polimi.ingsw.am37.controller.MultipleMatchesHandler;

import java.net.*;
import java.net.SocketException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Enumeration;

/**
 * This class is used to launch the server.
 */
public class ServerMain {
    /**
     * This method is used to launch the server.
     * @param args The arguments that are passed to the server.
     * @throws RemoteException If there is an error in the communication.
     * @throws AlreadyBoundException If the server is already bound.
     */
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        int tcpPort = Integer.parseInt(args[0]);
        int rmiPort = Integer.parseInt(args[1]);
        MultipleMatchesHandler multipleMatchesHandler = new MultipleMatchesHandler();

        //LAUNCH TCP SERVER
        new Thread(() -> {
            ServerTCP serverTCP = new ServerTCP(tcpPort, multipleMatchesHandler);
            serverTCP.startServer();
        }).start();

        // Set the hostname to localhost to avoid issues with the RMI server

        Enumeration e;
        try {
            e = NetworkInterface.getNetworkInterfaces();
            while(e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();
                int count = 0;
                boolean found = false;
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    if (count == 0 && i.getHostAddress().contains("wireless"))
                        found = true;
                    else if (count == 1 && found && i.getHostAddress().contains("172")) {
                        System.setProperty("java.rmi.server.hostname", i.getHostAddress());
                        System.out.println("RMI Server hostname set to: " + i.getHostAddress());
                        break;
                    }
                    count++;
                }
            }
        } catch (SocketException ex) {
            throw new RuntimeException(ex);
        }

        //LAUNCH RMI SERVER
        RMIServer rmiServer = new RMIServer(multipleMatchesHandler);
        Registry registry = LocateRegistry.createRegistry(rmiPort);

        registry.bind("RMIServer", rmiServer);
        System.out.println("RMI Server bound and ready on port: " + rmiPort);
    }
}
