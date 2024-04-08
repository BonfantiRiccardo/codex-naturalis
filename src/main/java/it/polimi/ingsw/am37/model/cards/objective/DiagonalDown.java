package it.polimi.ingsw.am37.model.cards.objective;

import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Kingdom;
import it.polimi.ingsw.am37.model.sides.Side;

import java.util.List;

public class DiagonalDown extends PlacementBoundObjective {

    public DiagonalDown(int id, int points, Resource otherResource, Resource cardColorThatTriggersCheck) {
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
                while (s.getTL().getLinkedSide()!=null && s.getTL().getLinkedSide().getMainResource().equals(getCardColorThatTriggersCheck()) && !s.getTL().getLinkedSide().getUsedDiagonal()){
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
