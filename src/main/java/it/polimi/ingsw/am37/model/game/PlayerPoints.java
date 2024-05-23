package it.polimi.ingsw.am37.model.game;

import it.polimi.ingsw.am37.model.player.Player;

/**
 * the PlayerPoints class contains the information about each player's points.
 */
public class PlayerPoints {
    /**
     * the player attribute is the player's of whose points are attributed.
     */
    Player player;
    /**
     * the points attribute is the points each player got during the game.
     */
    Integer points;
    /**
     * the completions attribute is the number of objectives each player completed.
     */
    Integer completions;

    /**
     * the PlayerPoints method sets each player's points and the number of objectives he completed.
     * @param player is the player's of whose points are attributed.
     * @param points is the points the player got during the game.
     * @param completions is the number of objectives the player completed.
     */
    public PlayerPoints(Player player,Integer points, Integer completions){
        this.player = player;
        this.points = points;
        this.completions = completions;
    }

    /**
     * the Player method returns the player.
     * @return the player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * the getPoints method returns the player's point
     * @return the player's point.
     */
    public Integer getPoints() {
        return points;
    }

    /**
     * the getNumOfCompletion method returns the number of objectives the player completed.
     * @return the number of objectives the player completed.
     */
    public Integer getNumOfCompletion() {
        return completions;
    }
}
