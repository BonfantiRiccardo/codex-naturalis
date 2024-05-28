package it.polimi.ingsw.am37.exceptions;

/**
 * the alreadyAssignedException is a class that represents an Exception in case an object has already been assigned.
 */
public class AlreadyAssignedException extends Exception {
    /**
     * AlreadyAssignedException is a setter method of the exception, which gives the message to send and calls the method
     * of the super class.
     * @param s is the message to be sent.
     */
    public AlreadyAssignedException(String s)
    {
        super(s);
    }
}
