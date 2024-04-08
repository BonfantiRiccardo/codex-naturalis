package it.polimi.ingsw.am37.model.cards;

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

    @Override
    public String toString() {
        return "id: " + id;
    }
}
