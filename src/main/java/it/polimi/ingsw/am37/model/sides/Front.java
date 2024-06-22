package it.polimi.ingsw.am37.model.sides;

import it.polimi.ingsw.am37.model.game.Resource;

import java.util.Hashtable;

/**
 * The Front class represents the concept of the front side of a generic GameCard.
 */

public class Front extends Side {
    /**
     * pointsGivenOnPlacement attribute is an integer that indicates how many points the player can receive when a
     * generic GameCard gets played.
     */
    private final int pointsGivenOnPlacement;
    /**
     * resourcePlacementCondition attribute is a HashTable that contains what kind and how many visible objects the
     * player needs to have in the Kingdom to be able to play the card. It is not null only when the card is a GoldCard.
     */
    private final Hashtable<Resource, Integer> resourcePlacementCondition;
    /**
     * bonus attribute indicates how many points the player can receive when a generic GameCard gets played.
     * bonus is not null when the assigned points are based on the corner coverage of the played card or on
     * the amount of visible objects in the player's Kingdom.
     * It is an object of type Bonus.
     */
    private final Bonus bonus;

    /**
     * The Front(tl, tr, bl, br, mainResource, pointsGivenOnPlacement, resourcePlacementCondition, bonus) constructor
     * uses the constructor of the superclass to create the corners of the front of a generic GameCard and assigns the
     * pointsGivenOnPlacement, resourcePlacementCondition and bonus points to the ones given as parameters.
     * @param tl A Corner object representing the top left corner of the front of a GameCard.
     * @param tr A Corner object representing the top right corner of the front of a GameCard.
     * @param bl A Corner object representing the bottom left corner of the front of a GameCard.
     * @param br A Corner object representing the bottom right corner of the front of a GameCard.
     * @param mainResource A Resource object that represents the colour of the Card.
     * @param pointsGivenOnPlacement An integer that indicates the points that the player can receive when the card gets played.
     * @param resourcePlacementCondition A Hash Table that contains the objects that the player needs in the Kingdom to be able to play the card.
     * @param bonus A Bonus object that indicates the way the player gets points after placing the card.
     */
    public Front(Corner tl, Corner tr, Corner bl, Corner br, Resource mainResource, int pointsGivenOnPlacement,
                 Hashtable<Resource, Integer> resourcePlacementCondition, Bonus bonus) {
        super(tl, tr, bl, br, mainResource);
        this.pointsGivenOnPlacement = pointsGivenOnPlacement;
        this.resourcePlacementCondition = resourcePlacementCondition;
        this.bonus = bonus;
    }

    /**
     * The getPointsGivenOnPlacement() method returns the points that the player can receive when the card gets played.
     * @return The pointsGivenOnPlacement attribute.
     */
    public int getPointsGivenOnPlacement() {
        return pointsGivenOnPlacement;
    }

    /**
     * The getResourcePlacementCondition() method returns the Hash Table of the needed objects in order to place the
     * card.
     * @return The resourcePlacementCondition attribute.
     */
    public Hashtable<Resource, Integer> getResourcePlacementCondition() {
        return resourcePlacementCondition;
    }

    /**
     * The getBonus() method returns the bonus points the player can receive when the card gets played.
     * @return The bonus attribute.
     */
    public Bonus getBonus() {
        return bonus;
    }

    /**
     * The isPlacementConditionSatisfied() method verifies that the player has
     * enough resources in the Kingdom to be able to place the GoldCard by confronting the amount of resources in the
     * Hashtable myRes given as parameter and the Hashtable resourcePlacementCondition.
     * @param myRes A Hash Table that contains how many visible resources the player has in his Kingdom.
     * @return A boolean that indicates if the Card can or can't be played.
     */
    public boolean isPlacementConditionSatisfied (Hashtable<Resource, Integer> myRes) {
        boolean check;
        check = true;
        if (resourcePlacementCondition != null) {
            for (Resource s : resourcePlacementCondition.keySet()) {
                if (myRes.containsKey(s)) {
                    check = myRes.get(s) >= resourcePlacementCondition.get(s);
                }else check = false;
                if (!check)
                    break;
            }
        }
        return check;
    }
    /**
     * the toString method is used for debugging and prints  the front side of the card and where it's placed.
     * @return a string representing the front side of the card.
     */
    @Override
    public String toString() {
        return super.toString() + ((pointsGivenOnPlacement>0) ? (", pointsGOP: " + pointsGivenOnPlacement) : "") +
                ((resourcePlacementCondition!=null) ? (", placCond: " + resourcePlacementCondition) : "") +
                ((bonus != null) ? (", bonus: " + bonus) : "");
    }
}