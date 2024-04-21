package it.polimi.ingsw.am37.model.decks;

import it.polimi.ingsw.am37.model.cards.Card;
import it.polimi.ingsw.am37.model.cards.CardCreator;
import it.polimi.ingsw.am37.model.cards.placeable.GameCard;
import it.polimi.ingsw.am37.exceptions.NoCardsException;
import it.polimi.ingsw.am37.model.sides.Back;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Deck abstract class represent the idea of a generic list of cards.
 */
public abstract class Deck {
    /**
     * The list of cards that represents the deck; it has package visibility.
     */
    protected List<Card> cards;

    /**
     * The constructor of the abstract class Deck creates the list that will contain the cards.
     */
    public Deck () { cards = new ArrayList<>(); }

    /**
     * The abstract method initializeDeck(cc) uses the object CardCreator to create the list of cards by reading them
     * from a .json file specified in the subclass. It is an abstract method that will be implemented in the subclasses.
     * It has package visibility and will be called only once, at the end of the Constructor of the subclasses.
     */
    protected abstract void initializeDeck(CardCreator cc);

    /**
     * The shuffle() method will randomize the position of the cards in the list so that we can always pick the first
     * one of the pile when calling the method drawCard(). It has package visibility and will only be called once,
     * at the end of the initializeDeck() method.
     */
    protected void shuffle () {
        Collections.shuffle(cards);
    }

    /**
     * The drawCard() method checks if the deck is empty and if it is throws an exception. If it isn't empty
     * the method gets the first card of the list and saves it before removing it from the list, then it returns the
     * card saved.
     * @return c the card that is drawn from the top of the deck
     * @throws NoCardsException the exception prints the string in the console so that the user knows he will not be
     * able to draw again.
     */
    public abstract Card drawCard() throws NoCardsException;

    /**
     * The isEmpty() method is used by non-package classes to know if the list of cards in the deck is empty either
     * before or after accessing it.
     * @return true if the list is empty, false if the list is not empty.
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * The firstBack() method returns the Back of the first Card of the list. It is needed to tell the view what is the
     * colour of the Card that will be drawn next. If the deck contains Objective or Start Cards it returns null.
     * @return The Back of the first Card of the list.
     */
    public Back firstBack() {
        GameCard gc;
        if (this.getClass().equals(ObjectiveDeck.class) || this.getClass().equals(StartDeck.class)) {
            return null;
        } else {
            gc = (GameCard) cards.get(0);
            return gc.getBack();
        }
    }
}
