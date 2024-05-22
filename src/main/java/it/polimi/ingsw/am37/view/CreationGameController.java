package it.polimi.ingsw.am37.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

public class CreationGameController {
    @FXML
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
    }
}
