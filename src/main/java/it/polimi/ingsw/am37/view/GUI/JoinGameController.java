package it.polimi.ingsw.am37.view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JoinGameController {

    @FXML
    private MenuButton lobbyList;
    @FXML
    private Button refresh;
    @FXML
    private Button joinGame;

    //private ArrayList<Integer> lobby;   //lista delle lobby che deve essere inizializzata e aggiornata
    private List<Integer> lobby = List.of(1, 2, 3, 4, 5);
    private String selectedLobbyText;
    private int selectedLobbyValue;


    @FXML
    public void initialize() {
        for (Integer option : lobby) {
            MenuItem menuItem = new MenuItem("Lobby: " + option);
            menuItem.setOnAction(event -> {
                selectedLobbyValue = option;
                lobbyList.setText("Lobby:  " + option);
                selectedLobbyText = "Lobby: " + option;
            });
            lobbyList.getItems().add(menuItem);
        }
    }

    @FXML
    private void handleSaveButtonAction() {
        // Save or use the selected option value as needed
        System.out.println("Selected Option Value: " + selectedLobbyValue);
        System.out.println("Selected Option Text: " + selectedLobbyText);
    }
    @FXML
    public void refreshClicked(){
        initialize();
    }

    @FXML
    public void joinGameClick(ActionEvent joinClick) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("lobby.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) joinClick.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }


}
