package it.polimi.ingsw.am37.model.decks;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.am37.model.cards.CardCreator;
import it.polimi.ingsw.am37.model.cards.placeable.ResourceCard;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.exceptions.NoCardsException;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * The ResourceDeck class is a subclass of Deck that creates a list of ResourceCards.
 */
public class ResourceDeck extends Deck {
    /**
     * The fileName attribute is used by the createCards(fileName) method to know from which file to read.
     */
    private final String fileName;

    /**
     * The ResourceDeck(cc) constructor calls the constructor of the superclass to instantiate the ArrayList of cards,
     * then it assigns the fileName attribute to the ResourceCards.json file in the /resources/cards/ folder and
     * finally, it calls the initializeDeck(cc) method to create the list of ResourceCards.
     * @param cc A reference to the CardCreator object.
     */
    public ResourceDeck(CardCreator cc) {
        super();
        fileName = "/it/polimi/ingsw/am37/cards/ResourceCards.json";
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
        Type cardListType = new TypeToken<ArrayList<ResourceCard>>() {}.getType();
        cards.addAll(cc.createCards(fileName, cardListType));

        shuffle();
    }

    public StandardCard drawCard() throws NoCardsException {
        if (!cards.isEmpty())  {
            StandardCard c = (StandardCard) cards.get(0);
            cards.remove(0);

            return c;
        } else throw new NoCardsException("No cards remaining in the deck");
    }
}
