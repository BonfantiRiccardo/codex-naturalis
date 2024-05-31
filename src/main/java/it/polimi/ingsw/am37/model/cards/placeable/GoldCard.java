package it.polimi.ingsw.am37.model.cards.placeable;

import it.polimi.ingsw.am37.model.cards.Card;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.sides.Back;
import it.polimi.ingsw.am37.model.sides.Front;

import java.util.Map;

/**
 * The GoldCard class represents a generic gold card of the game. It is a subclass of the StandardCard abstract class.
 */
public class GoldCard extends StandardCard {
    /**
     * The GoldCard(id, front, back, mainResource) constructor uses the constructor of the superclass to assign the
     * id, front side, back side and mainResource.
     * @param id An integer to uniquely identify the card.
     * @param front A Front object that represents the front side of the card.
     * @param back  A Back object that represents the back side of the card.
     */
    public GoldCard(int id, Front front, Back back){
        super(id, front, back);
    }

    /**
     * the toString method prints the gold card corresponding to the id.
     * @param utfActive tells if the encoding is enable or not.
     * @return the gold card corresponding to the id.
     */
    public String toString(boolean utfActive){
        String bonus;
        String plc;

        Map<Resource, String> resMap = Card.resourceToString(utfActive);

        if (utfActive) {
            plc = switch (this.getId()) {
                case 41 -> {
                    bonus = "1 🪶";
                    yield "⠀⠀⠀🍄🍄🐺⠀⠀";
                }
                case 42 -> {
                    bonus = "1 🖋️";
                    yield "⠀⠀⠀🍄🍄🍁⠀⠀";
                }
                case 43 -> {
                    bonus = "1 📜";
                    yield "⠀⠀⠀🍄🍄🦋⠀⠀";
                }
                case 44 -> {
                    bonus = "2 🔲";
                    yield " ⠀🍄🍄🍄🐺⠀";
                }
                case 45 -> {
                    bonus = "2 🔲";
                    yield "⠀⠀🍄🍄🍄🍁⠀";
                }
                case 46 -> {
                    bonus = "2 🔲";
                    yield "⠀⠀🍄🍄🍄🦋⠀";
                }
                case 47, 48, 49 -> {
                    bonus = "  3⠀";
                    yield "⠀⠀⠀🍄🍄🍄⠀⠀";
                }
                case 50 -> {
                    bonus = "  5⠀";
                    yield " 🍄🍄🍄🍄🍄";
                }
                case 51 -> {
                    bonus = "1 🪶";
                    yield "⠀⠀⠀🍁🍁🦋⠀⠀";
                }
                case 52 -> {
                    bonus = "1 📜";
                    yield "⠀⠀⠀🍁🍁🍄⠀⠀";
                }
                case 53 -> {
                    bonus = "1 🖋️";
                    yield "⠀⠀⠀🍁🍁🐺⠀⠀";
                }
                case 54 -> {
                    bonus = "2 🔲";
                    yield " ⠀🍁🍁🍁🦋⠀";
                }
                case 55 -> {
                    bonus = "2 🔲";
                    yield "⠀⠀🍁🍁🍁🐺⠀";
                }
                case 56 -> {
                    bonus = "2 🔲";
                    yield " ⠀🍁🍁🍁🍄⠀";
                }
                case 57, 58, 59 -> {
                    bonus = "  3⠀";
                    yield "⠀⠀⠀🍁🍁🍁⠀⠀";
                }
                case 60 -> {
                    bonus = "  5⠀";
                    yield " 🍁🍁🍁🍁🍁";
                }
                case 61 -> {
                    bonus = "1 🖋️";
                    yield "⠀⠀⠀🐺🐺🦋⠀⠀";
                }
                case 62 -> {
                    bonus = "1 📜";
                    yield "⠀⠀⠀🐺🐺🍁⠀⠀";
                }
                case 63 -> {
                    bonus = "1 🪶";
                    yield "⠀⠀⠀🐺🐺🍄⠀⠀";
                }
                case 64 -> {
                    bonus = "2 🔲";
                    yield "⠀⠀🐺🐺🐺🦋⠀";
                }
                case 65 -> {
                    bonus = "2 🔲";
                    yield " ⠀🐺🐺🐺🍄⠀";
                }
                case 66 -> {
                    bonus = "2 🔲";
                    yield " ⠀🐺🐺🐺🍁⠀";
                }
                case 67, 68, 69 -> {
                    bonus = "  3⠀";
                    yield "⠀⠀⠀🐺🐺🐺⠀⠀";
                }
                case 70 -> {
                    bonus = "  5⠀";
                    yield " 🐺🐺🐺🐺🐺";
                }
                case 71 -> {
                    bonus = "1 🪶";
                    yield "⠀⠀⠀🦋🦋🍁⠀⠀";
                }
                case 72 -> {
                    bonus = "1 📜";
                    yield "⠀⠀⠀🦋🦋🐺⠀⠀";
                }
                case 73 -> {
                    bonus = "1 🖋️";
                    yield "⠀⠀⠀🦋🦋🍄⠀⠀";
                }
                case 74 -> {
                    bonus = "2 🔲";
                    yield " ⠀🦋🦋🦋🐺⠀";
                }
                case 75 -> {
                    bonus = "2 🔲";
                    yield "⠀⠀🦋🦋🦋🍁⠀";
                }
                case 76 -> {
                    bonus = "2 🔲";
                    yield " ⠀🦋🦋🦋🍄⠀";
                }
                case 77, 78, 79 -> {
                    bonus = "  3⠀";
                    yield "⠀⠀⠀🦋🦋🦋⠀⠀";
                }
                case 80 -> {
                    bonus = "  5⠀";
                    yield " 🦋🦋🦋🦋🦋";
                }
                default -> {
                    bonus = "error";
                    yield "error";
                }
            };
        } else {
            plc = switch (this.getId()) {
                case 41 -> {
                    bonus = "1 Q";
                    yield "    F F A    ";
                }
                case 42 -> {
                    bonus = "1 N";
                    yield "    F F P    ";
                }
                case 43 -> {
                    bonus = "1 M";
                    yield "     F F I   ";
                }
                case 44 -> {
                    bonus = "2 C";
                    yield "   F F F A   ";
                }
                case 45 -> {
                    bonus = "2 C";
                    yield "   F F F P   ";
                }
                case 46 -> {
                    bonus = "2 C";
                    yield "   F F F I   ";
                }
                case 47, 48, 49 -> {
                    bonus = " 3 ";
                    yield "    F F F    ";
                }
                case 50 -> {
                    bonus = " 5 ";
                    yield "  F F F F F  ";
                }
                case 51 -> {
                    bonus = "1 Q";
                    yield "    P P I    ";
                }
                case 52 -> {
                    bonus = "1 M";
                    yield "    P P F    ";
                }
                case 53 -> {
                    bonus = "1 N";
                    yield "    P P A    ";
                }
                case 54 -> {
                    bonus = "2 C";
                    yield "   P P P I   ";
                }
                case 55 -> {
                    bonus = "2 C";
                    yield "   P P P A   ";
                }
                case 56 -> {
                    bonus = "2 C";
                    yield "   P P P F   ";
                }
                case 57, 58, 59 -> {
                    bonus = " 3 ";
                    yield "    P P P    ";
                }
                case 60 -> {
                    bonus = " 5 ";
                    yield "  P P P P P  ";
                }
                case 61 -> {
                    bonus = "1 N";
                    yield "    A A I    ";
                }
                case 62 -> {
                    bonus = "1 M";
                    yield "    A A P    ";
                }
                case 63 -> {
                    bonus = "1 Q";
                    yield "    A A F    ";
                }
                case 64 -> {
                    bonus = "2 C";
                    yield "   A A A I   ";
                }
                case 65 -> {
                    bonus = "2 C";
                    yield "   A A A F   ";
                }
                case 66 -> {
                    bonus = "2 C";
                    yield "   A A A P   ";
                }
                case 67, 68, 69 -> {
                    bonus = " 3 ";
                    yield "    A A A    ";
                }
                case 70 -> {
                    bonus = " 5 ";
                    yield "  A A A A A  ";
                }
                case 71 -> {
                    bonus = "1 Q";
                    yield "    I I P    ";
                }
                case 72 -> {
                    bonus = "1 M";
                    yield "    I I A    ";
                }
                case 73 -> {
                    bonus = "1 N";
                    yield "    I I F    ";
                }
                case 74 -> {
                    bonus = "2 C";
                    yield "   I I I A   ";
                }
                case 75 -> {
                    bonus = "2 C";
                    yield "   I I I P   ";
                }
                case 76 -> {
                    bonus = "2 C";
                    yield "   I I I F   ";
                }
                case 77, 78, 79 -> {
                    bonus = " 3 ";
                    yield "    I I I    ";
                }
                case 80 -> {
                    bonus = " 5 ";
                    yield "  I I I I I  ";
                }
                default -> {
                    bonus = "error";
                    yield "error";
                }
            };
        }

        if (utfActive)
            return  "┌────" + (getFront().getTL().getVisibility() ? "┬": "─") + "─────────────" + (getFront().getTR().getVisibility() ? "┬": "─") + "────┐" +                                                                                                                           "    ┌────┬─────────────┬────┐\n"+
                    "│" + (getFront().getTL().getVisibility() ? " " + resMap.get(getFront().getTL().getResource()) + " │" : "   ⠀ ") + "   ⠀" + bonus + "⠀⠀⠀⠀" + (getFront().getTR().getVisibility() ? "│ " + resMap.get(getFront().getTR().getResource()) + " " : "   ⠀ ") + "│"   +  "    │    │⠀⠀ ⠀⠀ ⠀⠀⠀⠀  │    │\n" +
                     (getFront().getTL().getVisibility() ? "├────┘" : "│     ") + "             " + (getFront().getTR().getVisibility() ? "└────┤" : "     │") +                                                                                                                        "    ├────┘             └────┤\n" +
                    "│                       │" +                                                                                                                                                                                                                                       "    │          " + (resMap.get(getBack().getMainResource()))+ "⠀⠀⠀⠀⠀⠀    │\n" +
                     (getFront().getBL().getVisibility() ? "├────┐" : "│     ") + "             " + (getFront().getBR().getVisibility() ? "┌────┤" : "     │") +                                                                                                                        "    ├────┐             ┌────┤\n" +
                    "│" + (getFront().getBL().getVisibility() ? " " + resMap.get(getFront().getBL().getResource()) + " │" : "   ⠀ ") + plc + (getFront().getBR().getVisibility() ? "│ " + resMap.get(getFront().getBR().getResource()) + " " : "   ⠀ ") + "│" +                         "    │    │⠀⠀⠀⠀⠀ ⠀⠀⠀ ⠀⠀│    │\n" +
                    "└────" + (getFront().getBL().getVisibility() ? "┴":"─") + "─────────────" + (getFront().getBR().getVisibility() ? "┴":"─") + "────┘" +                                                                                                                             "    └────┴─────────────┴────┘";
        else
            return  "┌────" + (getFront().getTL().getVisibility() ? "┬": "─") + "─────────────" + (getFront().getTR().getVisibility() ? "┬": "─") + "────┐" +                                                                                                                           "    ┌────┬─────────────┬────┐\n"+
                    "│" + (getFront().getTL().getVisibility() ? " " + resMap.get(getFront().getTL().getResource()) + " │" : "     ") + "     " + bonus + "     " + (getFront().getTR().getVisibility() ? "│ " + resMap.get(getFront().getTR().getResource()) + " " : "     ") + "│"  +  "    │    │             │    │\n" +
                     (getFront().getTL().getVisibility() ? "├────┘" : "│     ") + "             " + (getFront().getTR().getVisibility() ? "└────┤" : "     │") +                                                                                                                        "    ├────┘             └────┤\n" +
                    "│                       │" +                                                                                                                                                                                                                                       "    │           " + (resMap.get(getBack().getMainResource()))+ "          │\n" +
                    (getFront().getBL().getVisibility() ? "├────┐" : "│     ") + "             " + (getFront().getBR().getVisibility() ? "┌────┤" : "     │") +                                                                                                                         "    ├────┐             ┌────┤\n" +
                    "│" + (getFront().getBL().getVisibility() ? " " + resMap.get(getFront().getBL().getResource()) + " │" : "     ") + plc + (getFront().getBR().getVisibility() ? "│ " + resMap.get(getFront().getBR().getResource()) + " " : "     ") + "│" +                         "    │    │             │    │\n" +
                    "└────" + (getFront().getBL().getVisibility() ? "┴":"─") + "─────────────" + (getFront().getBR().getVisibility() ? "┴":"─") + "────┘" +                                                                                                                             "    └────┴─────────────┴────┘";
    }

}