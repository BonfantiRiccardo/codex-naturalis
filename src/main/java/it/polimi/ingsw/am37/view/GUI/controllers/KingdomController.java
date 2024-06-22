package it.polimi.ingsw.am37.view.GUI.controllers;

import it.polimi.ingsw.am37.model.cards.placeable.GameCard;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.model.sides.Side;
import it.polimi.ingsw.am37.view.ViewState;
import it.polimi.ingsw.am37.view.clientmodel.ClientSidePlayer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * This class is the controller for the Kingdom.fxml file.
 * It manages the GUI of the Kingdom phase of the game.
 */
public class KingdomController extends GUIController implements PropertyChangeListener {
    /**
     * button to return to the lobby
     */
    @FXML
    public Button returnToLobby;
    /**
     * text to show information
     */
    @FXML
    public Text infoText;
    /**
     * button to show the results
     */
    @FXML
    public Button resultsButton;

    //PUBLIC OBJECTIVES
    /**
     * image of the first public objective
     */
    @FXML
    private ImageView publicObj1;
    /**
     * image of the second public objective
     */
    @FXML
    private ImageView publicObj2;

    //DECKS AND AVAILABLE CARDS
    /**
     * button to draw from the resource deck
     */
    @FXML
    private Button topResDeckButton;
    /**
     * image of the top card of the resource deck
     */
    @FXML
    private ImageView topResDeckImage;

    /**
     * button to draw the first available resource card
     */
    @FXML
    private Button availResCard1Button;
    /**
     * image of the first available resource card
     */
    @FXML
    private ImageView availResCard1;
    /**
     * button to draw the second available resource card
     */
    @FXML
    private Button availResCard2Button;
    /**
     * image of the second available resource card
     */
    @FXML
    private ImageView availResCard2;

    /**
     * button to draw from the gold deck
     */
    @FXML
    private Button topGoldDeckButton;
    /**
     * image of the top card of the gold deck
     */
    @FXML
    private ImageView topGoldDeckImage;

    /**
     * button to draw the first available gold card
     */
    @FXML
    private Button availGoldCard1Button;
    /**
     * image of the first available gold card
     */
    @FXML
    private ImageView availGoldCard1;
    /**
     * button to draw the second available gold card
     */
    @FXML
    private Button availGoldCard2Button;
    /**
     * image of the second available gold card
     */
    @FXML
    private ImageView availGoldCard2;

    //HAND CARDS
    /**
     * button to select the first card of the hand
     */
    @FXML
    private Button handCard1Button;
    /**
     * image of the first card of the hand
     */
    @FXML
    private ImageView handCard1;
    /**
     * button to flip the first card of the hand
     */
    @FXML
    private Button flipCard1Button;
    /**
     * button to select the second card of the hand
     */
    @FXML
    private Button handCard2Button;
    /**
     * image of the second card of the hand
     */
    @FXML
    private ImageView handCard2;
    /**
     * button to flip the second card of the hand
     */
    @FXML
    private Button flipCard2Button;
    /**
     * button to select the third card of the hand
     */
    @FXML
    private Button handCard3Button;
    /**
     * image of the third card of the hand
     */
    @FXML
    private ImageView handCard3;
    /**
     * button to flip the third card of the hand
     */
    @FXML
    private Button flipCard3Button;

    //PRIVATE OBJECTIVE
    /**
     * image of the private objective
     */
    @FXML
    private ImageView privateObjective;

    //PLAYER GRIDS AND TABS
    /**
     * tab of the first player
     */
    @FXML
    private Tab player1Tab;
    /**
     * tab of the second player
     */
    @FXML
    private Tab player2Tab;
    /**
     * tab of the third player
     */
    @FXML
    private Tab player3Tab;
    /**
     * tab of the fourth player
     */
    @FXML
    private Tab player4Tab;

    /**
     * grid of the first player
     */
    @FXML
    private GridPane player1Grid;
    /**
     * grid of the second player
     */
    @FXML
    private GridPane player2Grid;
    /**
     * grid of the third player
     */
    @FXML
    private GridPane player3Grid;
    /**
     * grid of the fourth player
     */
    @FXML
    private GridPane player4Grid;

    //PLAYER INFOS
    /**
     * Box of players info
     */
    @FXML
    private HBox leftHBox;
    /**
     * Grid of the first player info
     */
    @FXML
    private GridPane p1Info;
    /**
     * Grid of the second player info
     */
    @FXML
    private GridPane p2Info;
    /**
     * Grid of the third player info
     */
    @FXML
    private GridPane p3Info;
    /**
     * Grid of the fourth player info
     */
    @FXML
    private GridPane p4Info;

    /**
     * Name of the first player
     */
    @FXML
    private Text p1Name;
    /**
     * Name of the second player
     */
    @FXML
    private Text p2Name;
    /**
     * Name of the third player
     */
    @FXML
    private Text p3Name;
    /**
     * Name of the fourth player
     */
    @FXML
    private Text p4Name;

    /**
     * Points of the first player
     */
    @FXML
    private Text p1Points;
    /**
     * Points of the second player
     */
    @FXML
    private Text p2Points;
    /**
     * Points of the third player
     */
    @FXML
    private Text p3Points;
    /**
     * Points of the fourth player
     */
    @FXML
    private Text p4Points;

    /**
     * Animal resources of the first player
     */
    @FXML
    private Text p1Animal;
    /**
     * Animal resources of the second player
     */
    @FXML
    private Text p2Animal;
    /**
     * Animal resources of the third player
     */
    @FXML
    private Text p3Animal;
    /**
     * Animal resources of the fourth player
     */
    @FXML
    private Text p4Animal;

