package it.polimi.ingsw.am37.server;

import it.polimi.ingsw.am37.controller.MultipleMatchesHandler;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        int tcpPort = Integer.parseInt(args[0]);
        int rmiPort = Integer.parseInt(args[1]);
        MultipleMatchesHandler multipleMatchesHandler = new MultipleMatchesHandler();

        //LAUNCH TCP SERVER
        new Thread(() -> {
            ServerTCP serverTCP = new ServerTCP(tcpPort, multipleMatchesHandler);
            serverTCP.startServer();
        }).start();

        //LAUNCH RMI SERVER
        RMIServer rmiServer = new RMIServer(multipleMatchesHandler);
        Registry registry = LocateRegistry.createRegistry(rmiPort);
        registry.bind("RMIServer", rmiServer);
        System.out.println("RMI Server bound and ready on port: " + rmiPort);
    }
}
