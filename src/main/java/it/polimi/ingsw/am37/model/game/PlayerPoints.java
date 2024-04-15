package it.polimi.ingsw.am37.model.game;

import it.polimi.ingsw.am37.model.player.Player;

public class PlayerPoints {
    Player player;
    Integer points;
    Integer completions;

    public PlayerPoints(Player player,Integer points, Integer completions){
        this.player = player;
        this.points = points;
        this.completions = completions;
    }

    public Player getPlayer() {
        return player;
    }

    public Integer getPoints() {
        return points;
    }

    public Integer getNumOfCompletion() {
        return completions;
    }
}
