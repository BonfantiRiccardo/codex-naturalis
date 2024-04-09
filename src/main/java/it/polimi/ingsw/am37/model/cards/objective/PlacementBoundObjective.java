package it.polimi.ingsw.am37.model.cards.objective;

import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Kingdom;

/**
 * The PlacementBoundObjective abstract class represent a generic placement bound objective card and is used as a
 * blueprint for cards that require different placements. It is a subclass of the ObjectiveCard abstract class.
 */
public abstract class PlacementBoundObjective extends ObjectiveCard {
    /**
     * The cardColourThatTriggersCheck attribute contains the colour of the card that will trigger the check on neighbour
     * cards in the calculateNumOfCompletion(k) method.
     */
    protected final Resource cardColourThatTriggersCheck;
    /**
     * The otherResource attribute contains the colour of the other two cards that are required to complete the objective.
     */
    protected final Resource otherResource;

    /**
     * The PlacementBoundObjective(id, points, otherResource, cardColourThatTriggersCheck) constructor uses the
     * constructor of the superclass to assign the id and the points, and then it assigns the otherResource and the
     * cardColourThatTriggersCheck attributes to the ones given as parameters.
     * @param id An integer to uniquely identify the card.
     * @param points An integer that represents the points given per completion.
     * @param otherResource A Resource that is needed apart from the main resource that triggers the check.
     * @param cardColourThatTriggersCheck A Resource that triggers the check for the rquested pattern.
     */
    public PlacementBoundObjective(int id, int points, Resource otherResource, Resource cardColourThatTriggersCheck) {
        super(id, points);
        this.otherResource = otherResource;
        this.cardColourThatTriggersCheck = cardColourThatTriggersCheck;
    }

    /**
     * The getOtherResource() method returns the value of the otherResource attribute.
     * @return The otherResource attribute.
     */
    public Resource getOtherResource(){
        return otherResource;
    }

    /**
     * The getCardColourThatTriggersCheck() method returns the value of the cardColourThatTriggersCheck attribute.
     * @return The cardColourThatTriggersCheck attribute.
     */
    public Resource getCardColourThatTriggersCheck(){
        return cardColourThatTriggersCheck;
    }

    /**
     * The calculateNumOfCompletion(kingdom) method receives the kingdom of the player as a parameter and calculates
     * the number of times that the objective is satisfied, returning said number.
     * @param kingdom The kingdom attribute of the player, where the cards are placed.
     * @return The number of times the objective is satisfied by the player.
     */
    public abstract int calculateNumOfCompletion(Kingdom kingdom);

    @Override
    public String toString() {
        return super.toString() + ", cardCheckColour: " + cardColourThatTriggersCheck + ", otherCardColour: " + otherResource;
    }
}

