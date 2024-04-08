package it.polimi.ingsw.am37.model.cards.placeable;

import it.polimi.ingsw.am37.model.sides.Back;
import it.polimi.ingsw.am37.model.sides.Front;

/**
 * The ResourceCard class represents a generic resource card of the game. It is a subclass of the StandardCard
 * abstract class.
 */
public class ResourceCard extends StandardCard {
    /**
     * The ResourceCard(id, front, back, mainResource) constructor uses the constructor of the superclass to assign the
     * id, front side, back side and mainResource.
     * @param id An integer to uniquely identify the card.
     * @param front A Front object that represents the front side of the card.
     * @param back  A Back object that represents the back side of the card.
     */
    public ResourceCard(int id, Front front, Back back){
        super(id, front, back);
    }
}
