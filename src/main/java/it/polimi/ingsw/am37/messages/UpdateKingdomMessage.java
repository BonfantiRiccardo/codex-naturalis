package it.polimi.ingsw.am37.messages;

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

        if (cardId >= 81 && cardId <= 86) {
            StartCard placed = null;
            for (StartCard sc : v.getLocalGameInstance().getStartCards())
                if (cardId == sc.getId()) {
                    placed = sc;
                    break;
                }

            assert placed != null;

            for (ClientSidePlayer p: v.getLocalGameInstance().getPlayers()) {
                if (p.getNickname().equals(player)) {
                    if (side.equalsIgnoreCase("f"))
                        p.setKingdom(new Kingdom(placed, placed.getFront()));
                    else if (side.equalsIgnoreCase("b"))
                        p.setKingdom(new Kingdom(placed, placed.getBack()));

                    break;
                }
            }

            if(v.getLocalGameInstance().getMe().getNickname().equals(player)) {
                if (side.equalsIgnoreCase("f"))
                    v.getLocalGameInstance().getMe().setKingdom(new Kingdom(placed, placed.getFront()));
                else if (side.equalsIgnoreCase("b"))
                    v.getLocalGameInstance().getMe().setKingdom(new Kingdom(placed, placed.getBack()));

            }

        } else if (cardId >= 1 && cardId <= 80)
            v.getLocalGameInstance().placeCard(player, cardId, side, pos);
    }
}
