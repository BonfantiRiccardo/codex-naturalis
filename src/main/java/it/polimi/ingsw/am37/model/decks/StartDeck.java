package it.polimi.ingsw.am37.model.decks;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.am37.model.cards.CardCreator;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * The StartDeck class is a subclass of Deck that creates a list of StartCards.
 */
public class StartDeck extends Deck {
    /**
     * The fileName attribute is used by the createCards(fileName) method to know from which file to read.
     */
    private final String fileName;

    /**
     * The StartDeck(cc) constructor calls the constructor of the superclass to instantiate the ArrayList of cards,
     * then it assigns the fileName attribute to the StartCards.json file in the /resources/cards/ folder and
     * finally, it calls the initializeDeck(cc) method to create the list of ResourceCards.
     * @param cc A reference to the CardCreator object.
     */
    public StartDeck(CardCreator cc) {
        super();
        fileName = "/it/polimi/ingsw/am37/cards/StartCards.json";
        initializeDeck(cc);
    }

    /**
     * The initializeDeck(cc) method implements the interface in the superclass. It has package visibility and will
     * only be called once, at the end of the Constructor. The method adds all the cards created by the
     * createCards(fileName, cardListType) method to the ArrayList, and then it calls the shuffle() method to randomize
     * the positions.
     * @param cc A reference to the CardCreator object.
     */
    @Override
    protected void initializeDeck(CardCreator cc) {
        Type cardListType = new TypeToken<ArrayList<StartCard>>() {}.getType();
        cards.addAll(cc.createCards(fileName, cardListType));

        shuffle();
    }
}
