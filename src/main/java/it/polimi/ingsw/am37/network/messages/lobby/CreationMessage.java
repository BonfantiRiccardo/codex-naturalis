package it.polimi.ingsw.am37.network.messages.lobby;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.controller.virtualview.TCPVirtualView;
import it.polimi.ingsw.am37.network.messages.mixed.ErrorMessage;
import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToServer;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.network.server.ClientHandler;

public class CreationMessage extends MessageToServer {
    private final String creator;
    private final int num;

    public CreationMessage(MessageId id, String creator, int num) {
        super(id);
        this.creator = creator;
        this.num = num;

    }

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

    @Override
    public String toString() {
        return "Received: " + super.toString() + " | nick: " + creator + " | maxPL: " + num;
    }
}
