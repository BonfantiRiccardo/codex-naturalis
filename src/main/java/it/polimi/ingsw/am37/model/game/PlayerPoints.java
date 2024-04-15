package it.polimi.ingsw.am37.model.game;

import it.polimi.ingsw.am37.model.player.Player;

public class PlayerPoints {
    Player player;
    Integer points;
    Integer compl;

    public PlayerPoints(Player player,Integer points, Integer compl){
        this.player=player;
        this.points=points;
        this.compl=compl;
    }

    public Integer getPoints() {
        return points;
    }

    public Player getPlayer() {
        return player;
    }

    public Integer getCompl() {
        return compl;
    }
}
