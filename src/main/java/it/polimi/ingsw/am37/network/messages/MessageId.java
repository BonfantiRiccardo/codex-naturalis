package it.polimi.ingsw.am37.network.messages;

import java.io.Serializable;

public enum MessageId implements Serializable {
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