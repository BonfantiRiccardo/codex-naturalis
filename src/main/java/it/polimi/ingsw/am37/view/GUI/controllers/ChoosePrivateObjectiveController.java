package it.polimi.ingsw.am37.view.GUI.controllers;

import it.polimi.ingsw.am37.view.ViewState;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Objects;

/**
 * Controller for the choose private objective scene.
 */
public class ChoosePrivateObjectiveController extends GUIController implements PropertyChangeListener {
    /**
     * The confirm button.
     */
    @FXML
    private Button confirmButton;
    /**
     * The first objective button.
     */
    @FXML
    private Button firstObjectiveButton;
    /**
     * The first objective image.
     */
    @FXML
    private ImageView firstObjective;
    /**
     * The second objective button.
     */
    @FXML
    private Button secondObjectiveButton;
    /**
     * The second objective image.
     */
    @FXML
    private ImageView secondObjective;
    /**
     * Text box to display information.
     */
    @FXML
    private Text infoText;

    /**
     * The top resource deck image.
     */
    @FXML
    private ImageView topResourceDeck;
    /**
     * The resource card 1 image.
     */
    @FXML
    private ImageView resourceCard1;
    /**
     * The resource card 2 image.
     */
    @FXML
    private ImageView resourceCard2;
    /**
     * The top gold deck image.
     */
    @FXML
    private ImageView topGoldDeck;
    /**
     * The gold card 1 image.
     */
    @FXML
    private ImageView goldCard1;
    /**
     * The gold card 2 image.
     */
    @FXML
    private ImageView goldCard2;
    /**
     * The player card 1 image.
     */
    @FXML
    private ImageView playerCard1;
    /**
     * The player card 2 image.
     */
    @FXML
    private ImageView playerCard2;
    /**
     * The player card 3 image.
     */
    @FXML
    private ImageView playerCard3;

    /**
     * The return to lobby button.
     */
    @FXML
    private Button returnToLobby;

    /**
     * The selected objective.
     */
    private int objectiveSelected = -1;
    /**
     * The objective sent to the server.
     */
    private int objectiveSent;
    /**
     * The event to be triggered.
     */
    private ActionEvent event = new ActionEvent(confirmButton, null);


    /**
     * Initializes the scene.
     */
    public void onLoad() {
        returnToLobby.setVisible(false);

        firstObjective.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"+
                        guiReference.getLocalGameInstance().getPrivateObjectives().get(0).getId()
                        + ".png"))));

        secondObjective.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"+
                        guiReference.getLocalGameInstance().getPrivateObjectives().get(1).getId()
                        + ".png"))));

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

        playerCard1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"+
                        guiReference.getLocalGameInstance().getMyHand().get(0).getId()
                        + ".png"))));

        playerCard2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"+
                        guiReference.getLocalGameInstance().getMyHand().get(1).getId()
                        + ".png"))));

        playerCard3.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"+
                        guiReference.getLocalGameInstance().getMyHand().get(2).getId()
                        + ".png"))));
    }

    /**
     * Handles the first objective button click.
     * @param actionEvent the event to be handled.
     */
    public void onFirstButtonClick(ActionEvent actionEvent) {
        objectiveSelected = 1;
        infoText.setText("You have selected the first private objective. Press confirm to continue.");
    }

    /**
     * Handles the second objective button click.
     * @param actionEvent the event to be handled.
     */
    public void onSecondButtonClick(ActionEvent actionEvent) {
        objectiveSelected = 2;
        infoText.setText("You have selected the second private objective. Press confirm to continue.");
    }

    /**
     * Handles the confirm button click.
     * @param actionEvent the event to be handled.
     */
    public void onConfirmClick(ActionEvent actionEvent) {
        if (objectiveSelected == 1) {
            objectiveSent = objectiveSelected;
            event = actionEvent;
            guiReference.choosePrivateObjective(guiReference.getLocalGameInstance().getPrivateObjectives().get(0).getId());
        } else if (objectiveSelected == 2) {
            objectiveSent = objectiveSelected;
            event = actionEvent;
            guiReference.choosePrivateObjective(guiReference.getLocalGameInstance().getPrivateObjectives().get(1).getId());
        } else {
            infoText.setText("You have not chosen a private objective yet. Please select one.");
        }
    }

    /**
     * Handles the return to lobby button click.
     * @param actionEvent the event to be handled.
     */
    public void onReturnToLobbyClick(ActionEvent actionEvent) {
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
     * @param evt the event to be handled.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("CHANGED_STATE")) {
            if (evt.getNewValue().equals(ViewState.NOT_TURN)) {
                if (objectiveSent == 1) {
                    guiReference.getLocalGameInstance().setMyPrivateObjective(guiReference.getLocalGameInstance().getPrivateObjectives().get(0));
                } else if (objectiveSent == 2) {
                    guiReference.getLocalGameInstance().setMyPrivateObjective(guiReference.getLocalGameInstance().getPrivateObjectives().get(1));
                }

                Platform.runLater(() -> {
                    try {
                        changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/kingdom.fxml", "kingdom", event);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else if (evt.getNewValue().equals(ViewState.DISCONNECTION)) {
                Platform.runLater(() -> {
                    infoText.setText("One of the player was disconnected, the game ended for everyone.");
                    returnToLobby.setVisible(true);
                    confirmButton.setVisible(false);
                    firstObjectiveButton.setVisible(false);
                    secondObjectiveButton.setVisible(false);
                });
            }
        }
    }

}
