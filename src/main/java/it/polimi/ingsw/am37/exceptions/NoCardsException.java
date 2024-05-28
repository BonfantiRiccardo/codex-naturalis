package it.polimi.ingsw.am37.exceptions;

/**
 * the NoCardsException is a class that represents an Exception in case there's no cards left in the deck.
 */
public class NoCardsException extends Exception{

    /**
     * NoCardsException is a setter method of the exception, which gives the message to send and calls the method
     * of the super class.
     * @param s is the message to be sent.
     */
        public NoCardsException(String s)
        {
            super(s);
        }
}