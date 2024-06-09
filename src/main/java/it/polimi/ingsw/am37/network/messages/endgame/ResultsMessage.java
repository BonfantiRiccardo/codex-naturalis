package it.polimi.ingsw.am37.network.messages.endgame;

import it.polimi.ingsw.am37.network.messages.MessageToClient;
import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.view.clientmodel.ClientSidePlayer;
import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;

import java.beans.PropertyChangeEvent;
import java.util.Map;

/**
 * the ResultsMessage class implements the MessageToClient Interface and
 * handles the messages in the endgame that gives information about the resulting points of each player.
 */
public class ResultsMessage extends MessageToClient {
    /**
     * A map that associates each player's nickname with their final points.
     */
    private final Map<String, Integer> playerPoints;
    /**
     * A map that associates each player's nickname with the number of objectives they completed.
     */
    private final Map<String, Integer> playerObjectivesCompleted;
    /**
     * The constructor creates a new ResultsMessage.
     * @param id the message id
     * @param playerPoints a map that associates each player's nickname with their final points
     * @param playerNumCompletedObjectives a map that associates each player's nickname with the number of objectives they completed
     */
    public ResultsMessage(MessageId id, Map<String, Integer> playerPoints, Map<String, Integer> playerNumCompletedObjectives){
        super(id);
        this.playerPoints = playerPoints;
        this.playerObjectivesCompleted = playerNumCompletedObjectives;
    }

    /**
     * This method is called by the client to decode the message and execute the action.
     * It sets the final points and the number of objectives completed for each player.
     * It also sets the final points and the number of objectives completed for the local player.
     * It then sets the view state to SHOW_RESULTS and notifies the view.
     * @param v the view that has to execute the action
     */
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
