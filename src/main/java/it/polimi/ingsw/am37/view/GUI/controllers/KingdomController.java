package it.polimi.ingsw.am37.view.GUI.controllers;

import it.polimi.ingsw.am37.view.clientmodel.ClientSidePlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.Objects;

public class KingdomController extends GUIController implements PropertyChangeListener {
    @FXML
    Image publicObjective1;
    @FXML
    Image publicObjective2;
    @FXML
    GridPane gridPane;

    Map<ClientSidePlayer, GridPane> playerGrids;

    public void setPublicObjective1(int publicObjective1) {
        gridPane.add(new Button(), 0, 0);
        this.publicObjective1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"
                                                                            + publicObjective1 + ".png")));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    public void onLoad() {
        publicObjective1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"
                                                         + guiReference.getLocalGameInstance().getPublicObjectives().get(0).getId())) + ".png");
        publicObjective2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"
                                                         + guiReference.getLocalGameInstance().getPublicObjectives().get(1).getId())) + ".png");
    }
}
