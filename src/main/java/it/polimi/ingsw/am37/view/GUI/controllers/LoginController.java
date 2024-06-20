package it.polimi.ingsw.am37.view.GUI.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.io.IOException;

/**
 * This class is the controller for the login.fxml file
 * It handles the login screen, where the player can choose to create a new game or join an existing one
 */
public class LoginController extends GUIController {
    /**
     * This text is used to display a message to the user in case of disconnection
     */
    @FXML
    private Text disconnectionText;

    /**
     * This method is called when the user clicks on the create button
     * It changes the scene to the createGame.fxml file
     * @param createClick the event of the click
     * @throws IOException if the file is not found
     */
    public void createButtonClicked(javafx.event.ActionEvent createClick) throws IOException {

        changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/createGame.fxml", "create", createClick);

    }

    /**
     * This method is called when the user clicks on the join button
     * It changes the scene to the joinGame.fxml file
     * @param joinClick the event of the click
     * @throws IOException if the file is not found
     */
    public void joinButtonClicked(ActionEvent joinClick) throws IOException {

        changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/joinGame.fxml", "join", joinClick);

    }

    /**
     * This method is called when the user is redirected here after another player in his lobby has disconnected
     */
    public void setDisconnectionText(String text) {
        disconnectionText.setText(text);
    }
}
