package it.polimi.ingsw.am37.messages;

import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;

public class NotifyMessage extends MessageToClient{
    private final String message;

    public NotifyMessage(MessageId id, String message) {
        super(id);
        this.message = message;
    }

    @Override
    public void decodeAndExecute(View v) {
        synchronized (v) {
            switch (message) {
                case "start card ok" -> {
                    v.setState(ViewState.CHOOSE_TOKEN);
                    v.notify();
                }
                case "token ok" -> {
                    v.setState(ViewState.CHOOSE_OBJECTIVE);
                    v.notify();
                }
                case "objective ok" -> {
                    v.setState(ViewState.NOT_TURN);
                    v.notify();
                }
                case "your turn", "draw ok" -> {
                    v.setState(ViewState.PLACE);
                    v.notify();
                }
                case "place ok" -> {
                    v.setState(ViewState.DRAW);
                    v.notify();
                }
            }
        }
    }
}
