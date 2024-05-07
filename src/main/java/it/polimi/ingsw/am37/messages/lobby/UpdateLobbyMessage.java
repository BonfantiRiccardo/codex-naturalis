package it.polimi.ingsw.am37.messages.lobby;

import it.polimi.ingsw.am37.messages.MessageId;
import it.polimi.ingsw.am37.messages.MessageToClient;
import it.polimi.ingsw.am37.view.ClientSidePlayer;
import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;

import java.util.List;

public class UpdateLobbyMessage extends MessageToClient {
    private final String yourNickname;
    private final List<String> otherPlayerNickname;
    private final int lobbyNum;
    private final int totalPlayers;


    public UpdateLobbyMessage(MessageId id, String yourNickname, List<String> playerNickname, int lobbyNum, int totalPlayers) {
        super(id);
        this.yourNickname = yourNickname;
        this.otherPlayerNickname = playerNickname;
        this.lobbyNum = lobbyNum;
        this.totalPlayers = totalPlayers;
    }

    @Override
    public void decodeAndExecute(View v) {
        synchronized (v) {
            v.getLocalGameInstance().setMe(new ClientSidePlayer(yourNickname));

            for (String nick: otherPlayerNickname)
                if(!yourNickname.equals(nick))
                    v.getLocalGameInstance().addPlayer(new ClientSidePlayer(nick));

            v.getLocalGameInstance().setNumOfLobby(lobbyNum);

            v.getLocalGameInstance().setNumOfPlayers(totalPlayers);

            v.printMyLobby();
            v.setState(ViewState.WAIT_IN_LOBBY);
            v.notify();
        }
    }

    @Override
    public String toString() {
        return "Received: " + super.toString() + " | you: " + yourNickname + " | others: " + otherPlayerNickname + " | lobby num: " + lobbyNum + " | total: " + totalPlayers;
    }
}
