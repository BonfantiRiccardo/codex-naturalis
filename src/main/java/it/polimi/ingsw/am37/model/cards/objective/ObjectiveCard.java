package it.polimi.ingsw.am37.model.cards.objective;

import it.polimi.ingsw.am37.model.cards.Card;
import it.polimi.ingsw.am37.model.player.Kingdom;

/**
 * The ObjectiveCard abstract class represent a generic objective card and is used as a blueprint for specific
 * objective cards. It is a subclass of the Card abstract class.
 */
public abstract class ObjectiveCard extends Card {
    /**
     * The pointsGivenPerCompletion attribute contains the number of points that will be added to a player if he
     * completes this particular objective once.
     */
    private final int pointsGivenPerCompletion;

    /**
     * The ObjectiveCard(id, points) constructor uses the constructor of the superclass to assign the id and assigns
     * the number of points given per completion to his respective attribute.
     * @param id An integer to uniquely identify the card.
     * @param points An integer that represents the points given per completion.
     */
    public ObjectiveCard(int id, int points) {
        super(id);
        this.pointsGivenPerCompletion = points;
    }

    /**
     * The getPointsGiven() method returns the value of the pointsGivenPerCompletion attribute.
     * @return The value of pointsGivenPerCompletion.
     */
    public int getPointsGiven() {
        return pointsGivenPerCompletion;
    }

    /**
     * The calculateNumOfCompletion(kingdom) method receives the kingdom of the player as a parameter and calculates
     * the number of times that the objective is satisfied, returning said number.
     * @param kingdom The kingdom attribute of the player, where the cards are placed.
     * @return The number of times the objective is satisfied by the player.
     */
    public abstract int calculateNumOfCompletion(Kingdom kingdom);

    /**
     * the toString() method is used to debug.
     * @return the points given for each completion of an objective.
     */
    @Override
    public String toString() {
        return super.toString() + ", points: " + pointsGivenPerCompletion;
    }

    /**
     * the toString() is used to show the objectives to the player.
     * @param utfActive is a boolean which tells if the encoding is enable.
     * @return the objectives.
     */
    public abstract String toString(boolean utfActive);
}
