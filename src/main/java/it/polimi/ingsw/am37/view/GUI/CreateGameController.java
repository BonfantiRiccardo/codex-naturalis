package it.polimi.ingsw.am37.view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateGameController {
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
    private TextField nick;

    private String nickname;
    private int numPlayers;


    @FXML
    public void initialize() {
        pl2.setOnAction(event -> updateMenuButtonText(pl2));
        pl3.setOnAction(event -> updateMenuButtonText(pl3));
        pl4.setOnAction(event -> updateMenuButtonText(pl4));
    }

    private void updateMenuButtonText(MenuItem selectedItem) {
        pl_num.setText(selectedItem.getText());
    }

    public void createButtonClicked(ActionEvent event) throws IOException {
        /*FXMLLoader loader = new FXMLLoader(getClass().getResource(""));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 500));
        stage.show();*/

        nickname=nick.getText();
        System.out.println(nickname);

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
}
