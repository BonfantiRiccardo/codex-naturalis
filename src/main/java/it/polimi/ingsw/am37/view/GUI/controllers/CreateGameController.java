package it.polimi.ingsw.am37.view.GUI.controllers;


import it.polimi.ingsw.am37.view.ViewState;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

/**
 * This class is the controller for the createGame.fxml file.
 * It handles the creation of a new lobby and the joining of an existing one.
 */
public class CreateGameController extends GUIController implements PropertyChangeListener {

    /**
     * The number of players.
     */
    @FXML
    private MenuButton pl_num;
    /**
     * The second player of the game.
     */
    @FXML
    private MenuItem pl2;
    /**
     * The third player of the game.
     */
    @FXML
    private MenuItem pl3;
    /**
     * The fourth player of the game.
     */
    @FXML
    private MenuItem pl4;
    /**
     * The create button.
     */
    @FXML
    private Button createButton;
    /**
     * The join button.
     */
    @FXML
    private Button joinButton;
    /**
     * The nickname of the player.
     */
    @FXML
    private TextField nick;
    /**
     * The text that shows the information.
     */
    @FXML
    private Text infoText;

    /**
     * The nickname of the player.
     */
    private String nickname;
    /**
     * The number of players.
     */
    private int numPlayers;
    /**
     * The event that is triggered when the create button is clicked.
     */
    private ActionEvent event;


    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        pl2.setOnAction(event -> updateMenuButtonText(pl2));
        pl3.setOnAction(event -> updateMenuButtonText(pl3));
        pl4.setOnAction(event -> updateMenuButtonText(pl4));
    }

    /**
     * Saves the item selected by the player.
     *
     * @param selectedItem the item that has been selected
     */
    private void updateMenuButtonText(MenuItem selectedItem) {
        pl_num.setText(selectedItem.getText());
        String[] toParse = selectedItem.getText().split(" ");
        numPlayers = Integer.parseInt(toParse[0]);
    }

    /**
     * Handles the creation of a new lobby.
     *
     * @param createClicked the event that is triggered when the create button is clicked
     * @throws IOException if the file is not found
     */
    public void createButtonClicked(ActionEvent createClicked) throws IOException {
        nickname = nick.getText();

        if (nickname != null && !nickname.isEmpty() && numPlayers != 0) {
            guiReference.createLobby(nickname, numPlayers);
            event = createClicked;
        } else
            infoText.setText("Lobby or nickname invalid");

    }

    /**
     * Changes scene to the join lobby one.
     *
     * @param joinClicked the event that is triggered when the join button is clicked
     * @throws IOException if the file is not found
     */
    public void joinButtonClicked(ActionEvent joinClicked) throws IOException {
        guiReference.setState(ViewState.CHOOSE_LOBBY);
        changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/joinGame.fxml", "join", joinClicked);
    }

    /**
     * Handles the selection of the number of players.
     */
    @FXML
    private void getPl2(){
        numPlayers=2;
    }
    /**
     * Handles the selection of the number of players.
     */
    @FXML
    private void getPl3(){
        numPlayers=3;
    }
    /**
     * Handles the selection of the number of players.
     */
    @FXML
    private void getPl4(){
        numPlayers=4;
    }

    /**
     * Returns the number of players.
     *
     * @return the number of players
     */
    public int getNumPlayers(){
        return numPlayers;
    }

    /**
     * Returns the nickname of the player.
     *
     * @return the nickname of the player
     */
    public String getNickname(){
        return nickname;
    }

    /**
     * Handles the property change.
     *
     * @param evt the event that is triggered when a property changes
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "CHANGED_STATE": {
                if (evt.getNewValue().equals(ViewState.WAIT_IN_LOBBY)) {
                    Platform.runLater(() -> {
                        try {
                            changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/lobby.fxml", "lobby", event);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                break;
            }

            case "ERROR": {
                infoText.setText((String) evt.getNewValue());
                guiReference.setState(ViewState.CHOOSE_LOBBY);
                break;
            }
        }
    }
}
