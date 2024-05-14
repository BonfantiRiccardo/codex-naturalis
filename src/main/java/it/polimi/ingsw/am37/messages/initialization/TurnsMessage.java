package it.polimi.ingsw.am37.messages.initialization;

import it.polimi.ingsw.am37.messages.MessageId;
import it.polimi.ingsw.am37.messages.MessageToClient;
import it.polimi.ingsw.am37.view.View;

import java.util.List;

public class TurnsMessage extends MessageToClient {
    private final List<String> playersInOrder;


    public TurnsMessage(MessageId id, List<String> playersInOrder) {
        super(id);
        this.playersInOrder = playersInOrder;
    }

    @Override
    public void decodeAndExecute(View v) {
        // SET A LIST OF NICKNAMES IN THE CLIENT SIDE MODEL AND GIVE THE BLACK TOKEN TO THE FIRST

        // DO NOT SET A NEW GAME STATE BECAUSE IT IS STILL NOT TURN
        // ANOTHER MESSAGE WILL NOTIFY THE TURN

        // EVERY TIME YOU RECEIVE A DECK UPDATE YOU SHOULD PRINT IN THE "UPDATES" A LINE THAT SAYS WHOSE TURN IT IS.
        // METHOD next() TO SET THE CURRENT TURN TO THE NEXT PLAYER SO THAT THE RIGHT ONE GETS PRINTED


    }
}
