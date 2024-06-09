package it.polimi.ingsw.am37.network.messages.lobby;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.controller.virtualview.TCPVirtualView;
import it.polimi.ingsw.am37.exceptions.*;
import it.polimi.ingsw.am37.network.messages.mixed.ErrorMessage;
import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToServer;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.network.server.ClientHandler;

/**
 * This message is sent by the client to the server when the player wants to join a lobby.
 * The server will add the player to the lobby and send the updated lobby view to all the players in the lobby.
 * If the lobby is full, the server will send the initial game state to all the players in the lobby.
 */
public class JoinMessage extends MessageToServer {
    /**
     * The hash of the lobby the player wants to join.
     */
    private final int controllerHash;
    /**
     * The nickname of the player.
     */
    private final String nick;

    /**
     * Constructor.
     * @param id The message id.
     * @param controllerHash The hash of the lobby the player wants to join.
     * @param nick The nickname of the player.
     */
    public JoinMessage(MessageId id, int controllerHash, String nick) {
        super(id);
        this.controllerHash = controllerHash;
        this.nick = nick;
    }

    /**
     * This method will add the player to the lobby and send the updated lobby view to all the players in the lobby.
     * If the lobby is full, the server will send the initial game state to all the players in the lobby.
     * @param c The game controller.
     * @param ch The client handler.
     */
    @Override
    public void decodeAndExecute(GameController c, ClientHandler ch) {
        try {

            GameController controller = ch.getMultipleMatchesHandler().getLobbyList().get(controllerHash);
            if (controller != null) {
                Player p = new Player(nick);

                synchronized (controller) {
                    controller.addPlayer(p);

                    controller.setVirtualView(p, new TCPVirtualView(ch));
                }

                ch.getMultipleMatchesHandler().addClient(ch, controller);

                System.out.println("correctly added: " + p.getNickname());

                controller.getPlayerViews().get(p).updateLobbyView(p, controller.getAddedPlayers(), controllerHash, controller.getNumOfPlayers());

                for (Player pl: controller.getAddedPlayers())
                    if (!pl.equals(p))
                        controller.getPlayerViews().get(pl).playerAdded(p);

                if (controller.isGameStarted()) {
                    ch.getMultipleMatchesHandler().removeLobby(controllerHash);

                    for (Player pl: controller.getGameInstance().getParticipants())
                        controller.getPlayerViews().get(pl).sendInitial(controller.getGameInstance().getAvailableGCards(),
                                controller.getGameInstance().getAvailableRCards(), pl.getStartCard(), pl.getHand(),
                                controller.getGameInstance().getPublicObjectives(), pl.getObjectivesToChooseFrom(),
                                controller.getGameInstance().getGDeck().firstBack().getMainResource(),
                                controller.getGameInstance().getRDeck().firstBack().getMainResource());
                }
            } else throw new IncorrectUserActionException("The lobby you chose is not an active lobby.");

        } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                 AlreadyAssignedException | NullPointerException e) {
            ch.send(new ErrorMessage(MessageId.ERROR, e.getMessage()));
        }
    }

    /**
     * @return The number of the lobby the player wants to join.
     */
    @Override
    public String toString() {
        return "Received: " + super.toString() + " | hash: " + controllerHash + " | nick: " + nick;
    }
}
