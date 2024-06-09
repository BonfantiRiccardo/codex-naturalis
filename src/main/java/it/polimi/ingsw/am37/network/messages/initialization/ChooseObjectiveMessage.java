package it.polimi.ingsw.am37.network.messages.initialization;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.exceptions.AlreadyAssignedException;
import it.polimi.ingsw.am37.exceptions.IncorrectUserActionException;
import it.polimi.ingsw.am37.exceptions.NoCardsException;
import it.polimi.ingsw.am37.exceptions.WrongGamePhaseException;
import it.polimi.ingsw.am37.network.messages.mixed.ErrorMessage;
import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToServer;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.network.server.ClientHandler;

/**
 * Message sent by the client to the server to choose an objective card.
 */
public class ChooseObjectiveMessage extends MessageToServer {
    /**
     * The nickname of the player who is choosing the objective card.
     */
    private final String player;
    /**
     * The id of the objective card that the player is choosing.
     */
    private final int cardId;
    /**
     * Constructor of the class.
     * @param id The id of the message.
     * @param player The nickname of the player who is choosing the objective card.
     * @param cardId The id of the objective card that the player is choosing.
     */
    public ChooseObjectiveMessage(MessageId id, String player, int cardId) {
        super(id);
        this.player = player;
        this.cardId = cardId;
    }

    /**
     * Method called by the server to decode and execute the message of choosing an objective card.
     * The server checks if the player is logged and if the message is corrupted.
     * If the player is logged and the message is not corrupted, the server checks if the player can choose the objective card.
     * If the player can choose the objective card, the server sends a message to the player to acknowledge the choice.
     * If the player can't choose the objective card, the server sends a message to the player with the error.
     * @param c The controller of the game.
     * @param ch The client handler that sent the message.
     */
    @Override
    public void decodeAndExecute(GameController c, ClientHandler ch) {
        if (c == null) {
            ch.send(new ErrorMessage(MessageId.ERROR, "You are not logged"));
            return;
        }

        boolean chosen = false;
        for (Player p: c.getGameInstance().getParticipants()) {
            if (p.getNickname().equalsIgnoreCase(player)) {
                for (ObjectiveCard oc: p.getObjectivesToChooseFrom()) {
                    if (oc.getId() == cardId) {
                        try {
                            c.playerChoosesObjective(p, oc);
                            chosen = true;
                        } catch (AlreadyAssignedException | IncorrectUserActionException | WrongGamePhaseException |
                                 NoCardsException e) {
                            ch.send(new ErrorMessage(MessageId.ERROR, e.getMessage()));
                            return;
                        }

                        c.getPlayerViews().get(p).acknowledgePlayer(p, "objective ok");



                        if (c.getGameInstance().getCurrentTurn() != null) {
                            for (Player pl: c.getGameInstance().getParticipants())
                                c.getPlayerViews().get(pl).sendPlayersInOrder(c.getGameInstance().getParticipants());

                            c.getPlayerViews().get(c.getGameInstance().getCurrentTurn()).notifyTurn(c.getGameInstance().getCurrentTurn());
                        }

                    }
                }

                if(!chosen)
                    ch.send(new ErrorMessage(MessageId.ERROR, "Couldn't choose the card because message is corrupted."));

                break;
            }
        }
    }
    /**
     * Method that returns the string representation of the message.
     * @return The string representation of the message.
     */
    @Override
    public String toString() {
        return "Received: " + super.toString() + " | player: " + player + " | obj card: " + cardId;
    }
}