    /**
     * Plant resources of the first player
     */
    @FXML
    private Text p1Plant;
    /**
     * Plant resources of the second player
     */
    @FXML
    private Text p2Plant;
    /**
     * Plant resources of the third player
     */
    @FXML
    private Text p3Plant;
    /**
     * Plant resources of the fourth player
     */
    @FXML
    private Text p4Plant;

    /**
     * Fungi resources of the first player
     */
    @FXML
    private Text p1Fungi;
    /**
     * Fungi resources of the second player
     */
    @FXML
    private Text p2Fungi;
    /**
     * Fungi resources of the third player
     */
    @FXML
    private Text p3Fungi;
    /**
     * Fungi resources of the fourth player
     */
    @FXML
    private Text p4Fungi;

    /**
     * Insect resources of the first player
     */
    @FXML
    private Text p1Insect;
    /**
     * Insect resources of the second player
     */
    @FXML
    private Text p2Insect;
    /**
     * Insect resources of the third player
     */
    @FXML
    private Text p3Insect;
    /**
     * Insect resources of the fourth player
     */
    @FXML
    private Text p4Insect;

    /**
     * Inkwell resources of the first player
     */
    @FXML
    private Text p1Inkwell;
    /**
     * Inkwell resources of the second player
     */
    @FXML
    private Text p2Inkwell;
    /**
     * Inkwell resources of the third player
     */
    @FXML
    private Text p3Inkwell;
    /**
     * Inkwell resources of the fourth player
     */
    @FXML
    private Text p4Inkwell;

    /**
     * Manuscript resources of the first player
     */
    @FXML
    private Text p1Manuscript;
    /**
     * Manuscript resources of the second player
     */
    @FXML
    private Text p2Manuscript;
    /**
     * Manuscript resources of the third player
     */
    @FXML
    private Text p3Manuscript;
    /**
     * Manuscript resources of the fourth player
     */
    @FXML
    private Text p4Manuscript;

    /**
     * Quill resources of the first player
     */
    @FXML
    private Text p1Quill;
    /**
     * Quill resources of the second player
     */
    @FXML
    private Text p2Quill;
    /**
     * Quill resources of the third player
     */
    @FXML
    private Text p3Quill;
    /**
     * Quill resources of the fourth player
     */
    @FXML
    private Text p4Quill;



    /**
     * Side shown of the first card of the hand
     */
    private String card1SideShown;
    /**
     * Side shown of the second card of the hand
     */
    private String card2SideShown;
    /**
     * Side shown of the third card of the hand
     */
    private String card3SideShown;
    /**
     * Selected card from the hand
     */
    private int selectedCard = -1;
    /**
     * Sent card to place
     */
    private int sentCard = -1;
    /**
     * Sent side of the card to place
     */
    private String sentSide;
    /**
     * Sent position of the card to place
     */
    private Position sentPosition;
    /**
     * Sent deck to draw from
     */
    private String deckSent;
    /**
     * Sent image view of the card to place
     */
    private ImageView sentIV;
    /**
     * Sent id of the card to draw from available
     */
    private int availDrawn;
    /**
     * Boolean to check if I was the one to send the draw request
     */
    private boolean iSent = false;
    /**
     * Maps the player's nickname to their grid
     */
    private final Map<String, GridPane> playerGrids = new HashMap<>();
    /**
     * Maps the player's nickname to their info grid
     */
    private final Map<String, GridPane> playerInfos =  new HashMap<>();
    /**
     * List of buttons of the available positions where I can place the card
     */
    private final List<Button> availPositions = new ArrayList<>();
    /**
     * Maps the player's nickname to the dimensions of their grid
     */
    private final Map<String, Integer> playerGridDimensions =  new HashMap<>();
    /**
     * Maps the image view of the hand card to its id
     */
    private final Map<ImageView, Integer> handCards = new HashMap<>();

