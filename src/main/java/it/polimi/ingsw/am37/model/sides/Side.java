package it.polimi.ingsw.am37.model.sides;

import it.polimi.ingsw.am37.model.cards.objective.Direction;
import it.polimi.ingsw.am37.model.game.Resource;

import java.util.HashMap;

/**
 * The Side abstract class represent the idea of a generic side of a card. It serves as a blueprint for more specific
 * sides.
 */
public abstract class Side {
    /**
     * The corners attribute contains all the 4 Corners of the Card mapped to the direction they lead to.
     */
    private final HashMap<Direction, Corner> corners;
    /**
     * The mainResource attribute represents the colour of the card (for the StartCards its initiated as empty).
     */
    private final Resource mainResource;
    /**
     * The usedDiagonal attribute is used to mark the side of the card if it has already been used in a diagonal
     * placement bound objective.
     */
    private boolean usedDiagonal;
    /**
     * The usedLCorner attribute is used to mark the side of the card if it has already been used in a LCorner
     * placement bound objective.
     */
    private boolean usedLCorner;
    /**
     * The usedLLeg attribute is used to mark the side of the card if it has already been used in a LLeg placement
     * bound objective.
     */
    private boolean usedLLeg;
    /**
     * The isPlaced attribute is used to mark the side of the card if it has already been placed in the Kingdom.
     */
    private boolean isPlaced;
    /**
     * The positionInKingdom attribute represents the coordinates where the side of the card is placed in the Kingdom.
     */
    private Position positionInKingdom;

    /**
     * The Side(tl, tr, bl, br) constructor assigns the corner to the ones given as parameters. It also initializes the
     * usedDiagonal, usedLCorner, usedLLeg and isPlaced attributes to false.
     * @param tl A Corner object representing the top left corner of the side.
     * @param tr A Corner object representing the top right corner of the side.
     * @param bl A Corner object representing the bottom left corner of the side.
     * @param br A Corner object representing the bottom right corner of the side.
     * @param mainResource A Resource object that represents the colour of the Card.
     */
    public Side(Corner tl, Corner tr, Corner bl, Corner br, Resource mainResource){
        corners = new HashMap<>();
        corners.put(Direction.TOPLEFT, tl);
        corners.put(Direction.TOPRIGHT, tr);
        corners.put(Direction.BOTTOMLEFT, bl);
        corners.put(Direction.BOTTOMRIGHT, br);
        this.usedDiagonal = false;
        this.usedLCorner = false;
        this.usedLLeg = false;
        this.isPlaced = false;
        this.mainResource = mainResource;
    }

    /**
     * The getCorners() method returns a table that maps the Direction to the Corner.
     * @return The corners attribute.
     */
    public HashMap<Direction, Corner> getCorners() {
        return corners;
    }

    /**
     * The getTL() method returns the top left corner object.
     * @return The TL attribute.
     */
    public Corner getTL() {
        return corners.get(Direction.TOPLEFT);
    }

    /**
     * The getTR() method returns the top right corner object.
     * @return The TR attribute.
     */
    public Corner getTR() {
        return corners.get(Direction.TOPRIGHT);
    }

    /**
     * The getBL() method returns the bottom left corner object.
     * @return The BL attribute.
     */
    public Corner getBL() {
        return corners.get(Direction.BOTTOMLEFT);
    }

    /**
     * The getBR() method returns the bottom right corner object.
     * @return The BR attribute.
     */
    public Corner getBR() {
        return corners.get(Direction.BOTTOMRIGHT);
    }

    /**
     * The getMainResource() method returns the value of the mainResource attribute.
     * @return The main resource of the card.
     */
    public Resource getMainResource(){
        return mainResource;
    }

    /**
     * The getUsedDiagonal() method returns the value of the usedDiagonal attribute.
     * @return A boolean that represents the value of the usedDiagonal attribute.
     */
    public boolean getUsedDiagonal() {
        return usedDiagonal;
    }

    /**
     * The setUsedDiagonal(usedDiagonal) method sets the usedDiagonal attribute to the value given as a parameter.
     * @param usedDiagonal A boolean that represents the new value of the usedDiagonal attribute.
     */
    public void setUsedDiagonal(boolean usedDiagonal) {
        this.usedDiagonal = usedDiagonal;
    }

    /**
     * The getUsedLCorner() method returns the value of the usedLCorner attribute.
     * @return A boolean that represents the value of the usedLCorner attribute.
     */
    public boolean getUsedLCorner() {
        return usedLCorner;
    }

    /**
     * The setUsedLCorner(usedLCorner) method sets the usedLCorner attribute to the value given as a parameter.
     * @param usedLCorner A boolean that represents the new value of the usedLCorner attribute.
     */
    public void setUsedLCorner(boolean usedLCorner) {
        this.usedLCorner = usedLCorner;
    }

    /**
     * The getUsedLLeg() method returns the value of the usedLLeg attribute.
     * @return A boolean that represents the value of the usedLLeg attribute.
     */
    public boolean getUsedLLeg() {
        return usedLLeg;
    }

    /**
     * The setUsedLLeg(usedLLeg) method sets the usedLLeg attribute to the value given as a parameter.
     * @param usedLLeg A boolean that represents the new value of the usedLLeg attribute.
     */
    public void setUsedLLeg(boolean usedLLeg) {
        this.usedLLeg = usedLLeg;
    }

    /**
     * The getIsPlaced() method returns the value of the isPlaced attribute.
     * @return A boolean that represents the value of the isPlaced attribute.
     */
    public boolean getIsPlaced() {
        return isPlaced;
    }

    /**
     * The getPositionInKingdom() method returns a Position object that represents the coordinates where the side
     * is placed in the kingdom.
     * @return The positionInKingdom attribute.
     */
    public Position getPositionInKingdom() {
        return positionInKingdom;
    }

    /**
     * The placeInPosition(x, y) method creates a new Position object with the coordinates given as parameters and
     * sets the isPlaced attribute to true.
     * @param x An integer that represents the x (horizontal) coordinate of the card in the Kingdom.
     * @param y An integer that represents the y (vertical) coordinate of the card in the Kingdom.
     */
    public void placeInPosition(int x, int y) {
        this.positionInKingdom = new Position(x, y);
        isPlaced = true;
    }

    /**
     * the toString method is used for debugging and prints a side of the card and where it's placed.
     * @return a string representing the side of the card.
     */
    @Override
    public String toString() {
        return "{" +
                "TL=" + corners.get(Direction.TOPLEFT) +
                ", TR=" + corners.get(Direction.TOPRIGHT) +
                ", BL=" + corners.get(Direction.BOTTOMLEFT) +
                ", BR=" + corners.get(Direction.BOTTOMRIGHT) +
                (mainResource!=null ? (", mainRes=" + mainResource) : "") +
                (isPlaced ? (", positionInKingdom=" + positionInKingdom) : "")  +
                '}';
    }
}
