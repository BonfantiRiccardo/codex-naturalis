package it.polimi.ingsw.am37.model.cards.objective;

import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Kingdom;
import it.polimi.ingsw.am37.model.sides.Side;

import java.util.List;

public class DiagonalUp extends PlacementBoundObjective {
    public DiagonalUp(int id, int points, Resource otherResource, Resource cardColorThatTriggersCheck) {
        super(id, points, otherResource, cardColorThatTriggersCheck);
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
            if(s.getMainResource().equals(getCardColorThatTriggersCheck()) && !s.getUsedDiagonal()){
                while (s.getTR().getLinkedSide()!=null && s.getTR().getLinkedSide().getMainResource().equals(getCardColorThatTriggersCheck()) && !s.getTR().getLinkedSide().getUsedDiagonal()){
                    s=s.getTR().getLinkedSide();
                }
                if(s.getBL().getLinkedSide()!=null){
                    if(s.getBL().getLinkedSide().getMainResource().equals(getOtherResource()) && !s.getBL().getLinkedSide().getUsedDiagonal()){
                        if(s.getBL().getLinkedSide().getBL().getLinkedSide()!=null){
                            if(s.getBL().getLinkedSide().getBL().getLinkedSide().getMainResource().equals(getOtherResource()) && !s.getBL().getLinkedSide().getBL().getLinkedSide().getUsedDiagonal()){
                                numSatisfied++;
                                s.setUsedDiagonal(true);
                                s.getBL().getLinkedSide().setUsedDiagonal(true);
                                s.getBL().getLinkedSide().getBL().getLinkedSide().setUsedDiagonal(true);
                            }
                        }
                    }
                }
            }
        }
        return numSatisfied;

    }
}
