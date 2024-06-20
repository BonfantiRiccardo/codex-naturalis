package it.polimi.ingsw.am37.view.GUI.controllers;

import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.view.ViewState;
import it.polimi.ingsw.am37.view.clientmodel.ClientSidePlayer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the controller for the chooseToken.fxml file
 * It allows the player to choose a token from the available ones and sends it to the server.
 */
public class ChooseTokenController extends GUIController implements PropertyChangeListener {
    /**
     * The tokenMenu is the menu that contains the available tokens
     */
    @FXML
    private MenuButton tokenMenu;
    /**
     * The infoText is the text that will be displayed to the player
     */
    @FXML
    private Text infoText;
    /**
     * The confirmButton is the button that the player will press to confirm his choice
     */
    @FXML
    private Button confirmButton;
    /**
     * The returnToLobby is the button that the player will press to return to the lobby
     */
    @FXML
    private Button returnToLobby;

    /**
     * The tokenSelected is the index of the token that the player has selected
     */
    private int tokenSelected = -1;
    /**
     * The tokenSent is the index of the token that the player has sent to the server
     */
    private int tokenSent;
    /**
     * The red is the index of the red token
     */
    private int red;
    /**
     * The blue is the index of the blue token
     */
    private int blue;
    /**
     * The yellow is the index of the yellow token
     */
    private int yellow;
    /**
     * The green is the index of the green token
     */
    private int green;
    /**
     * The event is the event that will be triggered when the player presses the confirmButton
     */
    private ActionEvent event = new ActionEvent(confirmButton, null);


    /**
     * This method initializes the controller
     */
    public void initialize(){
        returnToLobby.setVisible(false);

        int i = -1;
        for (Token option : guiReference.getLocalGameInstance().getTokens()) {
            i++;
            MenuItem menuItem = new MenuItem();
            switch (option){
                case Token.RED: {
                    menuItem.setText("Red");
                    red = i;
                    menuItem.setOnAction(event -> { tokenSelected = red;
                                                    tokenMenu.setText("Red");
                                                  });
                    break;
                }

                case Token.BLUE: {
                    menuItem.setText("Blue");
                    blue = i;
                    menuItem.setOnAction(event -> { tokenSelected = blue;
                                                    tokenMenu.setText("Blue");
                                                  });
                    break;
                }

                case Token.YELLOW: {
                    menuItem.setText("Yellow");
                    yellow = i;
                    menuItem.setOnAction(event -> { tokenSelected = yellow;
                                                    tokenMenu.setText("Yellow");
                                                  });
                    break;
                }

                case Token.GREEN: {
                    menuItem.setText("Green");
                    green = i;
                    menuItem.setOnAction(event -> { tokenSelected = green;
                                                    tokenMenu.setText("Green");
                                                  });
                    break;
                }
            }
            tokenMenu.getItems().add(menuItem);
        }
    }

    /**
     * This method is called when the player presses the confirmButton
     * It sends the token to the server
     * @param actionEvent event fired by the click on the button
     */
    public void onConfirmClick(ActionEvent actionEvent) {
        if (tokenSelected == red) {
            guiReference.chooseToken(Token.RED);
            event = actionEvent;
            tokenSent = tokenSelected;
        } else if (tokenSelected == blue) {
            guiReference.chooseToken(Token.BLUE);
            event = actionEvent;
            tokenSent = tokenSelected;
        } else if (tokenSelected == yellow) {
            guiReference.chooseToken(Token.YELLOW);
            event = actionEvent;
            tokenSent = tokenSelected;
        } else if (tokenSelected == green) {
            guiReference.chooseToken(Token.GREEN);
            event = actionEvent;
            tokenSent = tokenSelected;
        } else {
            infoText.setText("You have not chosen a token yet. Please select one.");
        }
    }

    /**
     * This method is called when the player presses the returnToLobby button
     * It returns the player to the login screen
     * @param actionEvent event fired by the click on the button
     */
    public void onReturnToLobbyClick(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            try {
                changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/login.fxml", "login", actionEvent);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * This method handles the property changes
     * @param evt property change event
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "CHANGED_STATE": {
                if (evt.getNewValue().equals(ViewState.CHOOSE_OBJECTIVE)) {
                    if (tokenSent == red)
                            guiReference.getLocalGameInstance().getMe().setToken(Token.RED);
                    else if (tokenSent == blue)
                            guiReference.getLocalGameInstance().getMe().setToken(Token.BLUE);
                    else if (tokenSent == yellow)
                            guiReference.getLocalGameInstance().getMe().setToken(Token.YELLOW);
                    else if (tokenSent == green)
                            guiReference.getLocalGameInstance().getMe().setToken(Token.GREEN);

                    Platform.runLater(() -> confirmButton.setVisible(false));
                    Platform.runLater(() -> tokenMenu.setVisible(false));
                    checkOtherPlayers();

                } else if (evt.getNewValue().equals(ViewState.DISCONNECTION)) {
                    Platform.runLater(() -> {
                        infoText.setText("One of the player was disconnected, the game ended for everyone.");
                        returnToLobby.setVisible(true);
                        confirmButton.setVisible(false);
                        tokenMenu.setVisible(false);
                    });
                }
                break;
            }
            case "TOKEN_REMOVED": {

                switch ((Token) evt.getOldValue()) {
                    case Token.RED: {
                        Platform.runLater(() -> tokenMenu.getItems().remove(red));
                        break;
                    }
                    case Token.BLUE: {
                        Platform.runLater(() -> tokenMenu.getItems().remove(blue));
                        break;
                    }
                    case Token.YELLOW: {
                        Platform.runLater(() -> tokenMenu.getItems().remove(yellow));
                        break;
                    }
                    case Token.GREEN: {
                        Platform.runLater(() -> tokenMenu.getItems().remove(green));
                        break;
                    }
                }
                checkOtherPlayers();
                break;
            }
        }
    }

    /**
     * This method checks if the other players have chosen their tokens
     */
    private void checkOtherPlayers() {
        List<String> stillToPlay = new ArrayList<>();
        for (ClientSidePlayer p: guiReference.getLocalGameInstance().getPlayers())
            if (p.getToken() == null) {
                stillToPlay.add(p.getNickname());
            }

        if (stillToPlay.isEmpty() && guiReference.getLocalGameInstance().getMe().getToken() != null) {
            Platform.runLater(() -> {
                try {
                    changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/choosePrivateObjective.fxml", "objective", event);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } else if (guiReference.getLocalGameInstance().getMe().getToken() != null) {
            Platform.runLater(() -> {
                infoText.setText("Waiting for: " + stillToPlay.toString() + " to choose their token");
            });
        }
    }

}
