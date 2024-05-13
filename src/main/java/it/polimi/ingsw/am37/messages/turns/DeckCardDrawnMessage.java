package it.polimi.ingsw.am37.messages.turns;

import it.polimi.ingsw.am37.messages.MessageId;
import it.polimi.ingsw.am37.messages.MessageToClient;
import it.polimi.ingsw.am37.model.cards.placeable.GoldCard;
import it.polimi.ingsw.am37.model.cards.placeable.ResourceCard;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;

public class DeckCardDrawnMessage extends MessageToClient {
    private final String deck;
    private final Resource resource;
    private final int cardId;

    public DeckCardDrawnMessage(MessageId id, String deck, Resource resource, int cardId) {
        super(id);
        this.deck = deck;
        this.resource = resource;
        this.cardId = cardId;
    }

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

        synchronized (v) {
        v.setState(ViewState.NOT_TURN);
            v.notify();
        }
    }
}
