package it.polimi.ingsw.am37.view.GUI.controllers;

import it.polimi.ingsw.am37.model.player.Token;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class PreparationPhaseController {
    @FXML
    private Button front;
    @FXML
    private Button back;
    @FXML
    private Button obj1;
    @FXML
    private Button obj2;
    @FXML
    private Button confirm;
    @FXML
    private MenuButton tokenList;
    @FXML
    private ImageView frontStartCard;
    @FXML
    private ImageView backStartCard;
    @FXML
    private ImageView objective1;
    @FXML
    private ImageView objective2;

    private List<String> availableToken = List.of("Red", "Blue", "Yellow", "Green");
    private int sideSelected;   //0 se ha selezionato front, 1 back
    private int objSelected;    //1 se ha selezionato obj1, 2 se ha selezioneto obj2
    private int tokenSelected;  //1 red, 2 blue, 3 yellow, 4 green

    public void frontClicked(){
        sideSelected=0;
    }
    public void backClicked(){
        sideSelected=1;
    }
    public void obj1Clicked(){
        objSelected=1;
    }
    public void obj2Clicked(){
        objSelected=2;
    }
    @FXML
    public void initialize() {
        for (String option : availableToken) {
            MenuItem menuItem = new MenuItem(option);
            menuItem.setOnAction(event -> {
                switch (option){
                    case "Red":
                        tokenSelected=1;
                        break;
                    case "Blue":
                        tokenSelected=2;
                        break;
                    case"Yellow":
                        tokenSelected=3;
                        break;
                    case "Green":
                        tokenSelected=4;
                        break;
                    default:
                        tokenSelected=0;
                        break;
                }
                tokenList.setText(option);
            });
            tokenList.getItems().add(menuItem);
        }
    }
    public void confirmClicked(){
        //cambia view a gioco
    }



}
