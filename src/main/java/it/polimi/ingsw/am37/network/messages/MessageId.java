package it.polimi.ingsw.am37.network.messages;

import java.io.Serializable;
/**
 * Enumerates all the possible message types
 */
public enum MessageId implements Serializable {
    /**
     * PING message type
     */
    PING,
    /**
     * CREATE message type
     */
    CREATE,
    /**
     * REQUEST_LOBBY message type
     */
    REQUEST_LOBBY,
    /**
     * LOBBIES message type
     */
    LOBBIES,
    /**
     * JOIN message type
     */
    JOIN,
    /**
     * UPDATE_LOBBY message type
     */
    UPDATE_LOBBY,
    /**
     * INITIALIZATION message type
     */
    INIT,
    /**
     * NOTIFY message type
     */
    NOTIFY,
    /**
     * PLACE THE START CARD message type
     */
    PLACE_SC,
    /**
     * TOKEN message type
     */
    TOKEN,
    /**
     * OBJECTIVE message type
     */
    OBJECTIVE,
    /**
     * PLACE THE CARD message type
     */
    PLACE,
    /**
     * DRAW FROM DECK message type
     */
    DRAW_DECK,
    /**
     * DRAW FROM AVAILABLE message type
     */
    DRAW_AVAIL,
    /**
     * UPDATE KINGDOM message type
     */
    UPDATE_KINGDOM,
    /**
     * UPDATE AVAILABLE CARDS message type
     */
    UPDATE_AVAIL,
    /**
     * UPDATE DECK message type
     */
    UPDATE_DECK,
    /**
     * SEND RESULTS message type
     */
    SEND_RESULTS,
    /**
     * ERROR message type
     */
    ERROR,
    /**
     * TERMINATE message type
     */
    TERMINATE
}