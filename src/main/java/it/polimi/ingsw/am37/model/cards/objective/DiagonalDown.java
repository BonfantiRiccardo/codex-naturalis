package it.polimi.ingsw.am37.model.cards.objective;

import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Kingdom;
import it.polimi.ingsw.am37.model.sides.Side;

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
        List<Side> cards;

        cards = kingdom.getPlacedSides();

        for(Side s: cards){
            if(s.getMainResource().equals(this.getCardColourThatTriggersCheck()) && !s.getUsedDiagonal()){
                while (s.getTL().getLinkedSide()!=null && s.getTL().getLinkedSide().getMainResource().equals(this.getCardColourThatTriggersCheck()) && !s.getTL().getLinkedSide().getUsedDiagonal()){
                    s=s.getTL().getLinkedSide();
                }
                if(s.getBR().getLinkedSide()!=null){
                    if(s.getBR().getLinkedSide().getMainResource().equals(getOtherResource()) && !s.getBR().getLinkedSide().getUsedDiagonal()){
                        if(s.getBR().getLinkedSide().getBR().getLinkedSide()!=null){
                            if(s.getBR().getLinkedSide().getBR().getLinkedSide().getMainResource().equals(getOtherResource()) && !s.getBR().getLinkedSide().getBR().getLinkedSide().getUsedDiagonal()){
                                numSatisfied++;
                                s.setUsedDiagonal(true);
                                s.getBR().getLinkedSide().setUsedDiagonal(true);
                                s.getBR().getLinkedSide().getBR().getLinkedSide().setUsedDiagonal(true);
                            }
                        }
                    }
                }
            }
        }
        return numSatisfied;
    }

    public int calculateNumOfCompletionTest(List<Side> placedsides){
        int numSatisfied=0;

        for(Side s: placedsides){
            if(s.getMainResource().equals(this.getCardColourThatTriggersCheck()) && !s.getUsedDiagonal()){
                while (s.getTL().getLinkedSide()!=null && s.getTL().getLinkedSide().getMainResource().equals(this.getCardColourThatTriggersCheck()) && !s.getTL().getLinkedSide().getUsedDiagonal()){
                    s=s.getTL().getLinkedSide();
                }
                if(s.getBR().getLinkedSide()!=null){
                    if(s.getBR().getLinkedSide().getMainResource().equals(getOtherResource()) && !s.getBR().getLinkedSide().getUsedDiagonal()){
                        if(s.getBR().getLinkedSide().getBR().getLinkedSide()!=null){
                            if(s.getBR().getLinkedSide().getBR().getLinkedSide().getMainResource().equals(getOtherResource()) && !s.getBR().getLinkedSide().getBR().getLinkedSide().getUsedDiagonal()){
                                numSatisfied++;
                                s.setUsedDiagonal(true);
                                s.getBR().getLinkedSide().setUsedDiagonal(true);
                                s.getBR().getLinkedSide().getBR().getLinkedSide().setUsedDiagonal(true);
                            }
                        }
                    }
                }
            }
        }
        return numSatisfied;
    }

}
