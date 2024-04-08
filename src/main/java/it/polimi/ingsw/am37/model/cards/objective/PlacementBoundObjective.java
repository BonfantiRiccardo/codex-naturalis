package it.polimi.ingsw.am37.model.cards.objective;

import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Kingdom;

public abstract class PlacementBoundObjective extends ObjectiveCard {
    protected final Resource otherResource;
    protected final Resource cardColorThatTriggersCheck;


    public Resource getOtherResource(){
        return otherResource;
    }

    public Resource getCardColorThatTriggersCheck(){
        return cardColorThatTriggersCheck;
    }
    /**
     * creates the card PlacementBoundObjective
     * @param id An integer to uniquely identify the card.
     * @param points An integer that represents the points given per completion.
     * @param otherResource A Resource that is needed apart from the main resource that triggers the check
     * @param cardColorThatTriggersCheck A Resource that triggers the check fot the pattern
     */
    public PlacementBoundObjective(int id, int points, Resource otherResource, Resource cardColorThatTriggersCheck) {
        super(id, points);
        this.otherResource = otherResource;
        this.cardColorThatTriggersCheck = cardColorThatTriggersCheck;
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
        return super.toString() + ", cardCheckColour: " + cardColorThatTriggersCheck + ", otherCardColour: " + otherResource;
    }
}

