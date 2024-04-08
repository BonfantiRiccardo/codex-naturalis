package it.polimi.ingsw.am37.model.game;

import it.polimi.ingsw.am37.model.player.*;

import java.util.Hashtable;
import java.util.List;

/**
 * The Scoreboard class contains a table of players with their corresponding points and methods to interact with it.
 */
public class Scoreboard {
    /**
     * The participantsPoints attribute is a table that contains for every player their respective points.
     */
    private final Hashtable<Token, Integer> participantsPoints;

    /**
     * The Scoreboard(participants) constructor creates a new Hashtable and adds all the participants tokens' (taken
     * from the list given as a parameter) to the table while assigning them 0 points.
     * @param participants A list of all the participants to the game.
     */
    public Scoreboard(List<Player> participants) {
        participantsPoints = new Hashtable<>();

        for (Player p : participants) {
            participantsPoints.put(p.getToken(), 0);            //cicla sui partecipanti e crea la tabella dei punti
        }
    }

    /**
     * The getParticipantsPoints() method returns the Hashtable containing the points of each player.
     * @return The Hashtable containing the participants tokens and their respective points.
     */
    public Hashtable<Token, Integer> getParticipantsPoints() {
        return participantsPoints;
    }

    /**
     * The addPoints(t, points) method receives the token of a player and a number of points as parameters and adds
     * the points to the value already stored in the table.
     * @param t The Token of a player.
     * @param points The number of points that needs to be added.
     */
    public void addPoints(Token t, int points) {
        int newPoints = participantsPoints.get(t) + points;

        participantsPoints.replace(t, newPoints);
    }
}
