package it.polimi.ingsw.am37.network.messages;

import java.io.Serializable;

/**
 * This class is the abstract class that represents a message that can be sent
 * through the network.
 * It has a field id that is the id of the message.
 */
public abstract class Message implements Serializable {
    /**
     * The id of the message.
     */
    protected final MessageId id;

    /**
     * Constructor.
     * @param id The id of the message.
     */
    public Message(MessageId id) {
        this.id = id;
    }

    /**
     * Returns the id of the message.
     * @return The id of the message.
     */
    public MessageId getId() {
        return id;
    }

    /**
     * Returns a string representation of the message.
     * @return A string representation of the message.
     */
    @Override
    public String toString() {
        return "id: " + id;
    }
}
