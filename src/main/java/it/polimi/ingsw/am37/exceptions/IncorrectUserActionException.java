package it.polimi.ingsw.am37.exceptions;

/**
 * the IncorrectUserActionException is a class that represents an Exception in case the controller can't execute a request
 * of the player.
 */
public class IncorrectUserActionException extends Exception{
    /**
     * IncorrectUserActionException is a setter method of the exception, which gives the message to send and calls the method
     * of the super class.
     * @param s is the message to be sent.
     */
    public IncorrectUserActionException(String s)
    {
        super(s);
    }
}
