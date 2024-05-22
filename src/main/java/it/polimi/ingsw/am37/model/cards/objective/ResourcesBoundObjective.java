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

    public String toString(boolean utfActive){
        int points = this.getPointsGiven();
        int id= this.getId();

        if (utfActive) {
            return switch (id) {
                case 95 -> "┌───────────────┐\n" +
                        "│ id:" + id + "  pt:" + points + "   │\n" +
                        "│               │\n" +
                        "│ resources:    │\n" +
                        "│   🍄 🍄 🍄   │\n" +
                        "└───────────────┘";
                case 96 -> "┌───────────────┐\n" +
                        "│ id:" + id + "  pt:" + points + "   │\n" +
                        "│               │\n" +
                        "│ resources:    │\n" +
                        "│   🍁 🍁 🍁   │\n" +
                        "└───────────────┘";
                case 97 -> "┌───────────────┐\n" +
                        "│ id:" + id + "  pt:" + points + "   │\n" +
                        "│               │\n" +
                        "│ resources:    │\n" +
                        "│   🐺 🐺 🐺   │\n" +
                        "└───────────────┘";
                case 98 -> "┌───────────────┐\n" +
                        "│ id:" + id + "  pt:" + points + "   │\n" +
                        "│               │\n" +
                        "│ resources:    │\n" +
                        "│   🦋 🦋 🦋   │\n" +
                        "└───────────────┘";
                case 99 -> "┌───────────────┐\n" +
                        "│ id:" + id + "  pt:" + points + "   │\n" +
                        "│               │\n" +
                        "│ resources:    │\n" +
                        "│   📜 🪶 🖋️   │\n" +
                        "└───────────────┘";
                //🥃
                case 100 -> "┌───────────────┐\n" +
                        "│ id:" + id + "  pt:" + points + "  │\n" +
                        "│               │\n" +
                        "│ resources:    │\n" +
                        "│     📜 📜 ⠀⠀⠀│\n" +
                        "└───────────────┘";
                case 101 -> "┌───────────────┐\n" +
                        "│ id:" + id + "  pt:" + points + "  │\n" +
                        "│               │\n" +
                        "│ resources:    │\n" +
                        "│     🖋️ 🖋️ ⠀⠀⠀│\n" +
                        "└───────────────┘";
                case 102 -> "┌───────────────┐\n" +
                        "│ id:" + id + "  pt:" + points + "  │\n" +
                        "│               │\n" +
                        "│ resources:    │\n" +
                        "│     🪶 🪶⠀⠀⠀ │\n" +
                        "└───────────────┘";
                default -> "error";
            };
        } else {
            return switch (id) {
                case 95 -> "┌───────────────┐\n" +
                        "│ id:" + id + "  pt:" + points + "   │\n" +
                        "│               │\n" +
                        "│ resources:    │\n" +
                        "│    F  F  F    │\n" +
                        "└───────────────┘";
                case 96 -> "┌───────────────┐\n" +
                        "│ id:" + id + "  pt:" + points + "   │\n" +
                        "│               │\n" +
                        "│ resources:    │\n" +
                        "│    P  P  P    │\n" +
                        "└───────────────┘";
                case 97 -> "┌───────────────┐\n" +
                        "│ id:" + id + "  pt:" + points + "   │\n" +
                        "│               │\n" +
                        "│ resources:    │\n" +
                        "│    A  A  A    │\n" +
                        "└───────────────┘";
                case 98 -> "┌───────────────┐\n" +
                        "│ id:" + id + "  pt:" + points + "   │\n" +
                        "│               │\n" +
                        "│ resources:    │\n" +
                        "│    I  I  I    │\n" +
                        "└───────────────┘";
                case 99 -> "┌───────────────┐\n" +
                        "│ id:" + id + "  pt:" + points + "   │\n" +
                        "│               │\n" +
                        "│ resources:    │\n" +
                        "│    M  Q  N    │\n" +
                        "└───────────────┘";
                //🥃
                case 100 -> "┌───────────────┐\n" +
                        "│ id:" + id + "  pt:" + points + "  │\n" +
                        "│               │\n" +
                        "│ resources:    │\n" +
                        "│      M  M     │\n" +
                        "└───────────────┘";
                case 101 -> "┌───────────────┐\n" +
                        "│ id:" + id + "  pt:" + points + "  │\n" +
                        "│               │\n" +
                        "│ resources:    │\n" +
                        "│      N  N     │\n" +
                        "└───────────────┘";
                case 102 -> "┌───────────────┐\n" +
                        "│ id:" + id + "  pt:" + points + "  │\n" +
                        "│               │\n" +
                        "│ resources:    │\n" +
                        "│      Q  Q     │\n" +
                        "└───────────────┘";
                default -> "error";
            };
        }

    }
}