package it.polimi.ingsw.am37.messages;

import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;

public class ErrorMessage extends MessageToClient{
    private final String description;

    public ErrorMessage(MessageId id, String description) {
        super(id);
        this.description = description;
    }

    @Override
    public void decodeAndExecute(View v) {
        synchronized (v) {
            v.printError(description);      //DO NOT PRINT IN THE MESSAGe
            v.setState(ViewState.ERROR);
            v.notify();
        }
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Received: " + super.toString() + " | description: " + description;
    }
}
