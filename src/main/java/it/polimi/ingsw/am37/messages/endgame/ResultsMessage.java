package it.polimi.ingsw.am37.messages.endgame;

import it.polimi.ingsw.am37.messages.MessageToClient;
import it.polimi.ingsw.am37.messages.MessageId;
import it.polimi.ingsw.am37.view.ClientSidePlayer;
import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;

import java.util.Map;

public class ResultsMessage extends MessageToClient {
    private final Map<String, Integer> PlayerPoints;
    private final Map<String, Integer> PlayerObjectivesCompleted;

    public ResultsMessage(MessageId id, Map<String, Integer> PlayerPoints, Map<String, Integer> PlayerNumCompletedObjectives){
        super(id);
        this.PlayerPoints = PlayerPoints;
        this.PlayerObjectivesCompleted = PlayerNumCompletedObjectives;
    }

    public void decodeAndExecute(View v){
        for(ClientSidePlayer p : v.getLocalGameInstance().getPlayers()) {
            p.setObjectivesCompleted(PlayerObjectivesCompleted.get(p.getNickname()));
            p.setFinalPoints(PlayerPoints.get(p.getNickname()));
        }
        v.setState(ViewState.SHOW_RESULTS);
        synchronized (v) {
            v.notify();
        }
    }
}
