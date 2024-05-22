package it.polimi.ingsw.am37.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIViewController {

   /* @FXML
    private MenuButton pl_num;
    @FXML
    private MenuItem pl2;
    @FXML
    private MenuItem pl3;
    @FXML
    private MenuItem pl4;


   @FXML
    public void initialize() {
        pl2.setOnAction(event -> updateMenuButtonText(pl2));
        pl3.setOnAction(event -> updateMenuButtonText(pl3));
        pl4.setOnAction(event -> updateMenuButtonText(pl4));
    }

    private void updateMenuButtonText(MenuItem selectedItem) {
        pl_num.setText(selectedItem.getText());
    }*/

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

        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("bbb.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) joinClick.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 500));
        stage.show();*/
    }

    public void createButtonClicked(javafx.event.ActionEvent createClick) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUIViewApplication.class.getResource("loginPhase/creationGame.fxml"));
        Scene fxmlScene = new Scene(fxmlLoader.load(), 800, 500);
        Stage window = (Stage)((Node)createClick.getSource()).getScene().getWindow();
        window.setScene(fxmlScene);
        window.show();

        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("loginPhase/creationGame.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) createClick.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 500));
        stage.show();*/

    }
}
