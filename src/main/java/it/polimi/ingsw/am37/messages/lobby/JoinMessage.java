package it.polimi.ingsw.am37.messages.lobby;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.controller.TCPVirtualView;
import it.polimi.ingsw.am37.exceptions.*;
import it.polimi.ingsw.am37.messages.ErrorMessage;
import it.polimi.ingsw.am37.messages.MessageId;
import it.polimi.ingsw.am37.messages.MessageToServer;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.server.ClientHandler;


public class JoinMessage extends MessageToServer {
    private final int controllerHash;
    private final String nick;

    public JoinMessage(MessageId id, int controllerHash, String nick) {
        super(id);
        this.controllerHash = controllerHash;
        this.nick = nick;
    }

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

                System.out.println("correctly added: " + p.getNickname());

                controller.getPlayerViews().get(p).updateLobbyView(p, controller.getAddedPlayers(), controllerHash, controller.getNumOfPlayers());

                for (Player pl: controller.getAddedPlayers())
                    if (!pl.equals(p))
                        controller.getPlayerViews().get(pl).playerAdded(p);

                if (controller.isGameStarted()) {
                    synchronized (ch.getMultipleMatchesHandler()) {
                        ch.getMultipleMatchesHandler().removeLobby(controllerHash);
                    }

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

    @Override
    public String toString() {
        return "Received: " + super.toString() + " | hash: " + controllerHash + " | nick: " + nick;
    }
}
