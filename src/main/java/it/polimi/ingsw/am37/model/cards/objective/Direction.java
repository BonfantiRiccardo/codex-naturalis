package it.polimi.ingsw.am37.model.cards.objective;

import it.polimi.ingsw.am37.model.sides.Position;

/**
 * The Direction enumeration is used to indicate where the Corner of the Card is located.
 */
public enum Direction {
    /**
     * The TOPLEFT value indicates that we will consider the top left Corner of the Card.
     */
    TOPLEFT,
    /**
     * The TOPRIGHT value indicates that we will consider the top left Corner of the Card.
     */
    TOPRIGHT,
    /**
     * The BOTTOMLEFT value indicates that we will consider the top left Corner of the Card.
     */
    BOTTOMLEFT,
    /**
     * The BOTTOMRIGHT value indicates that we will consider the top left Corner of the Card.
     */
    BOTTOMRIGHT;

    /**
     * The opposite() method returns the opposite (both horizontally and vertically) Position relatively to the one
     * that calls the method.
     * @return The opposite position relatively to the one calling the method.
     */
    public Direction opposite() {
        return switch(this) {
            case TOPLEFT -> Direction.BOTTOMRIGHT;
            case TOPRIGHT -> Direction.BOTTOMLEFT;
            case BOTTOMLEFT -> Direction.TOPRIGHT;
            case BOTTOMRIGHT -> Direction.TOPLEFT;
        };
    }

    /**
     * The createPosition(p) method creates a new Position based on the direction that calls the method.
     * @param p The relative Position from which we are moving given a specific direction.
     * @return A new Position, relative to the Position of the Side, that is in the Direction given as parameter.
     */
    public Position createPosition(Position p) {
        return switch (this) {
            case Direction.TOPLEFT -> new Position(p.getX() - 1, p.getY() + 1);
            case Direction.TOPRIGHT -> new Position(p.getX() + 1, p.getY() + 1);
            case Direction.BOTTOMLEFT -> new Position(p.getX() - 1, p.getY() - 1);
            case Direction.BOTTOMRIGHT -> new Position(p.getX() + 1, p.getY() - 1);
        };
    }

}
