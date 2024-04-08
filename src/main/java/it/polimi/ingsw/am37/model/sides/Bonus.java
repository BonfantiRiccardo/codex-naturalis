package it.polimi.ingsw.am37.model.sides;

/**
 * The Bonus enumeration gives information on the way the points are assigned after placing a Front of a GoldCard.
 */
public enum Bonus {
    /**
     * The Quill Bonus assigns 1 point for every quill object/resource currently present on the player's Kingdom.
     */
    QUILL,
    /**
     * The Manuscript Bonus assigns 1 point for every manuscript object/resource present on the player's Kingdom.
     */
    MANUSCRIPT,
    /**
     * The Corner Bonus assigns 2 points for every Corner covered by this Card placement.
     */
    CORNER,
    /**
     * The Inkwell Bonus assigns a point for every inkwell object/resource currently present on the player's Kingdom.
     */
    INKWELL
}
