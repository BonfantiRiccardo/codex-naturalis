package it.polimi.ingsw.am37.messages.initialization;

import it.polimi.ingsw.am37.messages.MessageId;
import it.polimi.ingsw.am37.messages.MessageToClient;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.player.Kingdom;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.view.ClientSidePlayer;
import it.polimi.ingsw.am37.view.View;

public class UpdateKingdomMessage extends MessageToClient {
    private final String player;
    private final int cardId;
    private final String side;
    private final Position pos;

    public UpdateKingdomMessage(MessageId id, String player, int cardId, String side, Position pos) {
        super(id);
        this.player = player;
        this.cardId = cardId;
        this.side = side;
        this.pos = pos;
    }

    @Override
    public void decodeAndExecute(View v) {
        //does not synchronize on the view

        //modifies the kingdoms
        if (cardId >= 81 && cardId <= 86) {
            StartCard placed = null;
            for (StartCard sc : v.getLocalGameInstance().getStartCards())
                if (cardId == sc.getId())
                    placed = sc;

            assert placed != null;

            if (side.equalsIgnoreCase("f")) {
                for (ClientSidePlayer p : v.getLocalGameInstance().getPlayers()) {
                    p.setKingdom(new Kingdom(placed, placed.getFront()));
                }
            } else if (side.equalsIgnoreCase("b")) {
                for (ClientSidePlayer p : v.getLocalGameInstance().getPlayers()) {
                    p.setKingdom(new Kingdom(placed, placed.getBack()));
                }
            }

            v.notify();

        }


        // notifies
    }
}
