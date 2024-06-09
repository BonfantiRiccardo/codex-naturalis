package it.polimi.ingsw.am37.network.messages.turns;

import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToClient;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.view.View;

/**
 * This message is sent to the client to update the deck in the view.
 * It contains the deck that changed and the top card of the deck.
 */
public class UpdateDeckView extends MessageToClient {
    /**
     * The deck that changed.
     */
    private final String deck;
    /**
     * The top card of the deck.
     */
    private final Resource resource;

    /**
     * Constructor.
     * @param id The message id.
     * @param deck The deck that changed.
     * @param resource The top card of the deck.
     */
    public UpdateDeckView(MessageId id, String deck, Resource resource) {
        super(id);
        this.deck = deck;
        this.resource = resource;
    }

    /**
     * This method updates the deck in the view.
     * It sets the top card of the deck in the local game instance.
     * It sets the state of the view to NOT_TURN and notifies the view.
     * It also calls the nextTurn method of the local game instance.
     * @param v The view.
     */
    @Override
    public void decodeAndExecute(View v) {
        if (deck.equalsIgnoreCase("r"))
            v.getLocalGameInstance().setTopOfResourceDeck(resource);
        else if (deck.equalsIgnoreCase("g"))
            v.getLocalGameInstance().setTopOfGoldDeck(resource);

        v.getLocalGameInstance().nextTurn();
    }
}
