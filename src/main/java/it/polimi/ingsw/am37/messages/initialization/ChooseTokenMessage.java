package it.polimi.ingsw.am37.messages.initialization;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.exceptions.AlreadyAssignedException;
import it.polimi.ingsw.am37.exceptions.IncorrectUserActionException;
import it.polimi.ingsw.am37.exceptions.NoCardsException;
import it.polimi.ingsw.am37.exceptions.WrongGamePhaseException;
import it.polimi.ingsw.am37.messages.ErrorMessage;
import it.polimi.ingsw.am37.messages.MessageId;
import it.polimi.ingsw.am37.messages.MessageToServer;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.server.ClientHandler;

public class ChooseTokenMessage extends MessageToServer {
    private final String player;
    private final Token token;

    public ChooseTokenMessage(MessageId id, String player, Token token) {
        super(id);
        this.player = player;
        this.token = token;
    }

    @Override
    public void decodeAndExecute(GameController c, ClientHandler ch) {
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

    @Override
    public String toString() {
        return "Received: " + super.toString() + " | player: " + player + " | colour: " + token;
    }
}
