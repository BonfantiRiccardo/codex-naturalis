package it.polimi.ingsw.am37.view.GUITest;

import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;



public class PrimoController {

    @FXML
    private Button aaa;

    @FXML
    public void buttonClick(ActionEvent event) throws IOException {
        System.out.println("click");

        FXMLLoader loader=new FXMLLoader(getClass().getResource("secondo.fxml"));
        Parent root = loader.load();
        Stage stage= (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }
}
