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

public class CreateGameController extends GUIController implements PropertyChangeListener {
    @FXML
    private MenuButton pl_num;
    @FXML
    private MenuItem pl2;
    @FXML
    private MenuItem pl3;
    @FXML
    private MenuItem pl4;
    @FXML
    private Button createButton;
    @FXML
    private Button joinButton;
    @FXML
    private TextField nick;
    @FXML
    private Text infoText;

    private String nickname;
    private int numPlayers;
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
        changeScene("/it/polimi/ingsw/am37/view/GUI/joinGame.fxml", "join", joinClicked);
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
                            changeScene("/it/polimi/ingsw/am37/view/GUI/lobby.fxml", "lobby", event);
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
