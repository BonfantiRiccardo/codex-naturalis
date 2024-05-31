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

public abstract class GUIController {
    protected static GUIView guiReference;

    public static void setGuiReference(GUIView guiRef) { guiReference = guiRef; }

    public void changeScene(String fxmlName, String controller, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
        Parent root = loader.load();

        switch (controller) {
            case "login": {
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

            case "initialization": {

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

            case "game": {
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
