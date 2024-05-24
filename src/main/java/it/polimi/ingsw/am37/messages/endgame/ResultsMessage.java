package it.polimi.ingsw.am37.messages.endgame;

import it.polimi.ingsw.am37.messages.MessageToClient;
import it.polimi.ingsw.am37.messages.MessageId;
import it.polimi.ingsw.am37.view.ClientSidePlayer;
import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;

import java.beans.PropertyChangeEvent;
import java.util.Map;

public class ResultsMessage extends MessageToClient {
    private final Map<String, Integer> playerPoints;
    private final Map<String, Integer> playerObjectivesCompleted;

    public ResultsMessage(MessageId id, Map<String, Integer> playerPoints, Map<String, Integer> playerNumCompletedObjectives){
        super(id);
        this.playerPoints = playerPoints;
        this.playerObjectivesCompleted = playerNumCompletedObjectives;
    }

    public void decodeAndExecute(View v){
        for(ClientSidePlayer p : v.getLocalGameInstance().getPlayers()) {
            p.setObjectivesCompleted(playerObjectivesCompleted.get(p.getNickname()));
            p.setFinalPoints(playerPoints.get(p.getNickname()));
        }

        for(String name: playerPoints.keySet())
            if (v.getLocalGameInstance().getMe().getNickname().equals(name)) {
                v.getLocalGameInstance().getMe().setFinalPoints(playerPoints.get(name));
                v.getLocalGameInstance().getMe().setObjectivesCompleted(playerObjectivesCompleted.get(name));
            }

        v.setState(ViewState.SHOW_RESULTS);

        PropertyChangeEvent evt = new PropertyChangeEvent(
                this,
                "RESULTS",
                null, null);
        v.propertyChange(evt);

        synchronized (v) {
            v.notify();
        }
    }
}
