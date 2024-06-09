package it.polimi.ingsw.am37.network.messages.turns;

import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToClient;
import it.polimi.ingsw.am37.model.cards.placeable.GoldCard;
import it.polimi.ingsw.am37.model.cards.placeable.ResourceCard;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;

import java.util.ArrayList;
import java.util.List;

/**
 * This message is sent to the client to update the available cards in the view.
 * It contains the deck that changed, the top card of the deck, the name of the list that changed and the new available cards.
 */
public class UpdateAvailableView extends MessageToClient {
    /**
     * The deck that changed.
     */
    private final String deck;
    /**
     * The top card of the deck.
     */
    private final Resource topOfDeck;
    /**
     * The name of the list that changed.
     */
    private final String availableChanged;
    /**
     * The new available cards.
     */
    private final List<Integer> available;

    /**
     * Constructor.
     * @param id The message id.
     * @param deck The deck that changed.
     * @param topOfDeck The top card of the deck.
     * @param availableChanged The name of the list that changed.
     * @param available The new available cards.
     */
    public UpdateAvailableView(MessageId id, String deck, Resource topOfDeck, String availableChanged, List<Integer> available) {
        super(id);
        this.deck = deck;
        this.topOfDeck = topOfDeck;
        this.availableChanged = availableChanged;
        this.available = available;
    }

    /**
     * This method updates the available cards in the view.
     * It sets the new available cards in the local game instance and notifies the view.
     * It also sets the top card of the deck in the local game instance.
     * It sets the state of the view to NOT_TURN and notifies the view.
     * @param v The view.
     */
    @Override
    public void decodeAndExecute(View v) {
        List<StandardCard> newAvailable = new ArrayList<>();

        if (availableChanged.equalsIgnoreCase("resource")) {
            for (ResourceCard rc: v.getLocalGameInstance().getResourceCards())
                if (available.contains(rc.getId())) {
                    newAvailable.add(rc);

                    if (newAvailable.size() == 2)
                        break;
                }

            v.getLocalGameInstance().setAvailableResourceCards(newAvailable);
        } else if (availableChanged.equalsIgnoreCase("gold")) {
            for (GoldCard gc: v.getLocalGameInstance().getGoldCards())
                if (available.contains(gc.getId())) {
                    newAvailable.add(gc);

                    if (newAvailable.size() == 2)
                        break;
                }

            v.getLocalGameInstance().setAvailableGoldCards(newAvailable);
        }

        if (deck.equalsIgnoreCase("r"))
            v.getLocalGameInstance().setTopOfResourceDeck(topOfDeck);
        else if (deck.equalsIgnoreCase("g"))
            v.getLocalGameInstance().setTopOfGoldDeck(topOfDeck);

        v.getLocalGameInstance().nextTurn();

        if (v.getState().equals(ViewState.DRAW)) {
            v.setState(ViewState.NOT_TURN);
            synchronized (v) {
                v.notify();
            }
        }
    }
}
