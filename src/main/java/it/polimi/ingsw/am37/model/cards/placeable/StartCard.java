package it.polimi.ingsw.am37.model.cards.placeable;

import it.polimi.ingsw.am37.model.cards.Card;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.sides.Back;
import it.polimi.ingsw.am37.model.sides.Front;

import java.util.List;
import java.util.Map;

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

    /**
     * the toString method shows to the player his starting card.
     * @param utfActive tells if the encoding is enable or not.
     * @return a string representing the starting card.
     */
    public String toString(boolean utfActive){
        int id = this.getId();

        Map<Resource, String> resMap = Card.resourceToString(utfActive);


        if (utfActive) {
            resMap.put(Resource.EMPTY, "⠀⠀");
            return             "┌────┬─────────────┬────┐"                                                                                                                                                                                                                                       + "    ┌────┬─────────────┬────┐\n" +
                               "│ "+resMap.get(getFront().getTL().getResource()) +" │             │ "+resMap.get(getFront().getTR().getResource())+" │"                                                                                                                                          + "    │ "+ resMap.get(getBack().getTL().getResource()) + " │     ⠀⠀⠀    │ "+ resMap.get(getBack().getTR().getResource()) + " │\n" +
                               "│────┘             └────│"                                                                                                                                                                                                                                       + "    │────┘             └────│\n" +
                    (id == 81? "│⠀⠀⠀⠀⠀⠀    🦋          │"                                                                                                                                                                                                                                       + "    │                       │\n" :
                     id == 82? "│⠀⠀⠀⠀⠀⠀    🍄          │"                                                                                                                                                                                                                                       + "    │                       │\n" :
                     id == 83? "│⠀⠀⠀      🍁🍄         │"                                                                                                                                                                                                                                      +  "    │                       │\n" :
                     id == 84? "│⠀⠀⠀      🐺🦋         │"                                                                                                                                                                                                                                       + "    │                       │\n" :
                     id == 85? "│        🐺🦋🍁        │"                                                                                                                                                                                                                                       + "    │                       │\n" :
                     id == 86? "│        🍁🐺🍄        │"                                                                                                                                                                                                                                       + "    │                       │\n" : "│                       │   │                       │\n") +
                               "│"+ (getFront().getBL().getVisibility() ? "────┐" : "     ") + "             "+ (getFront().getBR().getVisibility() ? "┌────" : "     ") +"│"                                                                                                                    + "    │────┐             ┌────│\n" +
                               "│"+ (getFront().getBL().getVisibility() ? " " + resMap.get(getFront().getBL().getResource()) + " │" : "     ") + "             " + (getFront().getBR().getVisibility() ? "│ " + resMap.get(getFront().getBR().getResource()) + " " : "     ") + "│"             +  "    │ " + resMap.get(getBack().getBL().getResource()) + " │    ⠀⠀     ⠀│ "+ resMap.get(getBack().getBR().getResource()) + " │\n" +
                               "└────" + (getFront().getBL().getVisibility() ? "┴":"─") + "─────────────" + (getFront().getBR().getVisibility() ? "┴":"─") + "────┘"                                                                                                                             + "    └────┴─────────────┴────┘";

        } else {
            resMap.put(Resource.EMPTY, "  ");
            return   "┌────┬─────────────┬────┐"                                                                                                                                                                                                                                       + "    ┌────┬─────────────┬────┐\n" +
                     "│ "+resMap.get(getFront().getTL().getResource()) +" │             │ "+resMap.get(getFront().getTR().getResource())+" │"                                                                                                                                          + "    │ "+ resMap.get(getBack().getTL().getResource()) + " │             │ "+ resMap.get(getBack().getTR().getResource()) + " │\n" +
                     "│────┘             └────│"                                                                                                                                                                                                                                       + "    │────┘             └────│\n" +
          (id == 81? "│           I           │"                                                                                                                                                                                                                                       + "    │                       │\n" :
           id == 82? "│           F           │"                                                                                                                                                                                                                                       + "    │                       │\n" :
           id == 83? "│          P F          │"                                                                                                                                                                                                                                      +  "    │                       │\n" :
           id == 84? "│          A I          │"                                                                                                                                                                                                                                       + "    │                       │\n" :
           id == 85? "│         A I P         │"                                                                                                                                                                                                                                       + "    │                       │\n" :
           id == 86? "│         P A F         │"                                                                                                                                                                                                                                       + "    │                       │\n" : "│                       │   │                       │\n") +
                     "│"+ (getFront().getBL().getVisibility() ? "────┐" : "     ") + "             "+ (getFront().getBR().getVisibility() ? "┌────" : "     ") +"│"                                                                                                                    + "    │────┐             ┌────│\n" +
                     "│"+ (getFront().getBL().getVisibility() ? " " + resMap.get(getFront().getBL().getResource()) + " │" : "     ") + "             " + (getFront().getBR().getVisibility() ? "│ " + resMap.get(getFront().getBR().getResource()) + " " : "     ") + "│"             +  "    │ " + resMap.get(getBack().getBL().getResource()) + " │             │ "+ resMap.get(getBack().getBR().getResource()) + " │\n" +
                     "└────" + (getFront().getBL().getVisibility() ? "┴":"─") + "─────────────" + (getFront().getBR().getVisibility() ? "┴":"─") + "────┘"                                                                                                                             + "    └────┴─────────────┴────┘";

        }

    }
}