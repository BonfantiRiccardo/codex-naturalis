package it.polimi.ingsw.am37.messages;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.controller.TCPVirtualView;
import it.polimi.ingsw.am37.exceptions.*;
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

                controller.addPlayer(p);

                controller.setVirtualView(p, new TCPVirtualView(ch));
                System.out.println("correctly added: " + p.getNickname());

                for (Player pl: controller.getAddedPlayers())
                    controller.getPlayerViews().get(pl).updateLobbyView(controller.getAddedPlayers(), controller.getAddedPlayers().size(), controller.getNumOfPlayers());

                if (controller.isGameStarted()) {
                    ch.getMultipleMatchesHandler().removeLobby(controllerHash);
                    for (Player pl: controller.getAddedPlayers())       //CHANGE TO SEND_INITIAL
                        controller.getPlayerViews().get(pl).sendAvailable(controller.getGameInstance().getAvailableGCards(), controller.getGameInstance().getAvailableRCards());
                }
            } else throw new IncorrectUserActionException("The lobby you chose is not an active lobby.");

        } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                 AlreadyAssignedException | NullPointerException e) {
            ch.send(new ErrorMessage(MessageId.ERROR, e.getMessage()));
        }
    }

    public String getNick() {
        return nick;
    }

    public int getControllerHash() {
        return controllerHash;
    }

    @Override
    public String toString() {
        return "Received: " + super.toString() + " | hash: " + controllerHash + " | nick: " + nick;
    }
}
