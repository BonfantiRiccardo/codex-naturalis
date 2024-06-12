package it.polimi.ingsw.am37.view.GUI.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ChoosePrivateObjectiveController extends GUIController implements PropertyChangeListener {
    @FXML
    private Button confirmButton;
    @FXML
    private Button frontButton;
    @FXML
    private Button backButton;
    @FXML
    private Text errorText;
    @FXML
    private ImageView frontStartCard;
    @FXML
    private ImageView backStartCard;
    @FXML
    private ImageView topResourceDeck;
    @FXML
    private ImageView resourceCard1;
    @FXML
    private ImageView resourceCard2;
    @FXML
    private ImageView topGoldDeck;
    @FXML
    private ImageView goldCard1;
    @FXML
    private ImageView goldCard2;
    @FXML
    private ImageView playerResourceCard1;
    @FXML
    private ImageView playerResourceCard2;
    @FXML
    private ImageView playerGoldCard;


    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
