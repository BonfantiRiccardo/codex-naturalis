package it.polimi.ingsw.am37.messages.initialization;

import it.polimi.ingsw.am37.messages.MessageId;
import it.polimi.ingsw.am37.messages.MessageToClient;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.GoldCard;
import it.polimi.ingsw.am37.model.cards.placeable.ResourceCard;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;

import java.util.ArrayList;
import java.util.List;

public class InitMessage extends MessageToClient {
    private final List<Integer> availableGold;
    private final List<Integer> availableResource;
    private final int startCard;
    private final List<Integer> hand;
    private final List<Integer> publicObjectives;
    private final List<Integer> privateObjectives;
    private final Resource goldDeckBack;
    private final Resource resourceDeckBack;

    public InitMessage(MessageId id, List<Integer> availableGold, List<Integer> availableResource, int startCard, List<Integer> hand,
                       List<Integer> publicObjectives, List<Integer> privateObjectives, Resource goldDeckBack, Resource resourceDeckBack) {
        super(id);
        this.availableGold = availableGold;
        this.availableResource = availableResource;
        this.startCard = startCard;
        this.hand = hand;
        this.publicObjectives = publicObjectives;
        this.privateObjectives = privateObjectives;
        this.goldDeckBack = goldDeckBack;
        this.resourceDeckBack = resourceDeckBack;
    }

    @Override
    public void decodeAndExecute(View v) {
        synchronized (v) {
            List<StandardCard> available = new ArrayList<>();

            for (ResourceCard rc: v.getLocalGameInstance().getResourceCards()) {
                if (rc.getId() == availableResource.get(0))
                    available.add(rc);
                else if (rc.getId() == availableResource.get(1))
                    available.add(rc);

                if (available.size() == 2)
                    break;
            }

            v.getLocalGameInstance().setAvailableResourceCards(available);
            available.clear();

            for (GoldCard gc: v.getLocalGameInstance().getGoldCards()) {
                if (gc.getId() == availableGold.get(0))
                    available.add(gc);
                else if (gc.getId() == availableGold.get(1))
                    available.add(gc);

                if (available.size() == 2)
                    break;
            }

            v.getLocalGameInstance().setAvailableGoldCards(available);


            List<StandardCard> myHand = new ArrayList<>();
            for (int i: hand) {
                if (i > 0 && i <= 40) {
                    for (ResourceCard rc: v.getLocalGameInstance().getResourceCards())
                        if (rc.getId() == i)
                            myHand.add(rc);

                } else if (i > 40 && i <= 80)
                    for (GoldCard gc: v.getLocalGameInstance().getGoldCards())
                        if (gc.getId() == i)
                            myHand.add(gc);

                if (myHand.size() == 3)
                    break;
            }

            v.getLocalGameInstance().setMyHand(myHand);


            for (StartCard sc: v.getLocalGameInstance().getStartCards())
                if (sc.getId() == startCard) {
                    v.getLocalGameInstance().setMyStartCard(sc);
                    break;
                }



            List<ObjectiveCard> publicObj = new ArrayList<>();
            List<ObjectiveCard> privateObj = new ArrayList<>();

            for (ObjectiveCard oc: v.getLocalGameInstance().getObjectiveCards()) {
                if (oc.getId() == publicObjectives.get(0))
                    publicObj.add(oc);
                else if (oc.getId() == publicObjectives.get(1))
                    publicObj.add(oc);

                if (oc.getId() == privateObjectives.get(0))
                    privateObj.add(oc);
                else if (oc.getId() == privateObjectives.get(1))
                    privateObj.add(oc);

                if (publicObj.size() == 2 && privateObj.size() == 2)
                    break;
            }

            v.getLocalGameInstance().setTopOfGoldDeck(goldDeckBack);
            v.getLocalGameInstance().setTopOfResourceDeck(resourceDeckBack);

            v.printAvail();

            v.setState(ViewState.PLACE_SC);
            v.notify();
        }
    }

    public List<Integer> getAvailableGold() {
        return availableGold;
    }

    public List<Integer> getAvailableResource() {
        return availableResource;
    }

    public int getStartCard() {
        return startCard;
    }

    public List<Integer> getHand() {
        return hand;
    }

    public List<Integer> getPublicObjectives() {
        return publicObjectives;
    }

    public List<Integer> getPrivateObjectives() {
        return privateObjectives;
    }

    public Resource getGoldDeckBack() {
        return goldDeckBack;
    }

    public Resource getResourceDeckBack() {
        return resourceDeckBack;
    }
}
