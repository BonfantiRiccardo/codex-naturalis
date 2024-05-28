package it.polimi.ingsw.am37.view.GUI.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;

import java.util.Objects;

public class kingdomController {
    @FXML
    Image publicObjective1;

    public void setPublicObjective1(int publicObjective1) {
        this.publicObjective1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("it/polimi/ingsw/am37/view/GUI/front/" + publicObjective1 + ".png")));
    }
}
