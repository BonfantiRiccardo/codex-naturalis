package it.polimi.ingsw.am37.view.GUI.controllers;

import it.polimi.ingsw.am37.view.clientmodel.ClientSidePlayer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the controller for the ShowResults.fxml file
 */
public class ShowResultsController extends GUIController {
    /**
     * Text that contains the name of the winner
     */
    @FXML
    Text winner;
    /**
     * Text that contains the name of the first player
     */
    @FXML
    Text player1;
    /**
     * Text that contains the name of the second player
     */
    @FXML
    Text player2;
    /**
     * Text that contains the name of the third player
     */
    @FXML
    Text player3;
    /**
     * Text that contains the name of the fourth player
     */
    @FXML
    Text player4;
    /**
     * Text that contains the points and completions of the first player
     */
    @FXML
    Text pointsAndCompletion1;
    /**
     * Text that contains the points and completions of the second player
     */
    @FXML
    Text pointsAndCompletion2;
    /**
     * Text that contains the points and completions of the third player
     */
    @FXML
    Text pointsAndCompletion3;
    /**
     * Text that contains the points and completions of the fourth player
     */
    @FXML
    Text pointsAndCompletion4;

    /**
     * Button that allows the player to return to the home page
     */
    @FXML
    Button returnToHome;
    /**
     * Button that allows the player to play again
     */
    @FXML
    Button closeGame;

    /**
     * Method that closes the application for the player
     */
    public void onCloseClick() {
        System.out.println();
        System.out.println("\n\nClosing application...");
        Platform.exit();
        System.exit(0);
    }

    /**
     * Method that allows the player to return to the home page
     * @param playAgainClick ActionEvent that allows the player to return to the home page
     * @throws IOException if the file is not found
     */
    public void onPlayAgainClick(ActionEvent playAgainClick) throws IOException {
        changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/login.fxml", "login", playAgainClick);
    }

    /**
     * Method that sets the winner of the game
     * @param playerTable List of players
     */
    public void setWinner(List<ClientSidePlayer> playerTable) {
        StringBuilder winner;
        int maxPoints = playerTable.getFirst().getPoints();
        int maxObjectives = playerTable.getFirst().getObjectivesCompleted();
        List<ClientSidePlayer> winners = new ArrayList<>(playerTable);

        winner = new StringBuilder(playerTable.get(0).getNickname());
        winners.remove(0);

        for (ClientSidePlayer p: winners) {
            if (p.getPoints() == maxPoints && p.getObjectivesCompleted() == maxObjectives) {
                winner.append(" and ").append(p.getNickname());
            } else
                break;
        }
        this.winner.setText(winner.toString());
    }

    /**
     * Method that loads the results of the game
     */
    public void loadResults() {
        List<ClientSidePlayer> players = new ArrayList<>(guiReference.getLocalGameInstance().getPlayers());
        List<ClientSidePlayer> finalOrder = new ArrayList<>();

        players.add(guiReference.getLocalGameInstance().getMe());

        for(ClientSidePlayer p: players) {
            if (finalOrder.isEmpty())
                finalOrder.add(p);
            else {
                boolean inserted = false;
                for (ClientSidePlayer p2 : finalOrder) {
                    if (p.getPoints() > p2.getPoints()) {
                        finalOrder.add(finalOrder.indexOf(p2), p);
                        inserted = true;
                        break;
                    } else if (p.getPoints() == p2.getPoints()) {
                        if (p.getObjectivesCompleted() >= p2.getObjectivesCompleted()) {
                            finalOrder.add(finalOrder.indexOf(p2), p);
                            inserted = true;
                            break;
                        }
                    }
                }
                if (!inserted)
                    finalOrder.add(p);
            }
        }

        setWinner(finalOrder);
        int i = 1;
        for (ClientSidePlayer p: finalOrder) {
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
