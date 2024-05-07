package it.polimi.ingsw.am37.model.cards.placeable;

import it.polimi.ingsw.am37.model.cards.Card;
import it.polimi.ingsw.am37.model.sides.Back;
import it.polimi.ingsw.am37.model.sides.Front;

/**
 * The GameCard abstract class represents a generic card that is placeable during the game and that has two sides.
 * It serves as a blueprint for more specific game cards. It is a subclass of the Card abstract class.
 */
public abstract class GameCard extends Card {
    /**
     * The front attribute represents the front side of the card. It is an object of type Front.
     */
    private final Front front;
    /**
     * The back attribute represents the back side of the card. It is an object of type Back.
     */
    private final Back back;

    /**
     * The GameCard(id, front, back) constructor uses the constructor of the superclass to assign the id and assigns
     * the front and back sides of the card to the ones given as parameters.
     * @param id An integer to uniquely identify the card.
     * @param front A Front object that represents the front side of the card.
     * @param back A Back object that represents the back side of the card.
     */
    public GameCard(int id, Front front, Back back) {
        super(id);
        this.front = front;
        this.back = back;
    }

    /**
     * The getFront() method returns a reference to the front side of the card.
     * @return The Front attribute.
     */
    public Front getFront() {
        return front;
    }

    /**
     * The getBack() method returns a reference to the back side of the card.
     * @return The back attribute.
     */
    public Back getBack() {
        return back;
    }

    @Override
    public String toString() {
        return super.toString() + " | FRONT: " + front.toString() + " | BACK: " + back.toString();
    }
}
