package it.polimi.ingsw.am37.view.GUI.controllers;

import it.polimi.ingsw.am37.view.clientmodel.ClientSidePlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.controlsfx.control.spreadsheet.Grid;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.Objects;

public class KingdomController extends GUIController implements PropertyChangeListener {

    //Public objectives and decks
    @FXML
    Image publicObj1;
    @FXML
    Image publicObj2;
    @FXML
    Button topResDeckButton;
    @FXML
    Image topResDeckImage;
    @FXML
    Button topGoldDeckButton;
    @FXML
    Image topGoldDeckImage;
    @FXML
    Button availableResCard1Button;
    @FXML
    Image availableResCard1Image;
    @FXML
    Button availableGoldCard1Button;
    @FXML
    Image availableGoldCard1Image;
    @FXML
    Button availableResCard2Button;
    @FXML
    Image availableResCard2Image;
    @FXML
    Button availableGoldCard2Button;
    @FXML
    Image availableGoldCard2Image;

    //player1Info
    @FXML
    GridPane player1Info;
    @FXML
    Text player1Username;
    @FXML
    Text player1Points;
    @FXML
    Text player1AnimalPoints;
    @FXML
    Text player1FungiPoints;
    @FXML
    Text player1InsectPoints;
    @FXML
    Text player1PlantPoints;
    @FXML
    Text player1InkwellPoints;
    @FXML
    Text player1ManuscriptPoints;
    @FXML
    Text player1QuillPoints;


    //player2Info
    @FXML
    GridPane player2Info;
    @FXML
    Text player2Username;
    @FXML
    Text player2Points;
    @FXML
    Text player2AnimalPoints;
    @FXML
    Text player2FungiPoints;
    @FXML
    Text player2InsectPoints;
    @FXML
    Text player2PlantPoints;
    @FXML
    Text player2InkwellPoints;
    @FXML
    Text player2ManuscriptPoints;
    @FXML
    Text player2QuillPoints;

    //player4Info
    @FXML
    GridPane player3Info;
    @FXML
    Text player3Username;
    @FXML
    Text player3Points;
    @FXML
    Text player3AnimalPoints;
    @FXML
    Text player3FungiPoints;
    @FXML
    Text player3InsectPoints;
    @FXML
    Text player3PlantPoints;
    @FXML
    Text player3InkwellPoints;
    @FXML
    Text player3ManuscriptPoints;
    @FXML
    Text player3QuillPoints;

    //player4Info
    @FXML
    GridPane player4Info;
    @FXML
    Text player4Username;
    @FXML
    Text player4Points;
    @FXML
    Text player4AnimalPoints;
    @FXML
    Text player4FungiPoints;
    @FXML
    Text player4InsectPoints;
    @FXML
    Text player4PlantPoints;
    @FXML
    Text player4InkwellPoints;
    @FXML
    Text player4ManuscriptPoints;
    @FXML
    Text player4QuillPoints;

    //kindoms
    @FXML
    Grid player1KingdomGrid;
    @FXML
    Tab player1Tab;
    @FXML
    Tab player2Tab;
    @FXML
    Grid player2KingdomGrid;
    @FXML
    Tab player3Tab;
    @FXML
    Grid player3KingdomGrid;
    @FXML
    Tab player4Tab;
    @FXML
    Grid player4KingdomGrid;

    //Player's hand
     @FXML
     Button handRes1Button;
     @FXML
     Image handRes1Image;
     @FXML
     Button handRes1Flip;

     @FXML
     Button handRes2Button;
     @FXML
     Image handRes2Image;
     @FXML
     Button handRes2Flip;

     @FXML
     Button handGoldButton;
     @FXML
     Image handGoldImage;
     @FXML
     Button handGoldFlip;

     //private objective
    @FXML
    Image privateObj;

    public void setPublicObjective1(int publicObjective1) {
    //    player1KingdomGrid.add(new Button(), 0, 0);
        this.publicObj2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"
                                                                            + publicObjective1 + ".png")));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    public void onLoad() {
        publicObj1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"
                                                         + guiReference.getLocalGameInstance().getPublicObjectives().get(0).getId())) + ".png");
        publicObj2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"
                                                         + guiReference.getLocalGameInstance().getPublicObjectives().get(1).getId())) + ".png");
    }
}
