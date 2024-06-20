package it.polimi.ingsw.am37.view.GUI.controllers;


import it.polimi.ingsw.am37.view.ViewState;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;

/**
 * This class is the controller of the joinGame.fxml file
 * It manages the join game scene
 */
public class JoinGameController extends GUIController implements PropertyChangeListener {
    /**
     * list of the lobbies received from server
     */
    @FXML
    private ListView<Integer> lobbyList;
    /**
     * refresh button to refresh the list of lobbies
     */
    @FXML
    private Button refresh;
    /**
     * join game button to join the selected lobby
     */
    @FXML
    private Button joinGame;
    /**
     * create game button to create a new lobby
     */
    @FXML
    private Button createGame;
    /**
     * text field to insert information
     */
    @FXML
    private Text waitingText;
    /**
     * text field to show the selected lobby
     */
    @FXML
    private Text lobbySelected;
    /**
     * text field to insert the nickname
     */
    @FXML
    private TextField nicknameSelected;

    /**
     * selected lobby value
     */
    private int selectedLobbyValue = 0;
    /**
     * action event
     */
    private ActionEvent event;
    /**
     * boolean to check if the state is changing
     */
    private boolean changingState = false;

    /**
     * Method to refresh the list of lobbies
     */
    @FXML
    public void refreshClicked(){
        guiReference.getLobbies();
        waitingText.setText("Wait for server response");
    }

    /**
     * Method to join the selected lobby
     * @param joinClick event fired when the join game button is clicked
     */
    @FXML
    public void joinGameClick(ActionEvent joinClick){
        String nickname = nicknameSelected.getText();
        if (selectedLobbyValue != 0 && nickname != null && !nickname.isEmpty()) {
            guiReference.joinLobby(selectedLobbyValue, nickname);
            waitingText.setText("Waiting for server response");
            event = joinClick;
        } else
            waitingText.setText("Lobby or nickname invalid");
    }

    /**
     * Method to change the scene to the "create game" scene
     * @param createClick event fired when the create game button is clicked
     * @throws IOException if the file is not found
     */
    @FXML
    public void createGameClick(ActionEvent createClick) throws IOException {
        guiReference.setState(ViewState.CREATE_JOIN);

        changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/createGame.fxml", "create", createClick);
    }

    /**
     * Handles the property change event
     * @param evt A PropertyChangeEvent object describing the event source and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "LOBBIES": {
                Platform.runLater( () -> {
                    List<Integer> newLobbies = (List<Integer>) evt.getNewValue();
                    if (evt.getNewValue() != null && !newLobbies.isEmpty()) {
                        waitingText.setText("");

                        int index = 0;
                        for (int j: lobbyList.getItems()) {
                            boolean check = false;
                            for (int i: newLobbies) {
                                if (i == j) {
                                    check = true;
                                    break;
                                }
                            }
                            if (!check)
                                lobbyList.getItems().remove(index);
                            index++;
                        }

                        for (int j: newLobbies) {
                            boolean check = false;
                            for (int i: lobbyList.getItems()) {
                                if (i == j) {
                                    check = true;
                                    break;
                                }
                            }
                            if (!check)
                                lobbyList.getItems().add(j);
                        }

                        lobbyList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
                            @Override
                            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                                if (lobbyList.getSelectionModel().getSelectedItem()!=null)
                                    selectedLobbyValue = lobbyList.getSelectionModel().getSelectedItem();
                                lobbySelected.setText(String.valueOf(selectedLobbyValue));
                            }
                        });
                    }
                });
                break;
            }
            case "ERROR": {
                Platform.runLater(() -> {
                    waitingText.setText((String) evt.getNewValue());
                    if(evt.getNewValue().equals("There are no active games.")) {
                        lobbySelected.setText("");
                        lobbyList.getItems().clear();
                    }
                });

                guiReference.setState(ViewState.CHOOSE_LOBBY);
                break;
            }
            case "CHANGED_STATE": {
                if(evt.getNewValue().equals(ViewState.WAIT_IN_LOBBY) || evt.getNewValue().equals(ViewState.PLACE_SC)) {
                    if (!changingState) {
                        changingState = true;
                        Platform.runLater(() -> {
                            try {
                                changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/lobby.fxml", "lobby", event);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                } else if (evt.getNewValue().equals(ViewState.DISCONNECTION)) {
                    Platform.runLater( () -> {
                        try {
                            changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/login.fxml", "login", new ActionEvent(joinGame, null));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                break;
            }
        }
    }
}