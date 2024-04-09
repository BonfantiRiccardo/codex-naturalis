package it.polimi.ingsw.am37.model.game;

import it.polimi.ingsw.am37.model.player.Player;

import it.polimi.ingsw.am37.model.player.Token;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {
    GameModel g = new GameModel(createList());

    @Test
    public void preparationTest() {
        //TODO
    }

    public List<Player> createList() {
        List<Player> playerList = new ArrayList<>();
        playerList.add(new Player("Riccardo", Token.BLUE));
        playerList.add(new Player("Dario", Token.RED));
        playerList.add(new Player("Alberto", Token.GREEN));
        return playerList;
    }
}