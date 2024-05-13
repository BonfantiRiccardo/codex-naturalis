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
                    if(v.getState().equals(ViewState.CHOOSE_OBJECTIVE)) {
                        v.setState(ViewState.NOT_TURN);
                        v.notify();
                    }
                }
                case "your turn" -> {
                    v.setState(ViewState.PLACE);
                    v.notify();
                }
                case "your turn and token" -> {
                    v.getLocalGameInstance().getMe().setHasBlackToken(true);
                    v.setState(ViewState.PLACE);
                    v.notify();
                }
                case "place ok" -> {
                    v.setState(ViewState.DRAW);
                    v.notify();
                }
                case  "draw ok" -> {                    //TO DELETE SINCE I CAN'T JUST NOTIFY THE PLAYER, I HAVE TO SEND THE NEW CARD
                    v.setState(ViewState.NOT_TURN);     //MAYBE USEFUL IN DRAW FROM AVAILABLE
                    v.notify();
                }
            }
        }
    }
}
