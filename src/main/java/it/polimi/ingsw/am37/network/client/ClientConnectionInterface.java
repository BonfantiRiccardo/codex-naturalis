package it.polimi.ingsw.am37.network.client;

import java.io.IOException;

/**
 * the ClientConnectionInterface is an Interface implemented in the ClientRMI class and in the ClientTCP class
 * to have the startClient method in both of them.
 */
public interface ClientConnectionInterface {
     /**
     * the startClient method is called by the ClientMain to connect the Client and the view.
     * @throws IOException when the connection does not work.
     */
    void startClient () throws IOException ;
}
