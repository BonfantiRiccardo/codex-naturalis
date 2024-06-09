package it.polimi.ingsw.am37.network.messages.lobby;

import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToClient;
import it.polimi.ingsw.am37.view.clientmodel.ClientSidePlayer;
import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;

import java.util.List;

/**
 * This message is sent by the server to the client when a player joins the lobby.
 * The server will add the player to the lobby and send the updated lobby view to all the players in the lobby.
 */
public class UpdateLobbyMessage extends MessageToClient {
    /**
     * The nickname of the player.
     */
    private final String yourNickname;
    /**
     * The list of nicknames of the other players in the lobby.
     */
    private final List<String> otherPlayerNickname;
    /**
     * The number of the lobby.
     */
    private final int lobbyNum;
    /**
     * The total number of players in the lobby.
     */
    private final int totalPlayers;


    /**
     * Constructor.
     * @param id The message id.
     * @param yourNickname The nickname of the player.
     * @param playerNickname The list of nicknames of the other players in the lobby.
     * @param lobbyNum The number of the lobby.
     * @param totalPlayers The total number of players in the lobby.
     */
    public UpdateLobbyMessage(MessageId id, String yourNickname, List<String> playerNickname, int lobbyNum, int totalPlayers) {
        super(id);
        this.yourNickname = yourNickname;
        this.otherPlayerNickname = playerNickname;
        this.lobbyNum = lobbyNum;
        this.totalPlayers = totalPlayers;
    }

    /**
     * This method will add the player to the lobby and send the updated lobby view to all the players in the lobby.
     * @param v The view.
     */
    @Override
    public void decodeAndExecute(View v) {
        synchronized (v) {
            v.getLocalGameInstance().setMe(new ClientSidePlayer(yourNickname));

            for (String nick: otherPlayerNickname)
                if(!yourNickname.equals(nick))
                    v.getLocalGameInstance().addPlayer(new ClientSidePlayer(nick));

            v.getLocalGameInstance().setNumOfLobby(lobbyNum);

            v.getLocalGameInstance().setNumOfPlayers(totalPlayers);

            if (!v.getState().equals(ViewState.PLACE_SC))
                v.setState(ViewState.WAIT_IN_LOBBY);
            v.notify();
        }
    }

    /**
     * This method will return a string representation of the message.
     * @return The string representation of the message.
     */
    @Override
    public String toString() {
        return "Received: " + super.toString() + " | you: " + yourNickname + " | others: " + otherPlayerNickname + " | lobby num: " + lobbyNum + " | total: " + totalPlayers;
    }
}
