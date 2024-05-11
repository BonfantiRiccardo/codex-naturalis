package it.polimi.ingsw.am37.messages.turns;

import it.polimi.ingsw.am37.messages.MessageId;
import it.polimi.ingsw.am37.messages.MessageToClient;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.view.View;

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

    }
}
