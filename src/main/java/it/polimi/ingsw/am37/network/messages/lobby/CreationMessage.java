package it.polimi.ingsw.am37.network.messages.lobby;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.controller.virtualview.TCPVirtualView;
import it.polimi.ingsw.am37.network.messages.mixed.ErrorMessage;
import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToServer;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.network.server.ClientHandler;
/**
 * This message is sent by the client to the server when the client wants to create a new game.
 * The server will create a new GameController and a new VirtualView for the player.
 * The server will also add the client to the MultipleMatchesHandler and the GameController to the MultipleMatchesHandler.
 * The server will send the lobby view to the player.
 */
public class CreationMessage extends MessageToServer {
    /**
     * The creator of the game.
     */
    private final String creator;
    /**
     * The number of players in the game.
     */
    private final int num;

    /**
     * Constructor.
     * @param id The id of the message.
     * @param creator The creator of the game.
     * @param num The number of players in the game.
     */
    public CreationMessage(MessageId id, String creator, int num) {
        super(id);
        this.creator = creator;
        this.num = num;

    }

    /**
     * This method is called by the client to execute the action that the message
     * carries.
     * It will create a new GameController and a new VirtualView for the player.
     * It will also add the client to the MultipleMatchesHandler and the GameController to the MultipleMatchesHandler.
     * It will send the lobby view to the player.
     * @param c The controller of the game.
     * @param ch The client handler.
     */
    @Override
    public void decodeAndExecute(GameController c, ClientHandler ch) {
        Player p = new Player(creator);
        if(num >= 2 && num <= 4) {
            GameController controller = new GameController(p, num);


            ch.getMultipleMatchesHandler().addClient(ch, controller);
            ch.getMultipleMatchesHandler().addLobby(controller.hashCode(), controller);

            controller.setVirtualView(p, new TCPVirtualView(ch));

            System.out.println("correctly created: " + controller.hashCode());

            controller.getPlayerViews().get(p).updateLobbyView(p, controller.getAddedPlayers(), controller.hashCode(), controller.getNumOfPlayers());
        } else
            ch.send(new ErrorMessage(MessageId.ERROR, "The player number is invalid, game not created."));
    }

    /**
     * This method is used to print the message.
     * @return The message.
     */
    @Override
    public String toString() {
        return "Received: " + super.toString() + " | nick: " + creator + " | maxPL: " + num;
    }
}
