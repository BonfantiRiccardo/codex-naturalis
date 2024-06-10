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
     * The following are the FXML elements used in the controller.
     */
    @FXML
    /**
     * The number of players.
     */
    private MenuButton pl_num;
    @FXML
    /**
     * The second player of the game.
     */
    private MenuItem pl2;
    @FXML
    /**
     * The third player of the game.
     */
    private MenuItem pl3;
    @FXML
    /**
     * The fourth player of the game.
     */
    private MenuItem pl4;
    @FXML
    /**
     * The create button.
     */
    private Button createButton;
    @FXML
    /**
     * The join button.
     */
    private Button joinButton;
    @FXML
    /**
     * The nickname of the player.
     */
    private TextField nick;
    @FXML
    /**
     * The text that shows the information.
     */
    private Text infoText;

    /**
     * The following are the variables used in the controller.
     */
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



    @FXML
    public void initialize() {
        pl2.setOnAction(event -> updateMenuButtonText(pl2));
        pl3.setOnAction(event -> updateMenuButtonText(pl3));
        pl4.setOnAction(event -> updateMenuButtonText(pl4));
    }

    private void updateMenuButtonText(MenuItem selectedItem) {
        pl_num.setText(selectedItem.getText());
        String[] toParse = selectedItem.getText().split(" ");
        numPlayers = Integer.parseInt(toParse[0]);
    }

    public void createButtonClicked(ActionEvent createClicked) throws IOException {
        nickname = nick.getText();

        if (nickname != null && !nickname.isEmpty() && numPlayers != 0) {
            guiReference.createLobby(nickname, numPlayers);
            event = createClicked;
        } else
            infoText.setText("Lobby or nickname invalid");

    }

    public void joinButtonClicked(ActionEvent joinClicked) throws IOException {
        guiReference.setState(ViewState.CHOOSE_LOBBY);
        changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/joinGame.fxml", "join", joinClicked);
    }

    @FXML
    private void getPl2(){
        numPlayers=2;
    }
    @FXML
    private void getPl3(){
        numPlayers=3;
    }
    @FXML
    private void getPl4(){
        numPlayers=4;
    }

    public int getNumPlayers(){
        return numPlayers;
    }

    public String getNickname(){
        return nickname;
    }

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
