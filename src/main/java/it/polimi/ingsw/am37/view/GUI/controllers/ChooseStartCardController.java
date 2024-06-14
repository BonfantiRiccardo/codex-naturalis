package it.polimi.ingsw.am37.view.GUI.controllers;

import it.polimi.ingsw.am37.model.player.Kingdom;
import it.polimi.ingsw.am37.view.ViewState;
import it.polimi.ingsw.am37.view.clientmodel.ClientSidePlayer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChooseStartCardController extends GUIController implements PropertyChangeListener {
    @FXML
    private Button confirmButton;
    @FXML
    private Button frontButton;
    @FXML
    private Button backButton;
    @FXML
    private Text infoText;
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


    private String selectedSide;
    private String sideSent;
    private ActionEvent event = new ActionEvent();

    public void onLoad() {
        String resource = "/it/polimi/ingsw/am37/view/GUI/back/" +
                switch (guiReference.getLocalGameInstance().getTopOfGoldDeck()) {
                    default -> "defaultBack";
                    case PLANT -> "PlantBackGold";
                    case ANIMAL -> "AnimalBackGold";
                    case FUNGI -> "FungiBackGold";
                    case INSECT -> "InsectBackGold";
                }
                + ".png";
        topGoldDeck.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(resource))));

        topResourceDeck.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/back/" +
                switch (guiReference.getLocalGameInstance().getTopOfResourceDeck()) {
                    default -> "defaultBack";
                    case PLANT -> "PlantBackResource";
                    case ANIMAL -> "AnimalBackResource";
                    case FUNGI -> "FungiBackResource";
                    case INSECT -> "InsectBackResource";
                }
                + ".png"))));

        goldCard1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                                "/it/polimi/ingsw/am37/view/GUI/front/"+
                                guiReference.getLocalGameInstance().getAvailableGoldCards().get(0).getId()
                                + ".png"))));
        goldCard2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                                "/it/polimi/ingsw/am37/view/GUI/front/"+
                                guiReference.getLocalGameInstance().getAvailableGoldCards().get(1).getId()
                                + ".png"))));

        resourceCard1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                                "/it/polimi/ingsw/am37/view/GUI/front/"+
                                 guiReference.getLocalGameInstance().getAvailableResourceCards().get(0).getId()
                                 + ".png"))));
        resourceCard2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                                "/it/polimi/ingsw/am37/view/GUI/front/"+
                                guiReference.getLocalGameInstance().getAvailableResourceCards().get(0).getId()
                                + ".png"))));

        frontStartCard.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                                "/it/polimi/ingsw/am37/view/GUI/front/" +
                                guiReference.getLocalGameInstance().getMyStartCard().getId()
                                + ".png"))));
        backStartCard.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                "/it/polimi/ingsw/am37/view/GUI/back/" +
                        guiReference.getLocalGameInstance().getMyStartCard().getId()
                        + ".png"))));
    }

    public void onFrontClick(ActionEvent actionEvent) {
        infoText.setText("You have chosen the front side of the card. Press confirm to continue");
        selectedSide = "f";
    }


    public void onBackClick(ActionEvent actionEvent) {
        infoText.setText("You have chosen the back side of the card. Press confirm to continue");
        selectedSide = "b";
    }


    public void onConfirmClick(ActionEvent actionEvent) {
        sideSent = selectedSide;
        guiReference.chooseStartCard(selectedSide);
        event = actionEvent;
    }



    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "CHANGED_STATE": {
                if (guiReference.getState().equals(ViewState.CHOOSE_TOKEN)) {
                    if (sideSent.equalsIgnoreCase("f"))
                        guiReference.getLocalGameInstance().getMe().setKingdom(new Kingdom(guiReference.getLocalGameInstance().getMyStartCard(),
                                guiReference.getLocalGameInstance().getMyStartCard().getFront()));
                    else if (sideSent.equalsIgnoreCase("b"))
                        guiReference.getLocalGameInstance().getMe().setKingdom(new Kingdom(guiReference.getLocalGameInstance().getMyStartCard(),
                                guiReference.getLocalGameInstance().getMyStartCard().getBack()));

                    checkOtherPlayers();
                } else if (guiReference.getState().equals(ViewState.DISCONNECTION)) {
                    Platform.runLater(() -> {
                        try {
                            changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/login.fxml", "login", event);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                break;
            }

            case "ERROR": {
                infoText.setText("Error: " + evt.getNewValue());

                guiReference.setState(ViewState.PLACE_SC);
                break;
            }

            case "START_CARD_PLACED": {
                checkOtherPlayers();
                break;
            }
        }
    }

    private void checkOtherPlayers() {
        List<String> stillToPlay = new ArrayList<>();
        for (ClientSidePlayer p: guiReference.getLocalGameInstance().getPlayers())
            if (p.getKingdom() == null) {
                stillToPlay.add(p.getNickname());
            }

        if (stillToPlay.isEmpty()) {
            Platform.runLater(() -> {
                try {
                    changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/chooseToken.fxml", "token", event);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            infoText.setText("Waiting for: " + stillToPlay.toString() + " to place their start card");
        }
    }

}
