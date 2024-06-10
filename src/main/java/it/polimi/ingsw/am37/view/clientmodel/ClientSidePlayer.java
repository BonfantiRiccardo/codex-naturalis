package it.polimi.ingsw.am37.view.clientmodel;

import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Kingdom;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Bonus;
import it.polimi.ingsw.am37.model.sides.Front;
/**
 * This class is a copy of the Player class, but it is used only for the client side.
 * It contains only the information that the client needs to know about the player.
 */
public class ClientSidePlayer {

    /**
     * The nickname of the player.
     */
    private final String nickname;
    /**
     * The token of the player.
     */
    private Token token;
    /**
     * The kingdom of the player.
     */
    private Kingdom kingdom;
    /**
     * A boolean that is true if the player has the black token.
     */
    private boolean hasBlackToken;
    /**
     * The points of the player.
     */
    private int points;
    /**
     * The number of objectives completed by the player.
     */
    private int objectivesCompleted;

    /**
     * Constructor of the class.
     * @param nickname The nickname of the player.
     */
    public ClientSidePlayer(String nickname) {
        this.nickname = nickname;
        hasBlackToken = false;
        points = 0;
        objectivesCompleted = 0;
    }

    /**
     * Getter of the nickname.
     * @return The nickname of the player.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Getter of the token.
     * @return The token of the player.
     */
    public Token getToken() {
        return token;
    }

    /**
     * Setter of the token.
     * @param token The token of the player.
     */
    public void setToken(Token token) {
        this.token = token;
    }

    /**
     * Getter of the kingdom.
     * @return The kingdom of the player.
     */
    public Kingdom getKingdom() {
        return kingdom;
    }

    /**
     * Setter of the kingdom.
     * @param kingdom The kingdom of the player.
     */
    public void setKingdom(Kingdom kingdom) {
        this.kingdom = kingdom;
    }

    /**
     * Getter of the hasBlackToken boolean.
     * @return True if the player has the black token, false otherwise.
     */
    public boolean hasBlackToken() {
        return hasBlackToken;
    }

    /**
     * Setter of the hasBlackToken boolean.
     * @param hasBlackToken True if the player has the black token, false otherwise.
     */
    public void setHasBlackToken(boolean hasBlackToken) {
        this.hasBlackToken = hasBlackToken;
    }

    /**
     * Getter of the points.
     * @return The points of the player.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Method that adds points to the player.
     * The number of points added depends on the front that has been placed.
     * The points are added to the total points of the player.
     * @param placed The front that has been placed.
     * @param cornersLinked The number of corners linked by the front.
     */
    public void addPoints(Front placed, int cornersLinked) {
        int toAdd = 0;

        if (placed.getBonus() == null)
            toAdd = placed.getPointsGivenOnPlacement();
        else if (placed.getBonus().equals(Bonus.CORNER))
            toAdd = placed.getPointsGivenOnPlacement() * cornersLinked;
        else {
            if (placed.getBonus().equals(Bonus.INKWELL)) {
                toAdd = (kingdom.getOnFieldResources().get(Resource.INKWELL) + 1) * placed.getPointsGivenOnPlacement();
            } else if (placed.getBonus().equals(Bonus.QUILL)) {
                toAdd = (kingdom.getOnFieldResources().get(Resource.QUILL) + 1) * placed.getPointsGivenOnPlacement();
            } else if (placed.getBonus().equals(Bonus.MANUSCRIPT))
                toAdd = (kingdom.getOnFieldResources().get(Resource.MANUSCRIPT) + 1) * placed.getPointsGivenOnPlacement();
        }

        points = points + toAdd;
    }

    /**
     * Setter of the final points.
     * @param points The points of the player.
     */
    public void setFinalPoints(int points) {
        this.points = points;
    }

    /**
     * Getter of the objectivesCompleted.
     * @return The number of objectives completed by the player.
     */
    public int getObjectivesCompleted() {
        return objectivesCompleted;
    }

    /**
     * Setter of the objectivesCompleted.
     * @param objectivesCompleted The number of objectives completed by the player.
     */
    public void setObjectivesCompleted(int objectivesCompleted) {
        this.objectivesCompleted = objectivesCompleted;
    }
}
