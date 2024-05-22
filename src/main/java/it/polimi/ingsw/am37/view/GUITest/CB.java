package it.polimi.ingsw.am37.view.GUITest;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class CB {
    // Controller logic for bbb.fxml, if any

    private void updateMenuButtonText(MenuItem selectedItem) {
        q1.setText(selectedItem.getText());
    }

    @FXML
    private MenuButton q1;

    @FXML
    private MenuItem q2;

    @FXML
    private MenuItem q3;

    @FXML
    private MenuItem q4;

    @FXML
    private Button button;

    @FXML
    public void buttonClik(){
        System.out.println("click");
    }

    @FXML
    public void initialize() {
        q2.setOnAction(event -> updateMenuButtonText(q2));
        q3.setOnAction(event -> updateMenuButtonText(q3));
        q4.setOnAction(event -> updateMenuButtonText(q4));
    }
}
