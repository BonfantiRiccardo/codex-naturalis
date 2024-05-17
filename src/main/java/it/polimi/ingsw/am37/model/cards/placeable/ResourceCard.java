package it.polimi.ingsw.am37.model.cards.placeable;

import it.polimi.ingsw.am37.model.cards.Card;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.sides.Back;
import it.polimi.ingsw.am37.model.sides.Front;

import java.util.Map;

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

    public String toString() {

        int points = this.getFront().getPointsGivenOnPlacement();

        Map<Resource, String> resMap = Card.resourceToString();

        return  "┌────" + (getFront().getTL().getVisibility() ? "┬": "─") + "─────────────" + (getFront().getTR().getVisibility() ? "┬": "─") + "────┐" +                                                                                                                                          "    ┌────┬─────────────┬────┐\n" +
                "│" + (getFront().getTL().getVisibility() ? " " + resMap.get(getFront().getTL().getResource()) + " │      " : "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀") + points +  (getFront().getTR().getVisibility() ? "⠀⠀⠀⠀⠀│ " + resMap.get(getFront().getTR().getResource()) + " " : "⠀⠀⠀⠀ ⠀⠀⠀ ⠀") + "│" +            "    │    │ ⠀⠀   ⠀⠀⠀⠀⠀ │    │\n" +
                "│" + (getFront().getTL().getVisibility() ? "────┘" : "     ") + "             " + (getFront().getTR().getVisibility() ? "└────" : "     ") + "│" +                                                                                                                                "    │────┘             └────│\n" +
                "│                       │" +                                                                                                                                                                                                                                                      "    │          " + (resMap.get(getBack().getMainResource()))+ "⠀⠀⠀⠀⠀⠀    │\n" +
                "│" + (getFront().getBL().getVisibility() ? "────┐" : "     ") + "             " + (getFront().getBR().getVisibility() ? "┌────" : "     ") + "│" +                                                                                                                                "    │────┐             ┌────│\n" +
                "│" + (getFront().getBL().getVisibility() ? " " + resMap.get(getFront().getBL().getResource()) + " │" : "⠀⠀⠀⠀⠀") + "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" + (getFront().getBR().getVisibility() ? "│ " + resMap.get(getFront().getBR().getResource()) + " " : "⠀⠀⠀⠀⠀") + "│" +                           "    │    │⠀⠀⠀⠀⠀⠀⠀⠀⠀ ⠀⠀│    │\n" +
                "└────" + (getFront().getBL().getVisibility() ? "┴":"─") + "─────────────" + (getFront().getBR().getVisibility() ? "┴":"─") + "────┘" +                                                                                                                                            "    └────┴─────────────┴────┘";
    }
}