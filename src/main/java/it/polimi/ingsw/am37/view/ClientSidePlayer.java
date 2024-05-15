package it.polimi.ingsw.am37.view;

import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Kingdom;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Bonus;
import it.polimi.ingsw.am37.model.sides.Front;

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
        points = 0;
        objectivesCompleted = 0;
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

    public boolean hasBlackToken() {
        return hasBlackToken;
    }

    public void setHasBlackToken(boolean hasBlackToken) {
        this.hasBlackToken = hasBlackToken;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(Front placed, int cornersLinked) {
        int toAdd = 0;

        if (placed.getBonus() == null)
            toAdd = placed.getPointsGivenOnPlacement();
        else if (placed.getBonus().equals(Bonus.CORNER))
            toAdd = placed.getPointsGivenOnPlacement() * cornersLinked;
        else {
            if (placed.getBonus().equals(Bonus.INKWELL)) {
                toAdd = (kingdom.getOnFieldResources().get(Resource.INKWELL) + 1) * placed.getPointsGivenOnPlacement();
            } else if (placed.getBonus().equals(Bonus.QUILL)) {
                toAdd = (kingdom.getOnFieldResources().get(Resource.QUILL) + 1) * placed.getPointsGivenOnPlacement();
            } else if (placed.getBonus().equals(Bonus.MANUSCRIPT))
                toAdd = (kingdom.getOnFieldResources().get(Resource.MANUSCRIPT) + 1) * placed.getPointsGivenOnPlacement();
        }

        points = points + toAdd;
    }

    public void setFinalPoints(int points) {
        this.points = points;
    }

    public int getObjectivesCompleted() {
        return objectivesCompleted;
    }

    public void setObjectivesCompleted(int objectivesCompleted) {
        this.objectivesCompleted = objectivesCompleted;
    }
}