    /**
     * Initializes the controller
     */
    public void onLoad() {
        //DISABLE BUTTON
        returnToLobby.setVisible(false);
        resultsButton.setVisible(false);
        infoText.setText("Wait for the turn phase to begin");

        //SET OBJECTIVES
        loadObjectives();

        //SET INITIAL DECKS AND AVAILABLE CARDS
        loadDrawables();

        //SET HAND CARDS
        handCard1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/" +
                guiReference.getLocalGameInstance().getMyHand().get(0).getId() + ".png"))));
        card1SideShown = "f";
        handCards.put(handCard1, guiReference.getLocalGameInstance().getMyHand().get(0).getId());

        handCard2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/" +
                guiReference.getLocalGameInstance().getMyHand().get(1).getId() + ".png"))));
        card2SideShown = "f";
        handCards.put(handCard2, guiReference.getLocalGameInstance().getMyHand().get(1).getId());

        handCard3.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/" +
                guiReference.getLocalGameInstance().getMyHand().get(2).getId() + ".png"))));
        card3SideShown = "f";
        handCards.put(handCard3, guiReference.getLocalGameInstance().getMyHand().get(2).getId());

        //SET PLAYER GRIDS AND TABS         //SET PLAYER INFOS
        loadGridsAndTabs();

        //MANUALLY GENERATES EVENTS IN CASE THE LOADING TOOK LONGER THAN THE NETWORK
        if(guiReference.getLocalGameInstance().getPlayersInOrder() != null && !guiReference.getLocalGameInstance().getPlayersInOrder().isEmpty()) {
            PropertyChangeEvent evt;
            if (guiReference.getLocalGameInstance().getPlayersInOrder().get(0).equals(guiReference.getLocalGameInstance().getMe().getNickname())) {
                evt = new PropertyChangeEvent(
                        this,
                        "CHANGED_TURN",
                        null,
                        guiReference.getLocalGameInstance().getPlayersInOrder().get(0));
            } else {
                evt = new PropertyChangeEvent(
                        this,
                        "NEW_TURN",
                        null,
                        guiReference.getLocalGameInstance().getPlayersInOrder().get(0));
            }
            propertyChange(evt);
        }
    }

    /**
     * Loads the objectives of the game and the private objective of the player
     */
    private void loadObjectives() {
        publicObj1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"
                + guiReference.getLocalGameInstance().getPublicObjectives().get(0).getId() + ".png"))));
        publicObj2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"
                + guiReference.getLocalGameInstance().getPublicObjectives().get(1).getId() + ".png"))));
        privateObjective.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"
                + guiReference.getLocalGameInstance().getMyPrivateObjective().getId() + ".png"))));
    }

    /**
     * Load the top of deck images and available cards images
     */
    private void loadDrawables() {
        topResDeckImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/back/" +
                switch (guiReference.getLocalGameInstance().getTopOfResourceDeck()) {
                    default -> "defaultBack";
                    case PLANT -> "PlantBackResource";
                    case ANIMAL -> "AnimalBackResource";
                    case FUNGI -> "FungiBackResource";
                    case INSECT -> "InsectBackResource";
                } + ".png"))));
        topGoldDeckImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/back/" +
                switch (guiReference.getLocalGameInstance().getTopOfGoldDeck()) {
                    default -> "defaultBack";
                    case PLANT -> "PlantBackGold";
                    case ANIMAL -> "AnimalBackGold";
                    case FUNGI -> "FungiBackGold";
                    case INSECT -> "InsectBackGold";
                } + ".png"))));
        availResCard1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/" +
                guiReference.getLocalGameInstance().getAvailableResourceCards().get(0).getId() + ".png"))));
        availResCard2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/" +
                guiReference.getLocalGameInstance().getAvailableResourceCards().get(1).getId() + ".png"))));
        availGoldCard1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/" +
                guiReference.getLocalGameInstance().getAvailableGoldCards().get(0).getId() + ".png"))));
        availGoldCard2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/" +
                guiReference.getLocalGameInstance().getAvailableGoldCards().get(1).getId() + ".png"))));
    }

    /**
     * Loads the player grids and tabs
     */
    private void loadGridsAndTabs() {
        player1Grid.getChildren().clear();
        player2Grid.getChildren().clear();
        player3Grid.getChildren().clear();
        player4Grid.getChildren().clear();

        player1Tab.setText(guiReference.getLocalGameInstance().getMe().getNickname());
        playerGridDimensions.put(guiReference.getLocalGameInstance().getMe().getNickname(), 6);
        int scId = guiReference.getLocalGameInstance().getMyStartCard().getId();
        String side = understandSide(scId, guiReference.getLocalGameInstance().getMe().getKingdom().getPlacedSides().get(0));

        Image img;
        if (side.equals("f"))
            img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"
                    + scId + ".png")));
        else
            img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/back/"
                    + scId + ".png")));
        ImageView iv = new ImageView(img);
        iv.setPreserveRatio(true);
        iv.setFitWidth(205);
        player1Grid.add(iv, 3,3);
        GridPane.setConstraints(iv, 3, 3, 1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER);

        updateResources(p1Info, guiReference.getLocalGameInstance().getMe());

        p1Name.setText("You");
        p1Points.setText("Points: " + guiReference.getLocalGameInstance().getMe().getPoints());

        int i = 0;
        for (ClientSidePlayer p: guiReference.getLocalGameInstance().getPlayers()) {
            playerGridDimensions.put(p.getNickname(), 6);
            int scIdPl = getIdFromSide(p.getKingdom().getPlacedSides().get(0));
            String sidePl = understandSide(scIdPl, p.getKingdom().getPlacedSides().get(0));

            Image imgPl;
            if (sidePl.equals("f"))
                imgPl = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"
                        + scIdPl + ".png")));
            else
                imgPl = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/back/"
                        + scIdPl + ".png")));
            ImageView ivPl = new ImageView(imgPl);
            ivPl.setPreserveRatio(true);
            ivPl.setFitWidth(205);
            if (i == 0) {
                player2Tab.setText(p.getNickname());
                player2Grid.add(ivPl, 3,3);

                playerGrids.put(p.getNickname(), player2Grid);
                //PLAYER INFOS
                p2Name.setText(p.getNickname());
                p2Points.setText("Points: " + p.getPoints());
                playerInfos.put(p.getNickname(), p2Info);
                updateResources(p2Info, p);
            } else if (i == 1) {
                player3Tab.setText(p.getNickname());
                player3Grid.add(ivPl, 3,3);

                playerGrids.put(p.getNickname(), player3Grid);
                //PLAYER INFOS
                p3Name.setText(p.getNickname());
                p3Points.setText("Points: " + p.getPoints());
                playerInfos.put(p.getNickname(), p3Info);
                updateResources(p3Info, p);
            } else if (i == 2) {
                player4Tab.setText(p.getNickname());
                player4Grid.add(ivPl, 3,3);

                playerGrids.put(p.getNickname(), player4Grid);
                //PLAYER INFOS
                p4Name.setText(p.getNickname());
                p4Points.setText("Points: " + p.getPoints());
                playerInfos.put(p.getNickname(), p4Info);
                updateResources(p4Info, p);
            }
            GridPane.setConstraints(ivPl, 3, 3, 1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER);
            i++;
        }

        if (i == 1) {
            player3Tab.setDisable(true);
            player4Tab.setDisable(true);

            //PLAYER INFOS
            p3Info.setDisable(true);
            leftHBox.getChildren().remove(p3Info);
            p4Info.setDisable(true);
            leftHBox.getChildren().remove(p4Info);
        } else if (i == 2) {
            player4Tab.setDisable(true);

            //PLAYER INFOS
            p4Info.setDisable(true);
            leftHBox.getChildren().remove(p4Info);
        }
    }

    /**
     * Action of the return to lobby button, changes the scene to the login
     * @param actionEvent the event of the button click
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
     * Action of the results button, changes the scene to the results
     * @param actionEvent the event of the button click
     */
    public void onResultsClick(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            try {
                changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/showResults.fxml", "results", actionEvent);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Action of the handCard1 button, changes the selected card to the one in the first position of the hand
     * @param actionEvent the event of the button click
     */
    public void onSelectCard1Click(ActionEvent actionEvent) {
        selectedCard = handCards.get(handCard1);
        if (guiReference.getState().equals(ViewState.PLACE) || guiReference.getState().equals(ViewState.ERROR)) {
            infoText.setText("You have selected the card "+ handCards.get(handCard1) + " and side " + card1SideShown + ". Now click on one of the active positions to place it.");
        }
    }

    /**
     * Action of the flipCard1 button, flips the first card of the hand
     * @param actionEvent the event of the button click
     */
    public void onFlipCard1Click(ActionEvent actionEvent) {
        card1SideShown = flip(card1SideShown, handCard1, handCards.get(handCard1));
        selectedCard = -1;
        Platform.runLater(() -> infoText.setText("Select a card by clicking on it."));
    }

    /**
     * Action of the handCard2 button, changes the selected card to the one in the second position of the hand
     * @param actionEvent the event of the button click
     */
    public void onSelectCard2Click(ActionEvent actionEvent) {
        selectedCard = handCards.get(handCard2);
        if (guiReference.getState().equals(ViewState.PLACE) || guiReference.getState().equals(ViewState.ERROR)) {
            infoText.setText("You have selected the card "+ handCards.get(handCard2) + " and side " + card2SideShown + ". Now click on one of the active positions to place it.");
        }
    }

    /**
     * Action of the flipCard2 button, flips the second card of the hand
     * @param actionEvent the event of the button click
     */
    public void onFlipCard2Click(ActionEvent actionEvent) {
        card2SideShown = flip(card2SideShown, handCard2, handCards.get(handCard2));
        selectedCard = -1;
        Platform.runLater(() -> infoText.setText("Select a card by clicking on it."));
    }

    /**
     * Action of the handCard3 button, changes the selected card to the one in the third position of the hand
     * @param actionEvent the event of the button click
     */
    public void onSelectCard3Click(ActionEvent actionEvent) {
        selectedCard = handCards.get(handCard3);
        if (guiReference.getState().equals(ViewState.PLACE) || guiReference.getState().equals(ViewState.ERROR)) {
            infoText.setText("You have selected the card "+ handCards.get(handCard3) + " and side " + card3SideShown + ". Now click on one of the active positions to place it.");
        }
    }

    /**
     * Action of the flipCard3 button, flips the third card of the hand
     * @param actionEvent the event of the button click
     */
    public void onFlipCard3Click(ActionEvent actionEvent) {
        card3SideShown = flip(card3SideShown, handCard3, handCards.get(handCard3));
        selectedCard = -1;
        Platform.runLater(() -> infoText.setText("Select a card by clicking on it."));
    }

    /**
     * Method to flip get the string that represents the new card after flipping
     * @param shown Currently shown side of the card
     * @param card ImageView of the card to flip
     * @param handId id of the card to flip
     * @return the new side shown
     */
    private String flip(String shown, ImageView card, int handId) {
        if(shown.equals("f")) {
            String resource = getBackResource(handId);

            card.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/back/" +
            resource + ".png"))));
            shown = "b";
        } else {
            card.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/" +
                    handId + ".png"))));
            shown = "f";
        }
        return shown;
    }

