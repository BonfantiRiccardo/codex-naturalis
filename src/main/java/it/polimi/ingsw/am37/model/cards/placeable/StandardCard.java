package it.polimi.ingsw.am37.model.cards.placeable;

import it.polimi.ingsw.am37.model.sides.Back;
import it.polimi.ingsw.am37.model.sides.Front;

/**
 * The StandardCard abstract class represents a generic gold card from the game, has two sides and has a
 * main Resource, which can also be interpreted as his colour. It serves as a blueprint for more specific standard
 * cards. It is a subclass of the GameCard abstract class.
 */
public abstract class StandardCard extends GameCard {
    /**
     * The StandardCard(id, front, back, mainResource) constructor uses the constructor of the superclass to assign the
     * id, front and back sides of the card and then assigns the mainResource to the one given as a parameter.
     * @param id An integer to uniquely identify the card.
     * @param front A Front object that represents the front side of the card.
     * @param back  A Back object that represents the back side of the card.
     */
    public StandardCard(int id, Front front, Back back) {
        super(id, front, back);
    }

    /**
     * the toString method returns front and back of each card.
     * @return front and back of each card.
     */
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * the toString method is used to show the cards to the player.
     * @param utfActive tells if the encoding is enable or not.
     * @return the cards.
     */
    public abstract String toString(boolean utfActive);
}
