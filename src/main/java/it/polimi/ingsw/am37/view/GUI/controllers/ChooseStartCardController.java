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

/**
 * Controller for the choose start card scene.
 */
public class ChooseStartCardController extends GUIController implements PropertyChangeListener {
    /**
     * The confirm button.
     */
    @FXML
    private Button confirmButton;
    /**
     * The front button.
     */
    @FXML
    private Button frontButton;
    /**
     * The back button.
     */
    @FXML
    private Button backButton;
    /**
     * Text box to display information.
     */
    @FXML
    private Text infoText;
    /**
     * The front start card image.
     */
    @FXML
    private ImageView frontStartCard;
    /**
     * The back start card image.
     */
    @FXML
    private ImageView backStartCard;
    /**
     * The top resource deck image.
     */
    @FXML
    private ImageView topResourceDeck;
    /**
     * The first resource card image.
     */
    @FXML
    private ImageView resourceCard1;
    /**
     * The second resource card image.
     */
    @FXML
    private ImageView resourceCard2;
    /**
     * The top gold deck image.
     */
    @FXML
    private ImageView topGoldDeck;
    /**
     * The first gold card image.
     */
    @FXML
    private ImageView goldCard1;
    /**
     * The second gold card image.
     */
    @FXML
    private ImageView goldCard2;
    /**
     * The return to lobby button.
     */
    @FXML
    private Button returnToLobby;

    /**
     * The selected side of the card.
     */
    private String selectedSide;
    /**
     * The side of the card sent to the server.
     */
    private String sideSent;
    /**
     * The event that triggered the confirm button.
     */
    private ActionEvent event = new ActionEvent(goldCard1, null);

    /**
     * Initializes the scene.
     */
    public void onLoad() {
        returnToLobby.setVisible(false);

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

    /**
     * Handles the front button click.
     * @param actionEvent the event that triggered the action.
     */
    public void onFrontClick(ActionEvent actionEvent) {
        infoText.setText("You have chosen the front side of the card. Press confirm to continue");
        selectedSide = "f";
    }

    /**
     * Handles the back button click.
     * @param actionEvent the event that triggered the action.
     */
    public void onBackClick(ActionEvent actionEvent) {
        infoText.setText("You have chosen the back side of the card. Press confirm to continue");
        selectedSide = "b";
    }

    /**
     * Handles the confirm button click.
     * @param actionEvent the event that triggered the action.
     */
    public void onConfirmClick(ActionEvent actionEvent) {
        sideSent = selectedSide;
        guiReference.chooseStartCard(selectedSide);
        event = actionEvent;
    }

    /**
     * Handles the return to lobby button click.
     * @param actionEvent the event that triggered the action.
     */
    public void onReturnToLobby(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            try {
                changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/login.fxml", "login", actionEvent);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Handles the property change event.
     * @param evt the event that triggered the action.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "CHANGED_STATE": {
                if (evt.getNewValue().equals(ViewState.CHOOSE_TOKEN)) {
                    if (sideSent.equalsIgnoreCase("f"))
                        guiReference.getLocalGameInstance().getMe().setKingdom(new Kingdom(guiReference.getLocalGameInstance().getMyStartCard(),
                                guiReference.getLocalGameInstance().getMyStartCard().getFront()));
                    else if (sideSent.equalsIgnoreCase("b"))
                        guiReference.getLocalGameInstance().getMe().setKingdom(new Kingdom(guiReference.getLocalGameInstance().getMyStartCard(),
                                guiReference.getLocalGameInstance().getMyStartCard().getBack()));
                    Platform.runLater(() -> {
                        confirmButton.setVisible(false);
                        frontButton.setVisible(false);
                        backButton.setVisible(false);
                    });
                    checkOtherPlayers();
                } else if (evt.getNewValue().equals(ViewState.DISCONNECTION)) {
                    infoText.setText("One or more players disconnected, press the button to return to the lobby");
                    Platform.runLater(() -> returnToLobby.setVisible(true));
                    Platform.runLater(() -> confirmButton.setVisible(false));
                } else if (evt.getNewValue().equals(ViewState.ERROR)) {
                    infoText.setText("Error: " + evt.getNewValue());
                    //ERROR STATE IS UNRECOVERABLE FOR NOW
                    //guiReference.setState((ViewState) evt.getOldValue());
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

    /**
     * Checks if other players have placed their start card.
     */
    private void checkOtherPlayers() {
        List<String> stillToPlay = new ArrayList<>();
        for (ClientSidePlayer p: guiReference.getLocalGameInstance().getPlayers())
            if (p.getKingdom() == null) {
                stillToPlay.add(p.getNickname());
            }

        if (stillToPlay.isEmpty() && guiReference.getLocalGameInstance().getMe().getKingdom() != null) {
            Platform.runLater(() -> {
                try {
                    changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/chooseToken.fxml", "token", event);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } else if (guiReference.getLocalGameInstance().getMe().getKingdom() != null) {
            Platform.runLater(() -> {
                infoText.setText("Waiting for: " + stillToPlay.toString() + " to place their start card");
            });
        }
    }
}
