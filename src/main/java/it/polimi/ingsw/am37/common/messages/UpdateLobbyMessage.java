package it.polimi.ingsw.am37.common.messages;

public class UpdateLobbyMessage extends Message{
    private final String playerNickname;
    private final int joinedPlayer;
    private final int totalPlayers;


    public UpdateLobbyMessage(MessageId id, String playerNickname, int joinedPlayer, int totalPlayers) {
        super(id);
        this.playerNickname = playerNickname;
        this.joinedPlayer = joinedPlayer;
        this.totalPlayers = totalPlayers;
    }

    public String getPlayerNickname() {
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
