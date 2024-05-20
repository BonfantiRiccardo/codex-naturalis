package it.polimi.ingsw.am37.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIViewController {



    public void joinButtonClicked(javafx.event.ActionEvent joinClick) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUIViewApplication.class.getResource("joinGame.fxml"));
        Scene fxmlScene = new Scene(fxmlLoader.load(), 800, 500);
        Stage window = (Stage)((Node)joinClick.getSource()).getScene().getWindow();
        window.setScene(fxmlScene);
        window.show();
    }

    public void createButtonClicked(javafx.event.ActionEvent createClick) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUIViewApplication.class.getResource("creationGame.fxml"));
        Scene fxmlScene = new Scene(fxmlLoader.load(), 800, 500);
        Stage window = (Stage)((Node)createClick.getSource()).getScene().getWindow();
        window.setScene(fxmlScene);
        window.show();
    }
}
