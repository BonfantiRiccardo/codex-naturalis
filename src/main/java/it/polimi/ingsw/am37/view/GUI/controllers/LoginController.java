package it.polimi.ingsw.am37.view.GUI.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.io.IOException;

public class LoginController extends GUIController {
    @FXML
    private Text disconnectionText;

    public void createButtonClicked(javafx.event.ActionEvent createClick) throws IOException {

        changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/createGame.fxml", "create", createClick);

    }

    public void joinButtonClicked(ActionEvent joinClick) throws IOException {

        changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/joinGame.fxml", "join", joinClick);

    }

    public void setDisconnectionText(String text) {
        disconnectionText.setText(text);
    }
}
