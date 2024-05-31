package it.polimi.ingsw.am37.view.GUI.controllers;

import it.polimi.ingsw.am37.view.clientmodel.ClientSidePlayer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowResultsController extends GUIController {
    @FXML
    Text winner;
    @FXML
    Text player1;
    @FXML
    Text player2;
    @FXML
    Text player3;
    @FXML
    Text player4;
    @FXML
    Text pointsAndCompletion1;
    @FXML
    Text pointsAndCompletion2;
    @FXML
    Text pointsAndCompletion3;
    @FXML
    Text pointsAndCompletion4;

    @FXML
    Button returnToHome;
    @FXML
    Button closeGame;

    public void onCloseClick() {
        System.out.println();
        System.out.println("Closing application...");
        System.exit(0);
    }

    public void onPlayAgainClick(ActionEvent playAgainClick) throws IOException {
        changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/login.fxml", "login", playAgainClick);
    }

    public void setWinner(String winner) {
        this.winner.setText("The winner is: " + winner);
    }

    public void loadResults() {
        List<ClientSidePlayer> players = new ArrayList<>(guiReference.getLocalGameInstance().getPlayers());
        List<ClientSidePlayer> playerTable = new ArrayList<>(guiReference.getLocalGameInstance().getPlayers());
        int max;

        players.add(guiReference.getLocalGameInstance().getMe());
        playerTable.add(guiReference.getLocalGameInstance().getMe());
        for(int i = 0; i < players.size(); i++) {
            max = players.getFirst().getPoints();
            for (ClientSidePlayer p : players) {
                if (max < p.getPoints())
                    playerTable.set(i, p);
            }
            players.remove(playerTable.get(i));
        }

        setWinner(playerTable.getFirst().getNickname());
        int i = 1;
        for (ClientSidePlayer p: playerTable) {
            if (i == 1) {
                player1.setText(i + "°: " + p.getNickname());
                pointsAndCompletion1.setText("Points: " + p.getPoints() + " Completions: " + p.getObjectivesCompleted());
            } else if (i == 2) {
                player2.setText(i + "°: " + p.getNickname());
                pointsAndCompletion2.setText("Points: " + p.getPoints() + " Completions: " + p.getObjectivesCompleted());
            } else if (i == 3) {
                player3.setText(i + "°: " + p.getNickname());
                pointsAndCompletion3.setText("Points: " + p.getPoints() + " Completions: " + p.getObjectivesCompleted());
            } else if (i == 4) {
                player4.setText(i + "°: " + p.getNickname());
                pointsAndCompletion4.setText("Points: " + p.getPoints() + " Completions: " + p.getObjectivesCompleted());
            }
            i++;
        }
    }

}
