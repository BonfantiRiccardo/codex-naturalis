package it.polimi.ingsw.am37.model.decks;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.am37.model.cards.CardCreator;
import it.polimi.ingsw.am37.model.cards.objective.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * The ObjectiveDeck class is a subclass of Deck that creates a list of ObjectiveCards.
 */
public class ObjectiveDeck extends Deck {
    /**
     * The fileNamePool attribute is used by the createCards(fileName, cardListType) method to know from which
     * file to read.
     */
    private final String[] fileNamePool;

    /**
     * The ObjectiveDeck(cc) constructor calls the constructor of the superclass to instantiate the ArrayList of cards,
     * then it creates the fileNamePool array, that contain all the files in the /resources/cards/ folder that are
     * needed to create the deck and finally, it calls the initializeDeck(cc) method to create the list of
     * ObjectiveCards.
     * @param cc A reference to the CardCreator object.
     */
    public ObjectiveDeck (CardCreator cc) {
        super();
        fileNamePool = new String[] {"/it/polimi/ingsw/am37/cards/DiagUpPlacement.json",
                "/it/polimi/ingsw/am37/cards/DiagDownPlacement.json", "/it/polimi/ingsw/am37/cards/LShapePlacement.json",
                "/it/polimi/ingsw/am37/cards/ResourcesBoundObjectives.json"};
        initializeDeck(cc);
    }

    /**
     * The initializeDeck(cc) method implements the interface in the superclass. It has package visibility and will
     * only be called once, at the end of the Constructor. The method adds all the cards created by the
     * createCards(fileName, cardListType) method to the ArrayList for all the files in the pool. Finally, it calls the
     * shuffle() method to randomize the positions.
     * @param cc A reference to the CardCreator object.
     */
    @Override
    protected void initializeDeck(CardCreator cc) {
        Type cardListType = new TypeToken<ArrayList<DiagonalUp>>() {}.getType();
        cards.addAll(cc.createCards(fileNamePool[0], cardListType));

        cardListType = new TypeToken<ArrayList<DiagonalDown>>() {}.getType();
        cards.addAll(cc.createCards(fileNamePool[1], cardListType));

        cardListType = new TypeToken<ArrayList<LShape>>() {}.getType();
        cards.addAll(cc.createCards(fileNamePool[2], cardListType));

        cardListType = new TypeToken<ArrayList<ResourcesBoundObjective>>() {}.getType();
        cards.addAll(cc.createCards(fileNamePool[3], cardListType));

        shuffle();
    }
}