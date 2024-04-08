package it.polimi.ingsw.am37.model.game;

/**
 * The Resource enumeration contains all the possible resources that can be obtained by a player during the game. It
 * has been chosen to collapse the Gold Objects into the same enumeration as the regular resources since there is no
 * real difference between them and, in fact, they are treated the same way.
 */
public enum Resource {
    /**
     * The Empty resource represents the absence of a resource.
     */
    EMPTY,
    /**
     * The Plant resource also can represent the colour green of the card.
     */
    PLANT,
    /**
     * The Animal resource also can represent the colour blue of the card.
     */
    ANIMAL,
    /**
     * The Fungi resource also can represent the colour red of the card.
     */
    FUNGI,
    /**
     * The Insect resource also can represent the colour purple of the card.
     */
    INSECT,
    /**
     * The Manuscript object.
     */
    MANUSCRIPT,
    /**
     * The Inkwell object.
     */
    INKWELL,
    /**
     * The Quill object.
     */
    QUILL
}