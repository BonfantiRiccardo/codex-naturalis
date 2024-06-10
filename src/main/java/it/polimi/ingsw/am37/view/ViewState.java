package it.polimi.ingsw.am37.view;
/**
 * Enumerates the possible states of the view.
 */
public enum ViewState {
    /**
     * The view is in the initial state.
     */
    CREATE_JOIN,
    /**
     * The view is in the state of waiting for a player to choose a lobby.
     */
    CHOOSE_LOBBY,
    /**
     * The view is in this state when an error occurs.
     */
    ERROR,
    /**
     * The view is in the state of waiting for the other players to join the lobby.
     */
    WAIT_IN_LOBBY,
    /**
     * The view is in the state of waiting for the player to place a starting card.
     */
    PLACE_SC,
    /**
     * The view is in the state of waiting for the player to choose a token.
     */
    CHOOSE_TOKEN,
    /**
     * The view is in the state of waiting for the player to choose an objective.
     */
    CHOOSE_OBJECTIVE,
    /**
     * The view is in the state of waiting for the other players to do their turn.
     */
    NOT_TURN,
    /**
     * The view is in the state of waiting for the player to place a card.
     */
    PLACE,
    /**
     * The view is in the state of waiting for the player to draw a card.
     */
    DRAW,
    /**
     * The view is in the state of showing the results of the game.
     */
    SHOW_RESULTS,
    /**
     * The view is in this state when the player disconnects.
     */
    DISCONNECTION
}
