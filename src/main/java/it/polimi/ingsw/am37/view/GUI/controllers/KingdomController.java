package it.polimi.ingsw.am37.view.GUI.controllers;

import it.polimi.ingsw.am37.model.cards.placeable.GameCard;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class KingdomController extends GUIController implements PropertyChangeListener {
    @FXML
    public Button returnToLobby;
    @FXML
    public Text infoText;

    //PUBLIC OBJECTIVES
    @FXML
    private ImageView publicObj1;
    @FXML
    private ImageView publicObj2;

    //DECKS AND AVAILABLE CARDS
    @FXML
    private Button topResDeckButton;
    @FXML
    private ImageView topResDeckImage;

    @FXML
    private Button availResCard1Button;
    @FXML
    private ImageView availResCard1;
    @FXML
    private Button availResCard2Button;
    @FXML
    private ImageView availResCard2;

    @FXML
    private Button topGoldDeckButton;
    @FXML
    private ImageView topGoldDeckImage;

    @FXML
    private Button availGoldCard1Button;
    @FXML
    private ImageView availGoldCard1;
    @FXML
    private Button availGoldCard2Button;
    @FXML
    private ImageView availGoldCard2;

    //HAND CARDS
    @FXML
    private Button handCard1Button;
    @FXML
    private ImageView handCard1;
    @FXML
    private Button flipCard1Button;
    @FXML
    private Button handCard2Button;
    @FXML
    private ImageView handCard2;
    @FXML
    private Button flipCard2Button;
    @FXML
    private Button handCard3Button;
    @FXML
    private ImageView handCard3;
    @FXML
    private Button flipCard3Button;

    //PRIVATE OBJECTIVE
    @FXML
    private ImageView privateObjective;

    //PLAYER GRIDS AND TABS
    @FXML
    private Tab player1Tab;
    @FXML
    private Tab player2Tab;
    @FXML
    private Tab player3Tab;
    @FXML
    private Tab player4Tab;

    @FXML
    private GridPane player1Grid;
    @FXML
    private GridPane player2Grid;
    @FXML
    private GridPane player3Grid;
    @FXML
    private GridPane player4Grid;

    //PLAYER INFOS
    @FXML
    private HBox leftHBox;
    @FXML
    private GridPane p1Info;
    @FXML
    private GridPane p2Info;
    @FXML
    private GridPane p3Info;
    @FXML
    private GridPane p4Info;

    @FXML
    private Text p1Name;
    @FXML
    private Text p2Name;
    @FXML
    private Text p3Name;
    @FXML
    private Text p4Name;

    @FXML
    private Text p1Points;
    @FXML
    private Text p2Points;
    @FXML
    private Text p3Points;
    @FXML
    private Text p4Points;

    @FXML
    private Text p1Animal;
    @FXML
    private Text p2Animal;
    @FXML
    private Text p3Animal;
    @FXML
    private Text p4Animal;

    @FXML
    private Text p1Plant;
    @FXML
    private Text p2Plant;
    @FXML
    private Text p3Plant;
    @FXML
    private Text p4Plant;

    @FXML
    private Text p1Fungi;
    @FXML
    private Text p2Fungi;
    @FXML
    private Text p3Fungi;
    @FXML
    private Text p4Fungi;

    @FXML
    private Text p1Insect;
    @FXML
    private Text p2Insect;
    @FXML
    private Text p3Insect;
    @FXML
    private Text p4Insect;

    @FXML
    private Text p1Inkwell;
    @FXML
    private Text p2Inkwell;
    @FXML
    private Text p3Inkwell;
    @FXML
    private Text p4Inkwell;

    @FXML
    private Text p1Manuscript;
    @FXML
    private Text p2Manuscript;
    @FXML
    private Text p3Manuscript;
    @FXML
    private Text p4Manuscript;

    @FXML
    private Text p1Quill;
    @FXML
    private Text p2Quill;
    @FXML
    private Text p3Quill;
    @FXML
    private Text p4Quill;




    private String card1SideShown;
    private String card2SideShown;
    private String card3SideShown;
    private int selectedCard = -1;
    private Map<String, GridPane> playerGrids = new HashMap<>();
    private Map<String, GridPane> playerInfos =  new HashMap<>();


    public void onLoad() {
        //DISABLE BUTTON
        returnToLobby.setVisible(false);
        infoText.setText("Wait for the turn phase to begin");

        //SET OBJECTIVES
        loadObjectives();

        //SET INITIAL DECKS AND AVAILABLE CARDS
        loadDrawables();

        //SET HAND CARDS
        handCard1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/" +
                guiReference.getLocalGameInstance().getMyHand().get(0).getId() + ".png"))));
        card1SideShown = "front";
        handCard2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/" +
                guiReference.getLocalGameInstance().getMyHand().get(1).getId() + ".png"))));
        card2SideShown = "front";
        handCard3.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/" +
                guiReference.getLocalGameInstance().getMyHand().get(2).getId() + ".png"))));
        card3SideShown = "front";

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

    private void loadObjectives() {
        publicObj1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"
                + guiReference.getLocalGameInstance().getPublicObjectives().get(0).getId() + ".png"))));
        publicObj2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"
                + guiReference.getLocalGameInstance().getPublicObjectives().get(1).getId() + ".png"))));
        privateObjective.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"
                + guiReference.getLocalGameInstance().getMyPrivateObjective().getId() + ".png"))));
    }

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

    private void loadGridsAndTabs() {
        player1Grid.getChildren().clear();
        player2Grid.getChildren().clear();
        player3Grid.getChildren().clear();
        player4Grid.getChildren().clear();

        player1Tab.setText(guiReference.getLocalGameInstance().getMe().getNickname());
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

        p1Name.setText("You");
        p1Points.setText("Points: " + guiReference.getLocalGameInstance().getMe().getPoints());

        int i = 0;
        for (ClientSidePlayer p: guiReference.getLocalGameInstance().getPlayers()) {
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
            } else if (i == 1) {
                player3Tab.setText(p.getNickname());
                player3Grid.add(ivPl, 3,3);

                playerGrids.put(p.getNickname(), player3Grid);
                //PLAYER INFOS
                p3Name.setText(p.getNickname());
                p3Points.setText("Points: " + p.getPoints());
                playerInfos.put(p.getNickname(), p3Info);
            } else if (i == 2) {
                player4Tab.setText(p.getNickname());
                player4Grid.add(ivPl, 3,3);

                playerGrids.put(p.getNickname(), player4Grid);
                //PLAYER INFOS
                p4Name.setText(p.getNickname());
                p4Points.setText("Points: " + p.getPoints());
                playerInfos.put(p.getNickname(), p4Info);
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

    public void onReturnToLobbyClick(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            try {
                changeScene("/it/polimi/ingsw/am37/view/GUI/fxml/login.fxml", "login", actionEvent);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void onSelectCard1Click(ActionEvent actionEvent) {
        selectedCard = guiReference.getLocalGameInstance().getMyHand().get(0).getId();
        if (guiReference.getState().equals(ViewState.PLACE)) {
            infoText.setText("You have selected the card you want to place. Now click on one of the active positions to place it.");
        }
    }

    public void onFlipCard1Click(ActionEvent actionEvent) {
        card1SideShown = flip(card1SideShown, handCard1, 0);
    }

    public void onSelectCard2Click(ActionEvent actionEvent) {
        selectedCard = guiReference.getLocalGameInstance().getMyHand().get(1).getId();
        if (guiReference.getState().equals(ViewState.PLACE)) {
            infoText.setText("You have selected the card you want to place. Now click on one of the active positions to place it.");
        }
    }

    public void onFlipCard2Click(ActionEvent actionEvent) {
        card2SideShown = flip(card2SideShown, handCard2, 1);
    }

    public void onSelectCard3Click(ActionEvent actionEvent) {
        selectedCard = guiReference.getLocalGameInstance().getMyHand().get(2).getId();
        if (guiReference.getState().equals(ViewState.PLACE)) {
            infoText.setText("You have selected the card you want to place. Now click on one of the active positions to place it.");
        }
    }

    public void onFlipCard3Click(ActionEvent actionEvent) {
        card3SideShown = flip(card3SideShown, handCard3, 2);
    }

    private String flip(String shown, ImageView card, int handId) {
        if(shown.equals("front")) {
            String resource = getResource(handId);

            card.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/back/" +
            resource + ".png"))));
            shown = "back";
        } else {
            card.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/" +
                    guiReference.getLocalGameInstance().getMyHand().get(handId).getId() + ".png"))));
            shown = "front";
        }
        return shown;
    }

    private String getResource(int handId) {
        String resource;
        if(guiReference.getLocalGameInstance().getMyHand().get(handId).getId() <= 40 ) {
            resource = switch(guiReference.getLocalGameInstance().getMyHand().get(handId).getBack().getMainResource()) {
                default -> "defaultBack";
                case PLANT -> "PlantBackResource";
                case ANIMAL -> "AnimalBackResource";
                case FUNGI -> "FungiBackResource";
                case INSECT -> "InsectBackResource";
            };
        } else {
            resource = switch(guiReference.getLocalGameInstance().getMyHand().get(handId).getBack().getMainResource()) {
                default -> "defaultBack";
                case PLANT -> "PlantBackGold";
                case ANIMAL -> "AnimalBackGold";
                case FUNGI -> "FungiBackGold";
                case INSECT -> "InsectBackGold";
            };
        }
        return resource;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        switch (evt.getPropertyName()) {
            case "CHANGED_STATE": {
                //IF STATE = DRAW SET TEXT AND ENABLE BUTTONS

                //IF STATE = NOT TURN, SET TEXT AND DISABLE BUTTONS IN THE GRID

                //IF STATE = DISCONNECTED SET TEXT AND DISABLE BUTTONS, ENABLE RETURN TO LOBBY
                if (evt.getNewValue().equals(ViewState.DISCONNECTION)) {
                    infoText.setText("One or more players disconnected, press the button to return to the lobby");
                    Platform.runLater(() -> returnToLobby.setVisible(true));
                }

                break;
            }

            case "CHANGED_TURN": {
                Platform.runLater(() -> infoText.setText("It's your turn. Select a card and place it in the Kingdom."));

                break;
            }

            case "NEW_TURN": {
                if(!evt.getNewValue().equals(guiReference.getLocalGameInstance().getMe().getNickname())) {
                    Platform.runLater(() -> infoText.setText("It's " + evt.getNewValue() + "'s turn. Wait for them to place and draw a card."));
                }

                break;
            }
        }
    }

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

    public void topResDeckButtonClicked(ActionEvent actionEvent) {
    }

    public void firstResCardAvailableButtonClicked(ActionEvent actionEvent) {
    }

    public void secondResCardAvailableButtonClicked(ActionEvent actionEvent) {
    }

    public void topGoldDeckButtonClicked(ActionEvent actionEvent) {
    }
}
