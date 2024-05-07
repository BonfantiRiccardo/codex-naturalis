package it.polimi.ingsw.am37.view;

import it.polimi.ingsw.am37.model.player.Kingdom;
import it.polimi.ingsw.am37.model.player.Token;

public class ClientSidePlayer {

    private final String nickname;
    private Token token;
    private Kingdom kingdom;
    private boolean hasBlackToken;
    private int points;
    private int objectivesCompleted;

    public ClientSidePlayer(String nickname) {
        this.nickname = nickname;
        hasBlackToken = false;
    }

    public String getNickname() {
        return nickname;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Kingdom getKingdom() {
        return kingdom;
    }

    public void setKingdom(Kingdom kingdom) {
        this.kingdom = kingdom;
    }

    public boolean HasBlackToken() {
        return hasBlackToken;
    }

    public void setHasBlackToken(boolean hasBlackToken) {
        this.hasBlackToken = hasBlackToken;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getObjectivesCompleted() {
        return objectivesCompleted;
    }

    public void setObjectivesCompleted(int objectivesCompleted) {
        this.objectivesCompleted = objectivesCompleted;
    }
}
