package it.polimi.ingsw.am37.network.messages.turns;

import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToClient;
import it.polimi.ingsw.am37.model.cards.placeable.GoldCard;
import it.polimi.ingsw.am37.model.cards.placeable.ResourceCard;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;

/**
 * This message is sent to the client when a card is drawn from the deck.
 * The client will add the card to his hand and update the top of the deck.
 */
public class DeckCardDrawnMessage extends MessageToClient {
    /**
     * The deck from which the card was drawn.
     * "r" for resource deck, "g" for gold deck.
     */
    private final String deck;
    /**
     * The resource of the card drawn.
     */
    private final Resource resource;
    /**
     * The id of the card drawn.
     */
    private final int cardId;

    /**
     * Constructor.
     * @param id The message id.
     * @param deck The deck from which the card was drawn.
     * @param resource The resource of the card drawn.
     * @param cardId The id of the card drawn.
     */
    public DeckCardDrawnMessage(MessageId id, String deck, Resource resource, int cardId) {
        super(id);
        this.deck = deck;
        this.resource = resource;
        this.cardId = cardId;
    }

    /**
     * This method adds the drawn card to the player's hand and updates the top of the deck.
     * It then advances the turn to the next player.
     * The method is synchronized on the view object, so that the view can be notified when the turn is over.
     * It then notifies the view that the turn is over.
     * @param v The view that will execute the message.
     */
    @Override
    public void decodeAndExecute(View v) {
        if (cardId >= 1 && cardId <= 40) {
            for (ResourceCard rc: v.getLocalGameInstance().getResourceCards())
                if (rc.getId() == cardId)
                    v.getLocalGameInstance().getMyHand().add(rc);

        } else if (cardId >= 41 && cardId <= 80) {
            for (GoldCard gc: v.getLocalGameInstance().getGoldCards())
                if (gc.getId() == cardId)
                    v.getLocalGameInstance().getMyHand().add(gc);
        }

        if (deck.equalsIgnoreCase("r"))
            v.getLocalGameInstance().setTopOfResourceDeck(resource);
        else if (deck.equalsIgnoreCase("g"))
            v.getLocalGameInstance().setTopOfGoldDeck(resource);

        v.getLocalGameInstance().nextTurn();

        synchronized (v) {
            v.setState(ViewState.NOT_TURN);
            v.notify();
        }
    }
}
