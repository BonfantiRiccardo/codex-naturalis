package it.polimi.ingsw.am37.view.GUI;

import it.polimi.ingsw.am37.view.GUI.GUIViewApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    public void joinButtonClicked(javafx.event.ActionEvent joinClick) throws IOException {
        /*FXMLLoader fxmlLoader = new FXMLLoader(GUIViewApplication.class.getResource("loginPhase/joinGame.fxml"));
        Scene fxmlScene = new Scene(fxmlLoader.load(), 800, 500);
        Stage window = (Stage)((Node)joinClick.getSource()).getScene().getWindow();
        window.setScene(fxmlScene);
        window.show();

        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("loginPhase/joinGame.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) joinClick.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 500));
        stage.show();*/

        FXMLLoader loader = new FXMLLoader(getClass().getResource("joinGame.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) joinClick.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }

    public void createButtonClicked(javafx.event.ActionEvent createClick) throws IOException {
        /*FXMLLoader fxmlLoader = new FXMLLoader(GUIViewApplication.class.getResource("GUI/createGame.fxml"));
        Scene fxmlScene = new Scene(fxmlLoader.load(), 800, 500);
        Stage window = (Stage)((Node)createClick.getSource()).getScene().getWindow();
        window.setScene(fxmlScene);
        window.show();*/

        FXMLLoader loader = new FXMLLoader(getClass().getResource("createGame.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) createClick.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 500));
        stage.show();

    }
}
