package it.polimi.ingsw.am37.messages.turns;

import it.polimi.ingsw.am37.messages.MessageId;
import it.polimi.ingsw.am37.messages.MessageToClient;
import it.polimi.ingsw.am37.model.cards.placeable.GoldCard;
import it.polimi.ingsw.am37.model.cards.placeable.ResourceCard;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;

import java.util.ArrayList;
import java.util.List;

public class UpdateAvailableView extends MessageToClient {
    private final String deck;
    private final Resource topOfDeck;
    private final String availableChanged;      //OR THE NAME OF THE LIST THAT CHANGED
    private final List<Integer> available;

    public UpdateAvailableView(MessageId id, String deck, Resource topOfDeck, String availableChanged, List<Integer> available) {
        super(id);
        this.deck = deck;
        this.topOfDeck = topOfDeck;
        this.availableChanged = availableChanged;
        this.available = available;
    }

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

        if (v.getState().equals(ViewState.DRAW)) {
            synchronized (v) {
                v.setState(ViewState.NOT_TURN);
                v.notify();
            }
        }
    }
}
