package it.polimi.ingsw.am37.network.messages.turns;

import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToClient;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.view.View;

public class UpdateDeckView extends MessageToClient {
    private final String deck;
    private final Resource resource;

    public UpdateDeckView(MessageId id, String deck, Resource resource) {
        super(id);
        this.deck = deck;
        this.resource = resource;
    }

    @Override
    public void decodeAndExecute(View v) {
        if (deck.equalsIgnoreCase("r"))
            v.getLocalGameInstance().setTopOfResourceDeck(resource);
        else if (deck.equalsIgnoreCase("g"))
            v.getLocalGameInstance().setTopOfGoldDeck(resource);

        v.getLocalGameInstance().nextTurn();
    }
}
