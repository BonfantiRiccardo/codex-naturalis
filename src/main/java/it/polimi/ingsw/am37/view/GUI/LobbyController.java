package it.polimi.ingsw.am37.view.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LobbyController {
    @FXML
    private Text playerName;
    @FXML
    private Text lobbyNumber;
    @FXML
    private Text playersNeededToStart;
    @FXML
    private Button message;

    public void onMessageReceived(){
        playerName.setText("1234");
        lobbyNumber.setText("ric");
        playersNeededToStart.setText("1");
    }

}