//----------------------------------------------------------------------------------------------------Deck and avail Buttons
    /**
     * Action of the topResDeck button, draws a card from the resource deck
     * @param actionEvent the event of the button click
     */
    public void resDeckClicked(ActionEvent actionEvent) {
        if(guiReference.getState().equals(ViewState.DRAW)) {
            deckSent = "r";
            iSent = true;
            guiReference.drawFromDeck("r");
            topResDeckButton.setDisable(true);
            topGoldDeckButton.setDisable(true);
            availGoldCard1Button.setDisable(true);
            availGoldCard2Button.setDisable(true);
            availResCard1Button.setDisable(true);
            availResCard2Button.setDisable(true);
        }
    }

    /**
     * Action of the availRes1 button, draws the first available resource card
     * @param actionEvent the event of the button click
     */
    public void availRes1Clicked(ActionEvent actionEvent) {
        if(guiReference.getState().equals(ViewState.DRAW)) {
            deckSent = "r";
            availDrawn = guiReference.getLocalGameInstance().getAvailableResourceCards().get(0).getId();
            iSent = true;
            topResDeckButton.setDisable(true);
            topGoldDeckButton.setDisable(true);
            availGoldCard1Button.setDisable(true);
            availGoldCard2Button.setDisable(true);
            availResCard1Button.setDisable(true);
            availResCard2Button.setDisable(true);

            guiReference.drawFromAvail(availDrawn);
        }
    }

    /**
     * Action of the availRes2 button, draws the second available resource card
     * @param actionEvent the event of the button click
     */
    public void availRes2Clicked(ActionEvent actionEvent) {
        if(guiReference.getState().equals(ViewState.DRAW)) {
            deckSent = "r";
            availDrawn = guiReference.getLocalGameInstance().getAvailableResourceCards().get(1).getId();
            iSent = true;
            topResDeckButton.setDisable(true);
            topGoldDeckButton.setDisable(true);
            availGoldCard1Button.setDisable(true);
            availGoldCard2Button.setDisable(true);
            availResCard1Button.setDisable(true);
            availResCard2Button.setDisable(true);

            guiReference.drawFromAvail(availDrawn);
        }
    }

    /**
     * Action of the topGoldDeck button, draws a card from the gold deck
     * @param actionEvent the event of the button click
     */
    public void goldDeckClicked(ActionEvent actionEvent) {
        if(guiReference.getState().equals(ViewState.DRAW)) {
            guiReference.drawFromDeck("g");
            iSent = true;
            topResDeckButton.setDisable(true);
            topGoldDeckButton.setDisable(true);
            availGoldCard1Button.setDisable(true);
            availGoldCard2Button.setDisable(true);
            availResCard1Button.setDisable(true);
            availResCard2Button.setDisable(true);
        }
    }

    /**
     * Action of the availGold1 button, draws the first available gold card
     * @param actionEvent the event of the button click
     */
    public void availGold1Clicked(ActionEvent actionEvent) {
        if(guiReference.getState().equals(ViewState.DRAW)) {
            deckSent = "g";
            availDrawn = guiReference.getLocalGameInstance().getAvailableGoldCards().get(0).getId();
            iSent = true;
            topResDeckButton.setDisable(true);
            topGoldDeckButton.setDisable(true);
            availGoldCard1Button.setDisable(true);
            availGoldCard2Button.setDisable(true);
            availResCard1Button.setDisable(true);
            availResCard2Button.setDisable(true);

            guiReference.drawFromAvail(availDrawn);
        }
    }

    /**
     * Action of the availGold2 button, draws the second available gold card
     * @param actionEvent the event of the button click
     */
    public void availGold2Clicked(ActionEvent actionEvent) {
        if(guiReference.getState().equals(ViewState.DRAW)) {
            deckSent = "g";
            availDrawn = guiReference.getLocalGameInstance().getAvailableGoldCards().get(1).getId();
            iSent = true;

            topResDeckButton.setDisable(true);
            topGoldDeckButton.setDisable(true);
            availGoldCard1Button.setDisable(true);
            availGoldCard2Button.setDisable(true);
            availResCard1Button.setDisable(true);
            availResCard2Button.setDisable(true);

            guiReference.drawFromAvail(availDrawn);
        }
    }

    //----------------------------------------------------------------------------------------------------Property change

    /**
     * Method to handle property change events thrown by the model
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        switch (evt.getPropertyName()) {
            case "CHANGED_STATE": {
                //IF STATE = DRAW SET TEXT AND ENABLE BUTTONS
                if (evt.getNewValue().equals(ViewState.DRAW)) {
                    //DISABLE ALL BUTTONS IN THE GRID
                    //ACTUALLY PLACE THE CARD IN POSITION
                    Platform.runLater(() -> {
                        guiReference.getLocalGameInstance().placeCard(guiReference.getLocalGameInstance().getMe().getNickname(), sentCard, sentSide, sentPosition);

                        for (Button b : availPositions) {
                            player1Grid.getChildren().remove(b);
                        }
                        //player1Grid.getChildren().removeIf(node -> node instanceof Button);
                        ImageView iv;
                        if (sentSide.equals("f"))
                            iv = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                                    "/it/polimi/ingsw/am37/view/GUI/front/" + sentCard + ".png"))));
                        else
                            iv = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                                    "/it/polimi/ingsw/am37/view/GUI/back/" + getBackResource(sentCard) + ".png"))));
                        iv.setPreserveRatio(true);
                        iv.setFitWidth(205);
                        player1Grid.add(iv, sentPosition.getX()+ playerGridDimensions.get(guiReference.getLocalGameInstance().getMe().getNickname())/2,
                                -sentPosition.getY() + playerGridDimensions.get(guiReference.getLocalGameInstance().getMe().getNickname())/2);
                        GridPane.setConstraints(iv, sentPosition.getX() + playerGridDimensions.get(guiReference.getLocalGameInstance().getMe().getNickname())/2,
                                -sentPosition.getY() + playerGridDimensions.get(guiReference.getLocalGameInstance().getMe().getNickname())/2,
                                1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER);
                        updateResources(p1Info, guiReference.getLocalGameInstance().getMe());
                        updatePoints();

                        //REMOVE CARD FROM HAND VISUALIZATION
                        if (sentCard == handCards.get(handCard1))
                            handCard1Button.setVisible(false);
                        else if (sentCard == handCards.get(handCard2))
                            handCard2Button.setVisible(false);
                        else
                            handCard3Button.setVisible(false);
                    });



                    Platform.runLater(() -> infoText.setText("It's your turn. Draw a card from the deck or from the available cards."));
                    Platform.runLater(() -> {
                        topResDeckButton.setDisable(false);
                        topGoldDeckButton.setDisable(false);
                        availGoldCard1Button.setDisable(false);
                        availGoldCard2Button.setDisable(false);
                        availResCard1Button.setDisable(false);
                        availResCard2Button.setDisable(false);
                    });
                } else if (evt.getNewValue().equals(ViewState.SHOW_RESULTS)) {
                    Platform.runLater(() -> {
                        infoText.setText("The game has ended. Press the button to see the results.");
                        resultsButton.setVisible(true);
                    });
                } else
                //IF STATE = DISCONNECTED SET TEXT AND DISABLE BUTTONS, ENABLE RETURN TO LOBBY
                if (evt.getNewValue().equals(ViewState.DISCONNECTION)) {
                    infoText.setText("One or more players disconnected, press the button to return to the lobby");
                    Platform.runLater(() -> returnToLobby.setVisible(true));
                }
                break;
            }

            case "CHANGED_TURN": {
                Platform.runLater(() -> {
                    infoText.setText("It's your turn. Select a card and place it in the Kingdom.");
                    //SET ALL THE BUTTONS IN THE LIST TO VISIBLE
                    boolean success = false;
                    while (!success) {
                        success = true;
                        availPositions.clear();
                        for (Position p : guiReference.getLocalGameInstance().getMe().getKingdom().getActivePositions()) {
                            Button b = new Button();
                            b.setText("Place here: " + p.getX() + " " + p.getY());
                            b.setOnAction(e -> {
                                if (selectedCard != -1) {
                                    sentCard = selectedCard;        //SENT SIDE IS WRONG USE handCards MAP
                                    sentSide = (selectedCard == handCards.get(handCard1)) ? card1SideShown :
                                            (selectedCard == handCards.get(handCard2)) ? card2SideShown : card3SideShown;
                                    sentPosition = p;
                                    sentIV = handCards.get(handCard1) == sentCard ? handCard1 : handCards.get(handCard2) == sentCard ? handCard2 : handCard3;
                                    guiReference.placeCard(sentCard, sentSide, p);
                                    selectedCard = -1;
                                } else {
                                    infoText.setText("You must select a card in your hand by clicking on it first.");
                                }
                            });
                            if (p.getX() + playerGridDimensions.get(guiReference.getLocalGameInstance().getMe().getNickname())/2 < 0 ||
                                    -p.getY() + playerGridDimensions.get(guiReference.getLocalGameInstance().getMe().getNickname())/2 < 0 ||
                                    p.getX() + playerGridDimensions.get(guiReference.getLocalGameInstance().getMe().getNickname())/2 > playerGridDimensions.get(guiReference.getLocalGameInstance().getMe().getNickname()) -1 ||
                                    -p.getY() + playerGridDimensions.get(guiReference.getLocalGameInstance().getMe().getNickname())/2 > playerGridDimensions.get(guiReference.getLocalGameInstance().getMe().getNickname()) -1) {
                                redrawGrid(player1Grid, guiReference.getLocalGameInstance().getMe());
                                success = false;
                                break;
                            } else {
                                player1Grid.add(b, p.getX() + playerGridDimensions.get(guiReference.getLocalGameInstance().getMe().getNickname())/2,
                                        -p.getY() + playerGridDimensions.get(guiReference.getLocalGameInstance().getMe().getNickname())/2);
                                GridPane.setConstraints(b, p.getX() + playerGridDimensions.get(guiReference.getLocalGameInstance().getMe().getNickname())/2,
                                        -p.getY() + playerGridDimensions.get(guiReference.getLocalGameInstance().getMe().getNickname())/2,
                                        1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER);
                            }
                            availPositions.add(b);
                        }
                    }
                });

                break;
            }

            case "ERROR": {
                Platform.runLater(() -> infoText.setText((String) evt.getNewValue()));
                System.out.println("ERROR: " + evt.getNewValue());
                break;
            }

            case "NEW_TURN": {
                if(!evt.getNewValue().equals(guiReference.getLocalGameInstance().getMe().getNickname())) {
                    Platform.runLater(() -> infoText.setText("It's " + evt.getNewValue() + "'s turn. Wait for them to place and draw a card."));
                }

                break;
            }

            case "CHANGED_KINGDOM": {
                Platform.runLater(() -> {
                    String placedSide = understandSide((int) evt.getNewValue(), ((ClientSidePlayer) evt.getOldValue()).getKingdom().getPlacedSides().getLast());
                    Position placed = ((ClientSidePlayer) evt.getOldValue()).getKingdom().getPlacedSides().getLast().getPositionInKingdom();
                    ImageView iv;
                    if (placedSide.equals("f"))
                        iv = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                            "/it/polimi/ingsw/am37/view/GUI/front/" + evt.getNewValue() + ".png"))));
                    else
                        iv = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                                "/it/polimi/ingsw/am37/view/GUI/back/" + getBackResource((int) evt.getNewValue()) + ".png"))));

                    if (-placed.getY() + playerGridDimensions.get(((ClientSidePlayer) evt.getOldValue()).getNickname())/2 < 0 ||
                            placed.getX() + playerGridDimensions.get(((ClientSidePlayer) evt.getOldValue()).getNickname())/2 < 0 ||
                            -placed.getY() + playerGridDimensions.get(((ClientSidePlayer) evt.getOldValue()).getNickname())/2 > playerGridDimensions.get(((ClientSidePlayer) evt.getOldValue()).getNickname()) - 1 ||
                            placed.getX() + playerGridDimensions.get(((ClientSidePlayer) evt.getOldValue()).getNickname())/2 > playerGridDimensions.get(((ClientSidePlayer) evt.getOldValue()).getNickname()) - 1)
                        redrawGrid(playerGrids.get(((ClientSidePlayer) evt.getOldValue()).getNickname()), (ClientSidePlayer) evt.getOldValue());
                    else {
                        iv.setPreserveRatio(true);
                        iv.setFitWidth(205);
                        playerGrids.get(((ClientSidePlayer) evt.getOldValue()).getNickname()).add(iv, placed.getX() + playerGridDimensions.get(((ClientSidePlayer) evt.getOldValue()).getNickname())/2,
                                -placed.getY() + playerGridDimensions.get(((ClientSidePlayer) evt.getOldValue()).getNickname())/2);
                        GridPane.setConstraints(iv, placed.getX() + playerGridDimensions.get(((ClientSidePlayer) evt.getOldValue()).getNickname())/2,
                                -placed.getY() + playerGridDimensions.get(((ClientSidePlayer) evt.getOldValue()).getNickname())/2, 1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER);

                    }

                    updateResources(playerInfos.get(((ClientSidePlayer) evt.getOldValue()).getNickname()), (ClientSidePlayer) evt.getOldValue());
                    updatePoints();
                });
                break;
            }

            case "CHANGED_DECK" : {
                Platform.runLater(() -> {
                    if (evt.getOldValue().equals("r")) {
                        topResDeckImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/back/" +
                                switch ((Resource) evt.getNewValue()) {
                                    default -> "defaultBack";
                                    case PLANT -> "PlantBackResource";
                                    case ANIMAL -> "AnimalBackResource";
                                    case FUNGI -> "FungiBackResource";
                                    case INSECT -> "InsectBackResource";
                                } + ".png"))));
                    } else {
                        topGoldDeckImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/back/" +
                                switch ((Resource) evt.getNewValue()) {
                                    default -> "defaultBack";
                                    case PLANT -> "PlantBackGold";
                                    case ANIMAL -> "AnimalBackGold";
                                    case FUNGI -> "FungiBackGold";
                                    case INSECT -> "InsectBackGold";
                                } + ".png"))));
                    }

                    if (iSent) {
                        updateHand();
                        iSent = false;
                    }
                });
                break;
            }

            case "CHANGED_AVAILABLE": {
                Platform.runLater(() -> {
                    List<StandardCard> newCards = (List<StandardCard>) evt.getNewValue();

                    if (evt.getOldValue().equals("r")) {
                        availResCard1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/" +
                                newCards.get(0).getId() + ".png"))));
                        availResCard2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/" +
                                newCards.get(1).getId() + ".png"))));
                    } else {
                        availGoldCard1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/" +
                                newCards.get(0).getId() + ".png"))));
                        availGoldCard2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/" +
                                newCards.get(1).getId() + ".png"))));
                    }

                    if (iSent) {
                        guiReference.getLocalGameInstance().getMyHand().add(getCardFromId(availDrawn));

                        updateHand();
                        iSent = false;
                    }
                });
                break;
            }

            case "ENDGAME": {
                Platform.runLater(() -> infoText.setText("A player has reached 20 points. We have entered the endgame."));
                break;
            }

            case "LAST_TURN": {
                Platform.runLater(() -> infoText.setText("This will be the last turn."));
                break;
            }
        }
    }

    /**
     * Method to update the points of the players
     */
    private void updatePoints() {
        Platform.runLater(() -> {
            p1Points.setText("Points: " + guiReference.getLocalGameInstance().getMe().getPoints());
            for (ClientSidePlayer p: guiReference.getLocalGameInstance().getPlayers()) {
                if (playerInfos.get(p.getNickname()).equals(p2Info))
                    p2Points.setText("Points: " + p.getPoints());
                else if (playerInfos.get(p.getNickname()).equals(p3Info))
                    p3Points.setText("Points: " + p.getPoints());
                else if (playerInfos.get(p.getNickname()).equals(p4Info))
                    p4Points.setText("Points: " + p.getPoints());
            }
        });
    }

    /**
     * Method to update the hand of the player
     */
    private void updateHand() {
        if (sentCard == handCards.get(handCard1)) {
            for (StandardCard c: guiReference.getLocalGameInstance().getMyHand()) {
                if (c.getId() != handCards.get(handCard2) && c.getId() != handCards.get(handCard3)) {
                    handCard1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/" +
                            c.getId() + ".png"))));
                    handCards.put(handCard1, c.getId());
                    card1SideShown = "f";
                    break;
                }
            }
            handCard1Button.setVisible(true);
        } else if (sentCard == handCards.get(handCard2)) {
            for (StandardCard c : guiReference.getLocalGameInstance().getMyHand()) {
                if (c.getId() != handCards.get(handCard1) && c.getId() != handCards.get(handCard3)) {
                    handCard2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/" +
                            c.getId() + ".png"))));
                    handCards.put(handCard2, c.getId());
                    card2SideShown = "f";
                    break;
                }
            }
            handCard2Button.setVisible(true);

        } else {
            for (StandardCard c : guiReference.getLocalGameInstance().getMyHand()) {
                if (c.getId() != handCards.get(handCard1) && c.getId() != handCards.get(handCard2)) {
                    handCard3.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/" +
                            c.getId() + ".png"))));
                    handCards.put(handCard3, c.getId());
                    card3SideShown = "f";
                    break;
                }
            }
            handCard3Button.setVisible(true);
        }
    }

    /**
     * Method to update the resources of the players
     * @param grid grid of the player info that needs to be updated
     * @param player player that needs his resources to be updated
     */
    private void updateResources(GridPane grid, ClientSidePlayer player) {
        if (grid.equals(p1Info)) {
            p1Animal.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.ANIMAL)));
            p1Plant.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.PLANT)));
            p1Fungi.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.FUNGI)));
            p1Insect.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.INSECT)));
            p1Inkwell.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.INKWELL)));
            p1Manuscript.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.MANUSCRIPT)));
            p1Quill.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.QUILL)));
        } else if (grid.equals(p2Info)) {
            p2Animal.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.ANIMAL)));
            p2Plant.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.PLANT)));
            p2Fungi.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.FUNGI)));
            p2Insect.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.INSECT)));
            p2Inkwell.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.INKWELL)));
            p2Manuscript.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.MANUSCRIPT)));
            p2Quill.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.QUILL)));
        } else if (grid.equals(p3Info)) {
            p3Animal.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.ANIMAL)));
            p3Plant.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.PLANT)));
            p3Fungi.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.FUNGI)));
            p3Insect.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.INSECT)));
            p3Inkwell.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.INKWELL)));
            p3Manuscript.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.MANUSCRIPT)));
            p3Quill.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.QUILL)));
        } else if (grid.equals(p4Info)) {
            p4Animal.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.ANIMAL)));
            p4Plant.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.PLANT)));
            p4Fungi.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.FUNGI)));
            p4Insect.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.INSECT)));
            p4Inkwell.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.INKWELL)));
            p4Manuscript.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.MANUSCRIPT)));
            p4Quill.setText(String.valueOf(player.getKingdom().getOnFieldResources().get(Resource.QUILL)));
        }
    }

    /**
     * Method to understand the id of the card from the side
     * @param s side of the card
     * @return the id of the card
     */
    private int getIdFromSide(Side s) {

        for (StandardCard sc: guiReference.getLocalGameInstance().getResourceCards())
            if (sc.getFront().equals(s))
                return sc.getId();
            else if (sc.getBack().equals(s))
                return sc.getId();

        for (StandardCard sc: guiReference.getLocalGameInstance().getGoldCards())
            if (sc.getFront().equals(s))
                return sc.getId();
            else if (sc.getBack().equals(s))
                return sc.getId();

        for (StartCard sc: guiReference.getLocalGameInstance().getStartCards())
            if (sc.getFront().equals(s))
                return sc.getId();
            else if (sc.getBack().equals(s))
                return sc.getId();

        return -1;
    }

    /**
     * Method to get the card from the id
     * @param cardId id of the card
     * @return the card
     */
    private StandardCard getCardFromId(int cardId) {
        for (StandardCard sc: guiReference.getLocalGameInstance().getResourceCards())
            if (sc.getId() == cardId)
                return sc;
        for (StandardCard sc: guiReference.getLocalGameInstance().getGoldCards())
            if (sc.getId() == cardId)
                return sc;
        return null;
    }

    /**
     * Method to understand the side of the card from the id
     * @param card id of the card
     * @param s side of the card
     * @return the side of the card in string form
     */
    private String understandSide(int card, Side s) {
        GameCard found = null;

        if (card > 0 && card <= 40) {
            for (StandardCard sc: guiReference.getLocalGameInstance().getResourceCards())
                if (sc.getId() == card) {
                    found = sc;
                    break;
                }
        } else if (card > 40 && card <= 80) {
            for (StandardCard sc: guiReference.getLocalGameInstance().getGoldCards())
                if (sc.getId() == card) {
                    found = sc;
                    break;
                }
        } else {
            for (StartCard sc: guiReference.getLocalGameInstance().getStartCards())
                if (sc.getId() == card) {
                    found = sc;
                    break;
                }
        }
        assert found != null;

        if (found.getFront().equals(s))
            return "f";
        else
            return "b";
    }

    /**
     * Method to gets the correct string for displaying the back of the card given the id
     * @param cardId id of the card
     * @return string that represents the image of the back of the card
     */
    private String getBackResource(int cardId) {
        if (cardId > 0 && cardId <= 40) {
            if (cardId <= 10) return "FungiBackResource";
            else if (cardId <= 20) return "PlantBackResource";
            else if (cardId <= 30) return "AnimalBackResource";
            else return "InsectBackResource";
        } else if (cardId > 40 && cardId <= 80) {
            if (cardId <= 50) return "FungiBackGold";
            else if (cardId <= 60) return "PlantBackGold";
            else if (cardId <= 70) return "AnimalBackGold";
            else return "InsectBackGold";
        } else
            return String.valueOf(cardId);
    }

    /**
     * Method to redraw the grid of the player
     * @param grid grid of the player
     * @param player player that needs his grid to be redrawn
     */
    private void redrawGrid(GridPane grid, ClientSidePlayer player) {
        int newDimension;
        if (playerGridDimensions.get(player.getNickname()) == 6) {
            playerGridDimensions.put(player.getNickname(), 12);
            newDimension = 12;
        } else if (playerGridDimensions.get(player.getNickname()) == 12) {
            playerGridDimensions.put(player.getNickname(), 20);
            newDimension = 20;
        } else if (playerGridDimensions.get(player.getNickname()) == 20) {
            playerGridDimensions.put(player.getNickname(), 40);
            newDimension = 40;
        } else {
            playerGridDimensions.put(player.getNickname(), 80);
            newDimension = 80;
        }

        grid.getChildren().clear();
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
        int i = 0;
        while (i < newDimension) {
            grid.getColumnConstraints().add(new ColumnConstraints(162, 162, 162));
            grid.getRowConstraints().add(new RowConstraints(84, 84, 84));
            i++;
        }

        for (Side s: player.getKingdom().getPlacedSides()) {
            ImageView iv;
            int id = getIdFromSide(s);
            String placedSide = understandSide(id, s);
            if (placedSide.equals("f")) {
                iv = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                        "/it/polimi/ingsw/am37/view/GUI/front/" + id + ".png"))));
            } else {
                iv = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                        "/it/polimi/ingsw/am37/view/GUI/back/" + getBackResource(id) + ".png"))));
            }
            iv.setPreserveRatio(true);
            iv.setFitWidth(205);

            //grid.getColumnConstraints().add(new ColumnConstraints(162, 162, 162));
            //grid.getRowConstraints().add(new RowConstraints(84, 84, 84));
            grid.add(iv, s.getPositionInKingdom().getX() + newDimension/2, -s.getPositionInKingdom().getY() + newDimension/2);
            GridPane.setConstraints(iv, s.getPositionInKingdom().getX() + newDimension/2, -s.getPositionInKingdom().getY() + newDimension/2, 1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER);
        }
    }
}
