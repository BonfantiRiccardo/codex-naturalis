package it.polimi.ingsw.am37.model.sides;

/**
 * The Position class contains the coordinates of the side of a card placed in the kingdom.
 */
public class Position {
    /**
     * The x attribute contains an integer value that represents the horizontal coordinate of the card.
     */
    private final int x;
    /**
     * The y attribute contains an integer value that represents the vertical coordinate of the card.
     */
    private final int y;

    /**
     * The Position(x, y) constructor assigns the values given as parameters to the x and y attributes.
     * @param x An integer that represents the horizontal coordinate of the card.
     * @param y An integer that represents the vertical coordinate of the card.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * The getX() method returns the value of the x (horizontal) coordinate.
     * @return An integer that represents the horizontal coordinate of the card.
     */
    public int getX() {
        return x;
    }

    /**
     * The getY() method returns the value of the y (vertical) coordinate.
     * @return An integer that represents the vertical coordinate of the card.
     */
    public int getY() {
        return y;
    }
}
