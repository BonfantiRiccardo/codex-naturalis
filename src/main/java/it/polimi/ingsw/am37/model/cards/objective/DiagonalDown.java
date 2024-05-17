package it.polimi.ingsw.am37.model.cards.objective;

import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Kingdom;
import it.polimi.ingsw.am37.model.sides.Side;

import java.util.ArrayList;
import java.util.List;

/**
 * The DiagonalDown class represents the objective cards that require a descending diagonal (from left to right) of
 * cards of the same colour in order to be fulfilled. It is a subclass of the PlacementBoundObjective abstract class.
 */
public class DiagonalDown extends PlacementBoundObjective {
    /**
     * The DiagonalDown(id, points, otherResource, cardColourThatTriggersCheck) constructor uses the constructor of the
     * superclass to assign the id, the points, the otherResource and the cardColourThatTriggersCheck attributes to
     * the ones given as parameters.
     *  @param id An integer to uniquely identify the card.
     *  @param points An integer that represents the points given per completion.
     *  @param otherResource A Resource that is needed apart from the main resource that triggers the check.
     *  @param cardColourThatTriggersCheck A Resource that triggers the check for the requested pattern.
     */
    public DiagonalDown(int id, int points, Resource otherResource, Resource cardColourThatTriggersCheck) {
        super(id, points, otherResource, cardColourThatTriggersCheck);
    }

    /**
     * The calculateNumOfCompletion(kingdom) method receives the kingdom of the player as a parameter and calculates
     * the number of times that the objective is satisfied, returning said number.
     * @param kingdom The kingdom attribute of the player, where the cards are placed.
     * @return The number of times the objective is satisfied by the player.
     */
    public int calculateNumOfCompletion(Kingdom kingdom){
        int numSatisfied=0;
        List<Side> cards = new ArrayList<>();

        for (Side s: kingdom.getPlacedSides())
            if (s.getMainResource().equals(cardColourThatTriggersCheck))
                cards.add(s);

        for (Side s: cards) {
            if (!s.getUsedDiagonal()) {
                boolean foundTop = false;
                Side previous;
                while (!foundTop) {
                    previous = s;
                    for (Side goToTop: cards) {
                        if (!goToTop.getUsedDiagonal() && Direction.TOPLEFT.createPosition(s.getPositionInKingdom()).equals(goToTop.getPositionInKingdom())) {
                            s = goToTop; break;
                        } else if (goToTop.getUsedDiagonal() && Direction.TOPLEFT.createPosition(s.getPositionInKingdom()).equals(goToTop.getPositionInKingdom())) {
                            foundTop = true;
                            break;
                        }
                    }
                    if (s.equals(previous))
                        foundTop = true;
                }

                for (Side s2: cards) {
                    if (!s2.getUsedDiagonal() && Direction.BOTTOMRIGHT.createPosition(s.getPositionInKingdom()).equals(s2.getPositionInKingdom())) {
                        for (Side s3: cards){
                            if(!s3.getUsedDiagonal() && Direction.BOTTOMRIGHT.createPosition(s2.getPositionInKingdom()).equals(s3.getPositionInKingdom())){
                                numSatisfied++;
                                s.setUsedDiagonal(true);
                                s2.setUsedDiagonal(true);
                                s3.setUsedDiagonal(true);
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }
        return numSatisfied;
    }

    public String toString(){
        int points = this.getPointsGiven();
        int id= this.getId();
        String res;
        if(this.cardColourThatTriggersCheck==Resource.INSECT){
            res="🦋";
        } else if (this.cardColourThatTriggersCheck==Resource.ANIMAL) {
            res="🐺";
        } else if (this.cardColourThatTriggersCheck==Resource.PLANT) {
            res="🍁";
        }   else{       //fungi
            res="🍄";
        }

        //return "—————————————————\n| id:"+id+"  pt:"+points+"   |\n|    "+res+"         |\n|        "+res+"     |\n|            "+res+" |\n—————————————————";
        return  "┌───────────────┐\n" +
                "│ id:" + id + "   pt:" + points + "  │\n" +
                "│    "+res+"⠀⠀⠀⠀⠀⠀  │\n" +
                "│      ⠀"+res+"⠀⠀⠀⠀⠀│\n" +
                "│    ⠀⠀⠀⠀⠀"+res+"⠀  │\n" +
                "└───────────────┘";

    }

}
