package it.polimi.ingsw.am37.server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        int tcpPort = Integer.parseInt(args[0]);
        int rmiPort = Integer.parseInt(args[1]);

        new Thread(() -> {
            ServerTCP serverTCP = new ServerTCP(tcpPort);     //NO PORT HARDCODING
            serverTCP.startServer();
        }).start();

        //LAUNCH RMI SERVER
        RMIServer rmiServer = new RMIServer();
        Registry registry = LocateRegistry.createRegistry(rmiPort);
        registry.bind("RMIServer", rmiServer);
        System.out.println("RMI Server bound and ready on port: " + rmiPort);
    }
}
