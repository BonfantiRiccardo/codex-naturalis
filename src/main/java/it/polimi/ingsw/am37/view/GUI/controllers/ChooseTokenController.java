package it.polimi.ingsw.am37.view.GUI.controllers;

import it.polimi.ingsw.am37.model.player.Token;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ChooseTokenController extends GUIController implements PropertyChangeListener {
    @FXML
    private MenuButton tokenMenu;
    @FXML
    private Text errorText;
    @FXML
    private Button confirmButton;

    private int tokenSelected;



    public void initialize(){
        for (Token option : guiReference.getLocalGameInstance().getTokens()) {
            MenuItem menuItem = new MenuItem();
            menuItem.setOnAction(event -> {
                switch (option){
                    case Token.RED:
                        menuItem.setText("Red");
                        tokenSelected=1;
                        tokenMenu.setText("Red");
                        break;
                    case Token.BLUE:
                        menuItem.setText("Blue");
                        tokenSelected=2;
                        tokenMenu.setText("Blue");
                        break;
                    case Token.YELLOW:
                        menuItem.setText("Yellow");
                        tokenSelected=3;
                        tokenMenu.setText("Yellow");
                        break;
                    case Token.GREEN:
                        menuItem.setText("Green");
                        tokenSelected=4;
                        tokenMenu.setText("Green");
                        break;
                    default:
                        menuItem.setText("Error");
                        tokenSelected=0;
                        tokenMenu.setText("Error");
                        break;
                }
            });
            tokenMenu.getItems().add(menuItem);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }
}
