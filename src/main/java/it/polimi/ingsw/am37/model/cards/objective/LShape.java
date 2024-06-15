package it.polimi.ingsw.am37.model.cards.objective;

import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Kingdom;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.model.sides.Side;

import java.util.List;

/**
 * The LShape class represents the objective cards that require the cards to be placed to form an L in order to be
 * fulfilled. It needs two different colour of cards. It is a subclass of the PlacementBoundObjective abstract class.
 */
public class LShape extends PlacementBoundObjective {
    /**
     * The direction attribute contains the information on the desired direction of the second card that will be checked.
     */
    private final Direction direction;
    /**
     * The requestedPosition attribute contains the information on the desired position of the third card that
     * will be checked.
     */
    private final Position  requestedPosition;

    /**
     * The LShape(id, points, otherResource, cardColourThatTriggersCheck, direction, requestedPosition) constructor
     * uses the constructor of the superclass to assign the id, the points, the otherResource and the
     * cardColourThatTriggersCheck attributes, and then it assigns the direction and the requestedPosition to the ones
     * given as parameters.
     *  @param id An integer to uniquely identify the card.
     *  @param points An integer that represents the points given per completion.
     *  @param otherResource A Resource that is needed apart from the main resource that triggers the check.
     *  @param cardColourThatTriggersCheck A Resource that triggers the check for the requested pattern.
     * @param direction The requested Direction of the second card in order for the objective to be fulfilled.
     * @param requestedPosition The requested Position of the third card in order for the objective to be fulfilled.
     */
    public LShape(int id, int points, Resource otherResource, Resource cardColourThatTriggersCheck, Direction direction,
                  Position requestedPosition) {
        super(id, points, otherResource, cardColourThatTriggersCheck);
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
            if(s.getMainResource().equals(this.getCardColourThatTriggersCheck()) && !s.getUsedLCorner()){
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

    /**
     * The getDirection() method returns the requested Direction of the second card in order for the objective to be fulfilled.
     * @return The value of the direction attribute.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * The getRequestedPosition() method returns the requested Position of the third card in order for the objective to be fulfilled.
     * @return The value of the requestedPosition attribute.
     */
    public Position getRequestedPosition() {
        return requestedPosition;
    }
    /**
     * the toString method shows the placement of the cards the objective requires.
     * @param utfActive is a boolean which tells if the encoding is enable.
     * @return a String showing the placement the player has to achieve.
     */
    public String toString(boolean utfActive){
        int points = this.getPointsGiven();
        int id= this.getId();
        String res;
        String leg;

        if (utfActive) {
            if(this.cardColourThatTriggersCheck==Resource.INSECT){
                res="🦋";
            } else if (this.cardColourThatTriggersCheck==Resource.ANIMAL) {
                res="🐺";
            } else if (this.cardColourThatTriggersCheck==Resource.PLANT) {
                res="🍁";
            }   else{       //fungi
                res="🍄";
            }

            if(this.otherResource==Resource.INSECT){
                leg="🦋";
            } else if (this.otherResource==Resource.ANIMAL) {
                leg="🐺";
            } else if (this.otherResource==Resource.PLANT) {
                leg="🍁";
            }   else{       //fungi
                leg="🍄";
            }
        } else {
            if(this.cardColourThatTriggersCheck==Resource.INSECT){
                res="I ";
            } else if (this.cardColourThatTriggersCheck==Resource.ANIMAL) {
                res="A ";
            } else if (this.cardColourThatTriggersCheck==Resource.PLANT) {
                res="P ";
            }   else{       //fungi
                res="F ";
            }

            if(this.otherResource==Resource.INSECT){
                leg="I ";
            } else if (this.otherResource==Resource.ANIMAL) {
                leg="A ";
            } else if (this.otherResource==Resource.PLANT) {
                leg="P ";
            }   else{       //fungi
                leg="F ";
            }
        }


        if(this.direction==Direction.TOPLEFT){
            //return "—————————————————\n| id:"+id+"  pt:"+points+"    |\n|      "+leg+"       |\n|      "+leg+"       |\n|        "+res+"     |\n—————————————————";
            return  "┌───────────────┐\n" +
                    "│ id:" + id + "   pt:" + points + "  │\n" +
                    "│     "+leg+"        │\n" +
                    "│     "+leg+"        │\n" +
                    "│       "+res+"      │\n" +
                    "└───────────────┘";
        } else if (this.direction==Direction.TOPRIGHT) {
            //return "—————————————————\n| id:"+id+"  pt:"+points+"    |\n|        "+leg+"     |\n|        "+leg+"     |\n|     "+res+"        |\n—————————————————";
            return  "┌───────────────┐\n" +
                    "│ id:" + id + "   pt:" + points + "  │\n" +
                    "│       "+leg+"      │\n" +
                    "│       "+leg+"      │\n" +
                    "│     "+res+"        │\n" +
                    "└───────────────┘";
        } else if (this.direction==Direction.BOTTOMLEFT) {
            //return "—————————————————\n| id:"+id+"  pt:"+points+"    |\n|        "+res+"     |\n|     "+res+"        |\n|     "+res+"        |\n—————————————————";
            return  "┌───────────────┐\n" +
                    "│ id:" + id + "   pt:" + points + "  │\n" +
                    "│       "+res+"      │\n" +
                    "│     "+leg+"        │\n" +
                    "│     "+leg+"        │\n" +
                    "└───────────────┘";
        } else {
            //return "—————————————————\n| id:"+id+"  pt:"+points+"    |\n|      "+res+"       |\n|         "+res+"    |\n|         "+res+"    |\n—————————————————";
            return  "┌───────────────┐\n" +
                    "│ id:" + id + "   pt:" + points + "  │\n" +
                    "│     "+res+"        │\n" +
                    "│       "+leg+"      │\n" +
                    "│       "+leg+"      │\n" +
                    "└───────────────┘";
        }
    }
}
