package it.polimi.ingsw.am37.model.game;

/**
 * The GamePhase enumeration contains all the possible Game phases the GameModel class can be found in. The lobby phase
 * is absent because it is managed by the GameController class before creating the GameModel class.
 */
public enum GamePhase {
    /**
     * During the preparation phase the GameModel manages the creation of all the cards and the decks. Then it sets
     * the available cards, it gives everyone a StartCard, it initializes the players' hand by giving them 3 cards
     * (2 ResourceCard and 1 GoldCard), it sets common objectives, and finally it gives all the player 2 ObjectiveCard
     * and lets them choose only 1 of those 2.
     */
    PREPARATION,
    /**
     * During the playing phase the GameModel manages the normal flow of the game. When a player calls it for a
     * specific action it checks if it is his turn and if it isn't it denies the action. If it is then the action
     * can be done.
     */
    PLAYING,
    /**
     * During the endgame phase the GameModel manages the calculations for the last turn and for the final points of
     * each player.
     */
    ENDGAME,
    /**
     * After the endgame phase we can either destroy everything that was created or save it in the last phase before
     * closing. This way we will have a game history of all the games played on this server.
     */
    FINISHED
}
