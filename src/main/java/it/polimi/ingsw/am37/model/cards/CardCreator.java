package it.polimi.ingsw.am37.model.cards;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

/**
 * The CardCreator class creates the list of Cards using a .json parser from the Gson library.
 */
public class CardCreator {
    /**
     * The gson attribute that will be used to parse the .json file.
     */
    private final Gson gson;

    /**
     * The constructor CardCreator() creates the Gson object that will be used to parse the .json file.
     */
    public CardCreator() {
        gson = new Gson();
    }

    /**
     * The createCards(fileName) method is called by the initializeDeck(cc) method and creates the cards by reading
     * the .json file given as a parameter. If something goes wrong while the FileReader object tries to read the file
     * the method will throw an IOException(). Since we want to save the object in a list we need to use the Type class
     * from the Java Reflection library.
     * @param fileName The name or directory path of the file that will be read.
     * @param cardListType The Type of card that the gson parser needs to return.
     * @return The list of cards that is part of the Deck class.
     */
    public List<Card> createCards (String fileName, Type cardListType) {

        try (InputStream inputStream = getClass().getResourceAsStream(fileName)) {
            InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(inputStream));

            return gson.fromJson(reader, cardListType);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
