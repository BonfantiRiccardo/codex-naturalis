package it.polimi.ingsw.am37.network.client;

/**
 * the DefaultClientParams class keeps track of the default settings to start the connection.
 */
public class DefaultClientParams {
    /**
     * the hostname attribute is the server's ip.
     */
    public static String hostName = "127.0.0.1";
    /**
     * the portNumber attribute is the server's port.
     */
    public static int portNumber = 5467;
    /**
     * the view attribute is the kind of view used.
     */
    public static String view = "tui";
    /**
     * the protocol attribute is the kind of protocol used for the connection.
     */
    public static String protocol = "tcp";
}
