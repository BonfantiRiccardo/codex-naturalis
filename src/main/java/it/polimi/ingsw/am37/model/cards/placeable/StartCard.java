package it.polimi.ingsw.am37.model.cards.placeable;

import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.sides.Back;
import it.polimi.ingsw.am37.model.sides.Front;

import java.util.List;

/**
 * The StartCard class represents a generic start card of the game. It is a subclass of GameCard.
 */
public class StartCard extends GameCard {
    /**
     * The backResource is a list of the resource given if the Back of the StartCard is placed in the Kingdom.
     */
    private final List<Resource> backResource;

    /**
     * The StartCard(id, front, back) constructor uses the constructor of the superclasses to assign the id, the front
     * and the back.
     * @param id An integer to uniquely identify the card.
     * @param front A Front object that represents the front side of the card.
     * @param back A Back object that represents the back side of the card.
     * @param backResource A list of the Resources that are present on the back of the card.
     */
    public StartCard(int id, Front front, Back back, List<Resource> backResource){
        super(id, front, back);
        this.backResource = backResource;
    }

    /**
     * The getBackResource() returns the list of resources that are present on the back of the StartCard.
     * @return The backResource attribute.
     */
    public List<Resource> getBackResource(){
        return backResource;
    }

    @Override
    public String toString() {
        return super.toString() + " | Permanent Resources: " + backResource;
    }
}
