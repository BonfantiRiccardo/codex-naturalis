package it.polimi.ingsw.am37.view.GUI.controllers;

import it.polimi.ingsw.am37.view.clientmodel.ClientSidePlayer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LobbyController extends GUIController implements PropertyChangeListener {
    @FXML
    private Text playerName;
    @FXML
    private Text lobbyNumber;
    @FXML
    private Text namesOfPlayers;
    @FXML
    private Text playersNeededToStart;

    public void onLoad(){
        playerName.setText(guiReference.getLocalGameInstance().getMe().getNickname());
        lobbyNumber.setText(String.valueOf(guiReference.getLocalGameInstance().getNumOfLobby()));
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
                Platform.runLater( () -> {
                    playersNeededToStart.setText(String.valueOf((guiReference.getLocalGameInstance().getNumOfPlayers()
                            - 1 - guiReference.getLocalGameInstance().getPlayers().size())));

                    StringBuilder text = new StringBuilder();
                    if (!guiReference.getLocalGameInstance().getPlayers().isEmpty()) {
                        text.append("These players have joined: ");
                        int count = guiReference.getLocalGameInstance().getPlayers().size();
                        for (ClientSidePlayer p : guiReference.getLocalGameInstance().getPlayers()) {
                            text.append(p.getNickname());
                            if (count > 1) {
                                text.append(", ");
                                count--;
                            }
                        }
                    } else
                        text.append("No player joined yet");

                    namesOfPlayers.setText(String.valueOf(text));
                });
            }
        }
    }
}
