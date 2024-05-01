package it.polimi.ingsw.am37.messages;

import java.io.Serializable;

public enum MessageId implements Serializable {
    CREATE,
    REQUEST_LOBBY,
    LOBBIES,
    JOIN,
    UPDATE_LOBBY,
    ACK,
    PLACE_SC,
    TOKEN,
    OBJECTIVE,
    PLACE,
    DRAW_DECK,
    DRAW_AVAIL,
    UPDATE_AVAIL,
    ERROR,
    NOTIFY,
    TERMINATE
}