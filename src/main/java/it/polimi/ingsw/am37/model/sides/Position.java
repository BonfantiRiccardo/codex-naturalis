package it.polimi.ingsw.am37.model.sides;

import java.io.Serializable;

/**
 * The Position class contains the coordinates of the side of a card placed in the kingdom.
 */
public class Position implements Serializable {
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

    /**
     * the equals method checks if the position equals the position given.
     * @param obj is the position given.
     * @return if they are equals or not.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != Position.class) {
            return false;
        } else {
            Position compare = (Position) obj;
            return this.x == compare.x && this.y == compare.y;
        }
    }

    /**
     * the toString method is used for debugging and prints the position of the card.
     * @return a string with the coordinates of the card.
     */
    @Override
    public String toString() {
        return "{x = " + x + ", y = " + y + '}';
    }
}
