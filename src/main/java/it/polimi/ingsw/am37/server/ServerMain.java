package it.polimi.ingsw.am37.server;

public class ServerMain {
    public static void main(String[] args) {
        ServerTCP server = new ServerTCP(3456);     //NO PORT HARDCODING
        server.startServer();

        //LAUNCH RMI SERVER
    }
}
