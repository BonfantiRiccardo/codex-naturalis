package it.polimi.ingsw.am37.view.GUI.controllers;

import it.polimi.ingsw.am37.view.ViewState;
import it.polimi.ingsw.am37.view.clientmodel.ClientSidePlayer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class LobbyController extends GUIController implements PropertyChangeListener {
    @FXML
    private Text playerName;
    @FXML
    private Text lobbyNumber;
    @FXML
    private Text namesOfPlayers;
    @FXML
    private Text playersNeededToStart;
    @FXML
    private Text playersText;
    @FXML
    private Button continueButton;
    @FXML
    private Button disconnection;

    public void onLoad(){
        playerName.setText(guiReference.getLocalGameInstance().getMe().getNickname());
        lobbyNumber.setText(String.valueOf(guiReference.getLocalGameInstance().getNumOfLobby()));

        if (guiReference.getLocalGameInstance().getNumOfPlayers() - 1 - guiReference.getLocalGameInstance().getPlayers().size() == 0) {
            playersText.setText("The game is ready to start. Click on continue to start the game!");
        } else {
            continueButton.setVisible(false);
        }
        disconnection.setVisible(false);

        getInfo();
    }

    public void getInfo() {
        playersNeededToStart.setText(String.valueOf((guiReference.getLocalGameInstance().getNumOfPlayers()
                - 1 - guiReference.getLocalGameInstance().getPlayers().size())));

        StringBuilder text = new StringBuilder();
        if (!guiReference.getLocalGameInstance().getPlayers().isEmpty()) {
            text.append("These players have joined: ");
            int count = guiReference.getLocalGameInstance().getPlayers().size();
            for(ClientSidePlayer p: guiReference.getLocalGameInstance().getPlayers()) {
                text.append(p.getNickname());
                if (count > 1) {
                    text.append(", ");
                    count--;
                }
            }
        } else
            text.append("No player joined yet");

        namesOfPlayers.setText(String.valueOf(text));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "NEW_PLAYER": {
                //LOAD THE OTHER VIEW IF PLAYERS REACHED
                Platform.runLater(this::getInfo);
                break;
            }

            case "CHANGED_STATE": {
                if (evt.getNewValue().equals(ViewState.PLACE_SC)) {
                    Platform.runLater(() -> {
                        playersText.setText("The game is ready to start. Click on continue to start the game!");
                        continueButton.setVisible(true);
                    });
                } else if (evt.getNewValue().equals(ViewState.DISCONNECTION)) {
                    Platform.runLater(() -> {
                        continueButton.setVisible(false);
                        playersText.setText("One of the player was disconnected, the game ended for everyone.");
                        disconnection.setVisible(true);
                    });
                }
            }
            break;
        }
    }

    public void onContinueClick(ActionEvent actionEvent) {
        if (guiReference.getState().equals(ViewState.PLACE_SC)) {
            Platform.runLater(() -> {
                try {
                    changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/chooseStartCard.fxml", "startCard", actionEvent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void onReturnToLobbyClicked(ActionEvent actionEvent) {
        if (guiReference.getState().equals(ViewState.DISCONNECTION)) {
            Platform.runLater(() -> {
                try {
                    changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/login.fxml", "login", actionEvent);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
