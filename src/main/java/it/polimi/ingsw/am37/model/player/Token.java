package it.polimi.ingsw.am37.model.player;

/**
 * The Token enumeration contains all the possible colours a player can choose. It also contains the black token that is
 * assigned to the player who starts with the first turn at the beginning the playing phase.
 */
public enum Token {
    /**
     * The black token is assigned at the start of the playing phase to the first player who takes a turn.
     */
    BLACK,
    /**
     * The red token is assigned to the player that chooses it during the lobby phase.
     */
    RED,
    /**
     * The blue token is assigned to the player that chooses it during the lobby phase.
     */
    BLUE,
    /**
     * The yellow token is assigned to the player that chooses it during the lobby phase.
     */
    YELLOW,
    /**
     * The green token is assigned to the player that chooses it during the lobby phase.
     */
    GREEN
}