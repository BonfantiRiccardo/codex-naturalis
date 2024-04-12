package it.polimi.ingsw.am37.model.cards.objective;

import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Kingdom;

import java.util.Hashtable;

/**
 * The ResourcesBoundObjective class represents the specific objective cards that require a certain amount of visible
 * objects in the Kingdom to obtain the points indicated on the objective card.
 */
public class ResourcesBoundObjective extends ObjectiveCard {
    /**
     * The resourceNeeded attribute represents the needed resources to obtain the points indicated on the card.
     * It is an object of type Hashtable.
     */
    private final Hashtable <Resource, Integer> resourceNeeded;

    /**
     * The ResourcesBoundObjective(id, points, resourceNeeded) constructor uses the constructor of the superclass
     * to assign the id and points and then assigns the resourceNeeded attribute to the one given as parameter.
     * @param id An integer to uniquely identify the card.
     * @param points An integer that indicates how many points can the Player can receive.
     * @param resourceNeeded A Hashtable that represents the needed resources to obtain the points indicated on the card.
     */
    public ResourcesBoundObjective(int id, int points, Hashtable<Resource, Integer> resourceNeeded) {
        super(id, points);
        this.resourceNeeded = resourceNeeded;
    }

    /**
     * The calculateNumOfCompletion(kingdom) method receives the kingdom of the player as a parameter and calculates
     * the number of times that the object is satisfied, returning said number.
     * @param kingdom The kingdom attribute of the player, where the cards are placed.
     * @return The number of times the objective is satisfied by the player.
     */
    public int calculateNumOfCompletion(Kingdom kingdom) {
        int completion = 0;
        boolean check = true;
        Hashtable<Resource, Integer> tmp = new Hashtable<>(kingdom.getOnFieldResources());
        while (check) {
            int count = 0;
            for (Resource r: resourceNeeded.keySet()) {
                if ( tmp.get(r) >= resourceNeeded.get(r)) {
                    tmp.replace(r, tmp.get(r) - resourceNeeded.get(r));
                    count++;
                } else {
                    check = false;
                    break;
                }
            }
            if (count == resourceNeeded.keySet().size()) {
                completion++;
            }
        }
        return completion;
    }

    @Override
    public String toString() {
        return super.toString() + ", resourceNeeded: " + resourceNeeded;
    }
}