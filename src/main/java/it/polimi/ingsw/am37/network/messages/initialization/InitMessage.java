package it.polimi.ingsw.am37.network.messages.initialization;

import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToClient;
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
/**
 * This message is sent by the server to the client to initialize the game.
 * The server will send the available cards, the hand of the player, the start card,
 * the public and private objectives and the back of the decks.
 */
public class InitMessage extends MessageToClient {
    /**
     * The list of the available gold cards.
     */
    private final List<Integer> availableGold;
    /**
     * The list of the available resource cards.
     */
    private final List<Integer> availableResource;
    /**
     * The id of the start card.
     */
    private final int startCard;
    /**
     * The hand of the player.
     */
    private final List<Integer> hand;
    /**
     * The list of the public objectives.
     */
    private final List<Integer> publicObjectives;
    /**
     * The list of the private objectives.
     */
    private final List<Integer> privateObjectives;
    /**
     * The back of the gold deck.
     */
    private final Resource goldDeckBack;
    /**
     * The back of the resource deck.
     */
    private final Resource resourceDeckBack;

    /**
     * Constructor of the class.
     * @param id The id of the message.
     * @param availableGold The list of the available gold cards.
     * @param availableResource The list of the available resource cards.
     * @param startCard The id of the start card.
     * @param hand The hand of the player.
     * @param publicObjectives The list of the public objectives.
     * @param privateObjectives The list of the private objectives.
     * @param goldDeckBack The back of the gold deck.
     * @param resourceDeckBack The back of the resource deck.
     */
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

    /**
     * This method is called by the client to execute the action that the message
     * carries.
     * The client will set the available cards, the hand of the player, the start card,
     * the public and private objectives and the back of the decks.
     * The client will set the state to PLACE_SC.
     * The client will notify the view.
     * @param v The view of the client.
     */
    @Override
    public void decodeAndExecute(View v) {
        synchronized (v) {
            List<StandardCard> availableRes = new ArrayList<>();

            for (ResourceCard rc: v.getLocalGameInstance().getResourceCards()) {
                if (rc.getId() == availableResource.get(0))
                    availableRes.add(rc);
                else if (rc.getId() == availableResource.get(1))
                    availableRes.add(rc);

                if (availableRes.size() == 2)
                    break;
            }

            v.getLocalGameInstance().setAvailableResourceCards(availableRes);

            List<StandardCard> availableG = new ArrayList<>();
            for (GoldCard gc: v.getLocalGameInstance().getGoldCards()) {
                if (gc.getId() == availableGold.get(0))
                    availableG.add(gc);
                else if (gc.getId() == availableGold.get(1))
                    availableG.add(gc);

                if (availableG.size() == 2)
                    break;
            }

            v.getLocalGameInstance().setAvailableGoldCards(availableG);

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
            v.getLocalGameInstance().setPrivateObjectives(privateObj);
            v.getLocalGameInstance().setPublicObjectives(publicObj);


            v.getLocalGameInstance().setTopOfGoldDeck(goldDeckBack);
            v.getLocalGameInstance().setTopOfResourceDeck(resourceDeckBack);


            v.setState(ViewState.PLACE_SC);
            v.notify();
        }
    }
}
