package it.polimi.ingsw.am37.network.messages;

import java.io.Serializable;
/**
 * Enumerates all the possible message types
 */
public enum MessageId implements Serializable {
    PING,
    CREATE,
    REQUEST_LOBBY,
    LOBBIES,
    JOIN,
    UPDATE_LOBBY,
    INIT,
    NOTIFY,
    PLACE_SC,
    TOKEN,
    OBJECTIVE,
    PLACE,
    DRAW_DECK,
    DRAW_AVAIL,
    UPDATE_KINGDOM,
    UPDATE_AVAIL,
    UPDATE_DECK,
    SEND_RESULTS,
    ERROR,
    TERMINATE
}