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


public class JoinGameController extends GUIController implements PropertyChangeListener {

    @FXML
    private ListView<Integer> lobbyList;
    @FXML
    private Button refresh;
    @FXML
    private Button joinGame;
    @FXML
    private Button createGame;
    @FXML
    private Text waitingText;
    @FXML
    private Text lobbySelected;
    @FXML
    private TextField nicknameSelected;

    //private ArrayList<Integer> lobby;   //lista delle lobby che deve essere inizializzata e aggiornata

    private int selectedLobbyValue = 0;
    private ActionEvent event;

    @FXML
    public void refreshClicked(){
        guiReference.getLobbies();
        waitingText.setText("Wait for server response");
    }

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

    @FXML
    public void createGameClick(ActionEvent createClick) throws IOException {
        guiReference.setState(ViewState.CREATE_JOIN);

        changeScene("/it/polimi/ingsw/am37/view/GUI/createGame.fxml", "create", createClick);
    }

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
                                selectedLobbyValue = lobbyList.getSelectionModel().getSelectedItem();
                                lobbySelected.setText(String.valueOf(selectedLobbyValue));
                            }
                        });
                    }
                });
                break;
            }
            case "ERROR": {
                waitingText.setText((String) evt.getNewValue());
                guiReference.setState(ViewState.CHOOSE_LOBBY);
                break;
            }
            case "CHANGED_STATE": {
                if(evt.getNewValue().equals(ViewState.WAIT_IN_LOBBY)) {
                    Platform.runLater( () -> {
                        try {
                            changeScene("/it/polimi/ingsw/am37/view/GUI/lobby.fxml", "lobby", event);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else if (evt.getNewValue().equals(ViewState.PLACE_SC)) {
                    //LOAD THE OTHER VIEW
                } //else if DISCONNECTION load create - join page // or close everything if he was disconnected
                break;
            }
        }
    }


}
