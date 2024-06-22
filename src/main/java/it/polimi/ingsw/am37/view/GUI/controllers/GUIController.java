package it.polimi.ingsw.am37.view.GUI.controllers;

import it.polimi.ingsw.am37.view.GUI.GUIView;
import it.polimi.ingsw.am37.view.ViewState;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class is the superclass of all the controllers of the GUI.
 * It handles the change of scene.
 */
public abstract class GUIController {
    /**
     * The reference to the GUIView object.
     */
    protected static GUIView guiReference;

    /**
     * Sets the guiReference attribute to the given GUIView object.
     * @param guiRef the GUIView object to set as reference
     */
    public static void setGuiReference(GUIView guiRef) { guiReference = guiRef; }

    /**
     * Changes the scene to the one specified by the fxmlName and controller parameters.
     * @param fxmlName the name of the fxml file to load
     * @param controller the name of the controller to load
     * @param event the event that triggered the change of scene
     * @throws IOException if the fxml file is not found
     */
    public void changeScene(String fxmlName, String controller, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
        Parent root = loader.load();

        switch (controller) {
            case "login": {
                if (guiReference.getState().equals(ViewState.DISCONNECTION)) {
                    LoginController loginController = loader.getController();
                    loginController.setDisconnectionText("One of the player was disconnected, the game ended for everyone.");
                    guiReference.resetAfterDisconnection();
                }
                guiReference.setState(ViewState.CREATE_JOIN);
                break;
            }
            case "create": {
                CreateGameController createGameController = loader.getController();
                guiReference.getLocalGameInstance().setListener(createGameController);
                break;
            }

            case "join": {
                JoinGameController joinGameController = loader.getController();
                guiReference.getLocalGameInstance().setListener(joinGameController);
                guiReference.setState(ViewState.CHOOSE_LOBBY);
                guiReference.getLobbies();
                break;
            }

            case "lobby": {
                LobbyController lobbyController = loader.getController();
                guiReference.getLocalGameInstance().setListener(lobbyController);

                lobbyController.onLoad();
                break;
            }

            case "startCard": {
                ChooseStartCardController chooseStartCardController = loader.getController();
                guiReference.getLocalGameInstance().setListener(chooseStartCardController);

                chooseStartCardController.onLoad();
                break;
            }

            case "token": {
                ChooseTokenController chooseTokenController = loader.getController();
                guiReference.getLocalGameInstance().setListener(chooseTokenController);

                break;
            }

            case "objective": {
                ChoosePrivateObjectiveController choosePrivateObjectiveController = loader.getController();
                guiReference.getLocalGameInstance().setListener(choosePrivateObjectiveController);

                choosePrivateObjectiveController.onLoad();
                break;
            }

            case "kingdom": {
                KingdomController kingdomController = loader.getController();
                guiReference.getLocalGameInstance().setListener(kingdomController);

                kingdomController.onLoad();
                break;
            }

            case "results": {
                ShowResultsController showResultsController = loader.getController();
                showResultsController.loadResults();
                break;
            }

        }

        double x = ((Node) event.getSource()).getScene().getWidth();      //Open the new scene with the same size
        double y = ((Node) event.getSource()).getScene().getHeight();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root, x, y));
        stage.show();
    }
}
