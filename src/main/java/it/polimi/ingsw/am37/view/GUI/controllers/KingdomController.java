package it.polimi.ingsw.am37.view.GUI.controllers;

import it.polimi.ingsw.am37.model.cards.placeable.GameCard;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.sides.Side;
import it.polimi.ingsw.am37.view.clientmodel.ClientSidePlayer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private Map<String, GridPane> playerGrids = new HashMap<>();
    private Map<String, GridPane> playerInfos =  new HashMap<>();


    public void onLoad() {
        //SET OBJECTIVES
        publicObj1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"
                + guiReference.getLocalGameInstance().getPublicObjectives().get(0).getId() + ".png"))));
        publicObj2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"
                + guiReference.getLocalGameInstance().getPublicObjectives().get(1).getId() + ".png"))));
        privateObjective.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"
                + guiReference.getLocalGameInstance().getMyPrivateObjective().getId() + ".png"))));

        //SET INITIAL DECKS AND AVAILABLE CARDS
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
        player1Grid.getChildren().retainAll(player1Grid.getChildren().get(0));
        player2Grid.getChildren().retainAll(player2Grid.getChildren().get(0));
        player3Grid.getChildren().retainAll(player3Grid.getChildren().get(0));
        player4Grid.getChildren().retainAll(player4Grid.getChildren().get(0));

        player1Tab.setText(guiReference.getLocalGameInstance().getMe().getNickname());
        int scId = guiReference.getLocalGameInstance().getMyStartCard().getId();
        String side = understandSide(scId, guiReference.getLocalGameInstance().getMe().getKingdom().getPlacedSides().get(0));
        System.out.println("Placing side: " + side + "of card: " + scId);
        Image img;
        if (side == "f")
            img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"
                + scId + ".png")));
        else
            img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/back/"
                + scId + ".png")));
        ImageView iv = new ImageView(img);
        player1Grid.add(iv, 5,5);
        GridPane.setConstraints(iv, 5, 5, 1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER);

        p1Name.setText("You");
        p1Points.setText("Points: " + guiReference.getLocalGameInstance().getMe().getPoints());

        int i = 0;
        for (ClientSidePlayer p: guiReference.getLocalGameInstance().getPlayers()) {
            int scIdPl = getIdFromSide(p.getKingdom().getPlacedSides().get(0));
            String sidePl = understandSide(scId, p.getKingdom().getPlacedSides().get(0));
            System.out.println("Placing side: " + sidePl + " of card: " + scIdPl);
            Image imgPl;
            if (sidePl == "f")
                imgPl = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/front/"
                        + scIdPl + ".png")));
            else
                imgPl = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/back/"
                        + scIdPl + ".png")));
            ImageView ivPl = new ImageView(imgPl);
            if (i == 0) {
                player2Tab.setText(p.getNickname());

                player2Grid.add(ivPl, 5,5);
                GridPane.setConstraints(ivPl, 5, 5, 1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER);
                playerGrids.put(p.getNickname(), player2Grid);
                //PLAYER INFOS
                p2Name.setText(p.getNickname());
                p2Points.setText("Points: " + p.getPoints());
                playerInfos.put(p.getNickname(), p2Info);
            } else if (i == 1) {
                player3Tab.setText(p.getNickname());

                player3Grid.add(ivPl, 5,5);
                GridPane.setConstraints(ivPl, 5, 5, 1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER);
                playerGrids.put(p.getNickname(), player3Grid);
                //PLAYER INFOS
                p3Name.setText(p.getNickname());
                p3Points.setText("Points: " + p.getPoints());
                playerInfos.put(p.getNickname(), p3Info);
            } else if (i == 2) {
                player4Tab.setText(p.getNickname());

                player4Grid.add(ivPl, 5,5);
                GridPane.setConstraints(ivPl, 5, 5, 1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER);
                playerGrids.put(p.getNickname(), player4Grid);
                //PLAYER INFOS
                p4Name.setText(p.getNickname());
                p4Points.setText("Points: " + p.getPoints());
                playerInfos.put(p.getNickname(), p4Info);
            }
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

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
        if (found.getFront() == s)
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
