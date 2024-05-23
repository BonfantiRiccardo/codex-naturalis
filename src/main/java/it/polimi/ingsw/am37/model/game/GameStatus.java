package it.polimi.ingsw.am37.model.game;

public enum GameStatus {
    /**
     * the wait_start_card_side Status is the phase of the game where the player gets his starting card.
     */
    WAIT_START_CARD_SIDE,
    /**
     * the wait_token Status is the phase of the game where the player gets his token.
     */
    WAIT_TOKEN,
    /**
     * the wait_objective Status is the phase of the game where the player gets his objectives and his hand.
     */
    WAIT_OBJECTIVE,
    /**
     * the wait_place Status is the phase of the game where the player places a card.
     */
    WAIT_PLACE,
    /**
     * the wait_Draw Status is the phase of the game where the player gets draws a card from the deck or from the available.
     */
    WAIT_DRAW,
    /**
     * the over Status is the phase of the game where the game is over and proceed to calculate the points.
     */
    OVER
}
