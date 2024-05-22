package it.polimi.ingsw.am37.model.cards;

import it.polimi.ingsw.am37.model.game.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * The Card abstract class represents the idea of a generic card of the game and serves as a blueprint for more
 * specific cards.
 */
public abstract class Card {
    /**
     * The id attribute uniquely identifies a card.
     */
    private final int id;

    /**
     * The Card(id) constructor sets the value of the id attribute.
     * @param id An integer to uniquely identify the card.
     */
    public Card(int id){
        this.id = id;
    }

    /**
     * The getId() method returns the value of the id attribute.
     * @return The value of id.
     */
    public int getId(){
        return id;
    }

    public static Map<Resource,String> resourceToString() {
        Map<Resource,String> resourceMap = new HashMap<>();
        resourceMap.put(Resource.ANIMAL,     "🐺");
        resourceMap.put(Resource.PLANT,      "🍁");
        resourceMap.put(Resource.INSECT,     "🦋");
        resourceMap.put(Resource.FUNGI,      "🍄");
        resourceMap.put(Resource.INKWELL,    "🖋️");
        resourceMap.put(Resource.MANUSCRIPT, "📜");
        resourceMap.put(Resource.QUILL,      "🪶");
        resourceMap.put(Resource.EMPTY,      "⠀⠀");

        /*
        resourceMap.put(Resource.ANIMAL,     "A ");
        resourceMap.put(Resource.PLANT,      "P ");
        resourceMap.put(Resource.INSECT,     "I ");
        resourceMap.put(Resource.FUNGI,      "F ");
        resourceMap.put(Resource.INKWELL,    "W ");
        resourceMap.put(Resource.MANUSCRIPT, "M ");
        resourceMap.put(Resource.QUILL,      "Q ");
        resourceMap.put(Resource.EMPTY,      "⠀⠀");*/

        return resourceMap;
    }

    @Override
    public String toString() {
        return "id: " + id;
    }
}
