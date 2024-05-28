package it.polimi.ingsw.am37.exceptions;

/**
 * the WrongGamePhaseException is a class that represents an Exception in case a player sends a message in the wrong phase
 * of the game.
 */
public class WrongGamePhaseException extends Exception {

    /**
     * WrongGamePhaseException is a setter method of the exception, which gives the message to send and calls the method
     * of the super class.
     * @param s is the message to be sent.
     */
    public WrongGamePhaseException(String s)
    {
        super(s);
    }
}
