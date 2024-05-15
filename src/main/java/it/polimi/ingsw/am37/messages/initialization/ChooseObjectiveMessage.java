package it.polimi.ingsw.am37.messages.initialization;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.exceptions.AlreadyAssignedException;
import it.polimi.ingsw.am37.exceptions.IncorrectUserActionException;
import it.polimi.ingsw.am37.exceptions.NoCardsException;
import it.polimi.ingsw.am37.exceptions.WrongGamePhaseException;
import it.polimi.ingsw.am37.messages.ErrorMessage;
import it.polimi.ingsw.am37.messages.MessageId;
import it.polimi.ingsw.am37.messages.MessageToServer;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.server.ClientHandler;

public class ChooseObjectiveMessage extends MessageToServer {
    private final String player;
    private final int cardId;

    public ChooseObjectiveMessage(MessageId id, String player, int cardId) {
        super(id);
        this.player = player;
        this.cardId = cardId;
    }

    @Override
    public void decodeAndExecute(GameController c, ClientHandler ch) {
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

    @Override
    public String toString() {
        return "Received: " + super.toString() + " | player: " + player + " | obj card: " + cardId;
    }
}
