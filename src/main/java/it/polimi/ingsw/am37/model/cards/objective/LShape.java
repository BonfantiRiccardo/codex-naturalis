package it.polimi.ingsw.am37.model.cards.objective;

import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Kingdom;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.model.sides.Side;

import java.util.List;

public class LShape extends PlacementBoundObjective {
    private final Direction direction;
    private final Position  requestedPosition;

    public LShape(int id, int points, Resource otherResource, Resource cardColorThatTriggersCheck, Direction direction,
                  Position requestedPosition) {
        super(id, points, otherResource, cardColorThatTriggersCheck);
        this.direction = direction;
        this.requestedPosition = requestedPosition;
    }

    /**
     * The calculateNumOfCompletion(kingdom) method receives the kingdom of the player as a parameter and calculates
     * the number of times that the objective is satisfied, returning said number.
     * @param kingdom The kingdom attribute of the player, where the cards are placed.
     * @return The number of times the objective is satisfied by the player.
     */
    @Override
    public int calculateNumOfCompletion(Kingdom kingdom) {
        int numSatisfied=0;
        List<Side> cards;

        cards = kingdom.getPlacedSides();

        for(Side s: cards){
            if(s.getMainResource().equals(getCardColorThatTriggersCheck()) && !s.getUsedLCorner()){
                if(s.getCorners().get(direction).getLinkedSide()!=null){
                    if(s.getCorners().get(direction).getLinkedSide().getMainResource().equals(getOtherResource()) && !s.getCorners().get(direction).getLinkedSide().getUsedLLeg()){
                        for(Side check: cards){
                            if(check.getMainResource().equals(getOtherResource()) && !check.getUsedLLeg() && check.getPositionInKingdom().getX()==s.getPositionInKingdom().getX()+requestedPosition.getX() && check.getPositionInKingdom().getY()==s.getPositionInKingdom().getY()+requestedPosition.getY()){
                                numSatisfied++;
                                check.setUsedLLeg(true);
                                s.getCorners().get(direction).getLinkedSide().setUsedLLeg(true);
                                s.setUsedLCorner(true);
                            }
                        }
                    }
                }
            }
        }
        return numSatisfied;
    }

    @Override
    public String toString() {
        return super.toString() + ", direction: " + direction + ", position: x: " + requestedPosition.getX() + " y: " + requestedPosition.getY();
    }
}
