package it.polimi.ingsw.am37.model.sides;

import it.polimi.ingsw.am37.model.game.Resource;

/**
 * The Back class represents the concept of the back side of a generic GameCard.
 */
public class Back extends Side {

    /**
     * The Back(tl, tr, bl, br) constructor uses the
     * constructor of the superclass to create the corners of the front of a generic GameCard and assigns the
     * pointsGivenOnPlacement, resourcePlacementCondition and bonus points to the ones given as parameters.
     * @param tl A Corner object representing the top left corner of the front of a GameCard.
     * @param tr A Corner object representing the top right corner of the front of a GameCard.
     * @param bl A Corner object representing the bottom left corner of the front of a GameCard.
     * @param br A Corner object representing the bottom right corner of the front of a GameCard.
     * @param mainResource A Resource object that represents the colour of the Card.
     */
    public Back(Corner tl, Corner tr, Corner bl, Corner br, Resource mainResource) {
        super(tl, tr, bl, br, mainResource);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
