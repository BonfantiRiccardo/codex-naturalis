package it.polimi.ingsw.am37.network.messages.mixed;

import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.player.Kingdom;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToClient;
import it.polimi.ingsw.am37.view.clientmodel.ClientSidePlayer;
import it.polimi.ingsw.am37.view.View;

/**
 * This message is used to update the kingdom of a player.
 * It can be used to place a card in the kingdom of a player.
 * It can be used to place a start card in the kingdom of a player.
 */
public class UpdateKingdomMessage extends MessageToClient {
    /**
     * The nickname of the player that has to update his kingdom.
     */
    private final String player;
    /**
     * The id of the card that has to be placed in the kingdom.
     */
    private final int cardId;
    /**
     * The side of the card that has to be placed in the kingdom.
     */
    private final String side;
    /**
     * The position of the card that has to be placed in the kingdom.
     */
    private final Position pos;

    /**
     * Constructor of the message.
     * @param id The id of the message.
     * @param player The nickname of the player that has to update his kingdom.
     * @param cardId The id of the card that has to be placed in the kingdom.
     * @param side The side of the card that has to be placed in the kingdom.
     * @param pos The position of the card that has to be placed in the kingdom.
     */
    public UpdateKingdomMessage(MessageId id, String player, int cardId, String side, Position pos) {
        super(id);
        this.player = player;
        this.cardId = cardId;
        this.side = side;
        this.pos = pos;
    }

    /**
     * This method is used to decode the message and execute the action on the view.
     * It can be used to place a card in the kingdom of a player.
     * It can be used to place a start card in the kingdom of a player.
     * @param v The view that has to execute the action.
     */
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
