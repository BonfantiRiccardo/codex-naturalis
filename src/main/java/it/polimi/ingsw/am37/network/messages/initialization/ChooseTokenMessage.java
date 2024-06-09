package it.polimi.ingsw.am37.network.messages.initialization;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.exceptions.AlreadyAssignedException;
import it.polimi.ingsw.am37.exceptions.IncorrectUserActionException;
import it.polimi.ingsw.am37.exceptions.NoCardsException;
import it.polimi.ingsw.am37.exceptions.WrongGamePhaseException;
import it.polimi.ingsw.am37.network.messages.mixed.ErrorMessage;
import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToServer;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.network.server.ClientHandler;
/**
 * This message is sent by the client to the server to choose the token
 * that the player wants to use during the game.
 */
public class ChooseTokenMessage extends MessageToServer {
    /**
     * The nickname of the player that is choosing the token.
     */
    private final String player;
    /**
     * The token that the player wants to use.
     */
    private final Token token;

    /**
     * Constructor of the class.
     * @param id The id of the message.
     * @param player The nickname of the player that is choosing the token.
     * @param token The token that the player wants to use.
     */
    public ChooseTokenMessage(MessageId id, String player, Token token) {
        super(id);
        this.player = player;
        this.token = token;
    }
    /**
     * This method is called by the server to execute the action that the message
     * carries.
     * The server will call the method playerChoosesToken of the controller to
     * assign the token to the player.
     * If the player is not logged, the server will send an error message to the client.
     * @param c The controller of the game.
     * @param ch The client handler that sent the message.
     */
    @Override
    public void decodeAndExecute(GameController c, ClientHandler ch) {
        if (c == null) {
            ch.send(new ErrorMessage(MessageId.ERROR, "You are not logged"));
            return;
        }

        for (Player p: c.getGameInstance().getParticipants()) {
            if (p.getNickname().equalsIgnoreCase(player)) {
                try {
                    c.playerChoosesToken(p, token);
                } catch (AlreadyAssignedException | IncorrectUserActionException | WrongGamePhaseException |
                         NoCardsException e) {
                    ch.send(new ErrorMessage(MessageId.ERROR, e.getMessage()));
                    return;
                }

                c.getPlayerViews().get(p).acknowledgePlayer(p, "token ok");

                for (Player pl: c.getGameInstance().getParticipants())
                    if (!pl.getNickname().equals(player))
                        c.getPlayerViews().get(pl).nowUnavailableToken(p, token);

                break;
            }
        }

    }

    /**
     * This method is used to print the message.
     * @return The string that represents the message.
     */
    @Override
    public String toString() {
        return "Received: " + super.toString() + " | player: " + player + " | colour: " + token;
    }
}
