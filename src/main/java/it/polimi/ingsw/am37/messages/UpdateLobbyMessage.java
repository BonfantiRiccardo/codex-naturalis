package it.polimi.ingsw.am37.messages;

import java.util.List;

public class UpdateLobbyMessage extends MessageToClient{
    private final List<String> playerNickname;
    private final int joinedPlayer;
    private final int totalPlayers;


    public UpdateLobbyMessage(MessageId id, List<String> playerNickname, int joinedPlayer, int totalPlayers) {
        super(id);
        this.playerNickname = playerNickname;
        this.joinedPlayer = joinedPlayer;
        this.totalPlayers = totalPlayers;
    }

    @Override
    public void decodeAndExecute() {

    }

    public List<String> getPlayerNickname() {
        return playerNickname;
    }

    public int getJoinedPlayer() {
        return joinedPlayer;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }

    @Override
    public String toString() {
        return "Received: " + super.toString() + " | nick: " + playerNickname + " | joined: " + joinedPlayer + " | total: " + totalPlayers;
    }
}
