package it.polimi.ingsw.am37.model.player;

import it.polimi.ingsw.am37.model.cards.Card;
import it.polimi.ingsw.am37.model.cards.objective.Direction;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.sides.*;

import java.util.*;

/**
 * The Kingdom class contains all the information and behaviour of the play area of a Player. The play area is where the
 * Player places his cards.
 */
public class Kingdom {
    private final List<Resource> startCardResources;
    /**
     * The onFieldResources attribute is an object of type Hashtable<Resource, Integer> that maps all the values of the
     * Resource enumeration to an Integer, that represents the number of Resources of that type that the Player
     * currently possesses.
     */
    private final Hashtable<Resource, Integer> onFieldResources;
    /**
     * The placedSides attribute is a list that contains all the Sides that the Player has placed.
     */
    private final List<Side> placedSides;
    /**
     * The activePositions attribute is a list that contains all the Positions where a Card can be placed by the Player.
     */
    private final List<Position> activePositions;
    /**
     * The impossiblePositions attribute is a list that contains all the Positions where a Card can no longer be placed
     * for the entirety of the game. For example because if a Card were to be placed in this position it would cover an
     * inactive/invisible Corner.
     */
    private final List<Position> impossiblePositions;

    /**
     * The Kingdom(startCard, startCardSide) constructor
     * @param startCard The StartCard that the Player wants to place in the Kingdom.
     * @param startCardSide The Side of the StartCard that the Player wants to place.
     */
    public Kingdom(StartCard startCard, Side startCardSide) {
        if (startCard.getFront().equals(startCardSide))
            startCardResources = new ArrayList<>(startCard.getBackResource());
        else
            startCardResources = new ArrayList<>();

        placedSides = new ArrayList<>();
        placedSides.add(startCardSide);

        activePositions = new ArrayList<>();
        onFieldResources = new Hashtable<>();
        impossiblePositions = new ArrayList<>();

        for (Resource r: Resource.values()) {
            onFieldResources.put(r, 0);
        }

        startCardSide.placeInPosition(0,0);
        impossiblePositions.add(new Position(0,0));

        for (Direction d : startCardSide.getCorners().keySet()) {
            if (startCardSide.getCorners().get(d).getVisibility()) {
                activePositions.add(d.createPosition(startCardSide.getPositionInKingdom()));
                if (startCardSide.getCorners().get(d).getResource() != Resource.EMPTY) {
                    onFieldResources.put(startCardSide.getCorners().get(d).getResource(), onFieldResources.get(startCardSide.getCorners().get(d).getResource()) + 1);
                }
            } else if (!startCardSide.getCorners().get(d).getVisibility()) {
                impossiblePositions.add(d.createPosition(startCardSide.getPositionInKingdom()));
            }
        }

        if (startCardSide.equals(startCard.getFront())) {
            for (int i = 0; i < startCard.getBackResource().size(); i++) {
                onFieldResources.replace(startCard.getBackResource().get(i), onFieldResources.get(startCard.getBackResource().get(i)) + 1);
            }
        }
    }

    /**
     * The getOnFieldResources() method returns the table containing the current Resources of the Player.
     * @return The onFieldResources attribute.
     */
    public Hashtable<Resource, Integer> getOnFieldResources() {
        return onFieldResources;
    }

    /**
     * The getPlacedSides() method returns the list containing all the Sides placed by the Player.
     * @return The placedSides attribute.
     */
    public List<Side> getPlacedSides() {
        return placedSides;
    }

    /**
     * The getActivePositions() method returns the list containing all the Position currently active in the Kingdom,
     * meaning that a Card can be placed here.
     * @return The activePositions attribute.
     */
    public List<Position> getActivePositions() {
        return activePositions;
    }

    /**
     * The getImpossiblePositions() method returns the list containing all the Position where it is impossible to place
     * a Card, meaning that during the entire game, the Player will never be able to place a Card here.
     * @return The impossiblePositions method.
     */
    public List<Position> getImpossiblePositions() {
        return impossiblePositions;
    }

    /**
     * The updateKingdom(sidePlaced, position) method is called every time the player successfully places a Card and
     * invokes private methods that update the current state of the Kingdom.
     * @param card The Card that the Player has placed down.
     * @param sidePlaced The Side of the Card that the player has placed.
     * @param position The Position where the Card is placed.
     */
    public void updateKingdom(StandardCard card, Side sidePlaced, Position position) {
        updateOnFieldResources(card, sidePlaced);
        updatePlacedSides(sidePlaced);
        updateActivePositions(sidePlaced, position);
    }

    /**
     * The updateOnFieldResources(sidePlaced) method updates the table of Resources of the Player adding the ones
     * present on the Side of the Card placed and removing the ones covered by this Card's placement.
     * @param card The Card that the Player has placed down.
     * @param sidePlaced The Side of the Card that the player has placed.
     */
    private void updateOnFieldResources(StandardCard card, Side sidePlaced) {
        if (card.getFront().equals(sidePlaced)) {
            for (Direction d : Direction.values()) {
                if (!sidePlaced.getCorners().get(d).getResource().equals(Resource.EMPTY)) {
                    onFieldResources.replace(sidePlaced.getCorners().get(d).getResource(), onFieldResources.get(sidePlaced.getCorners().get(d).getResource()) + 1);
                }

            }
        } else if (card.getBack().equals(sidePlaced))
            onFieldResources.replace(sidePlaced.getMainResource(), onFieldResources.get(sidePlaced.getMainResource()) + 1);

        for (Direction d : Direction.values()) {
            for (Side s : placedSides) {
                if (s.getCorners().get(d).getLinkedSide() != null) {
                    if (s.getCorners().get(d).getLinkedSide().equals(sidePlaced) && !s.getCorners().get(d).getResource().equals(Resource.EMPTY))
                        onFieldResources.replace(s.getCorners().get(d).getResource(), onFieldResources.get(s.getCorners().get(d).getResource()) - 1);
                }
            }
        }
    }

    /**
     * The updateActivePositions(sidePlaced, position) method updates the list of Positions where a Card can be placed
     * by the Player. It also updates the impossiblePositions list, that contains all the Positions where it is
     * impossible to place the Card.
     * @param sidePlaced The Side of the Card that the player has placed.
     * @param position The Position where the Card is placed.
     */
    private void updateActivePositions(Side sidePlaced, Position position) {
        activePositions.remove(position);
        impossiblePositions.add(position);

        for (Direction d : sidePlaced.getCorners().keySet()) {
            Position toAdd = d.createPosition(sidePlaced.getPositionInKingdom());
            if (sidePlaced.getCorners().get(d).getVisibility()) {
                if (!impossiblePositions.contains(toAdd) && !activePositions.contains(toAdd)) {
                    activePositions.add(d.createPosition(sidePlaced.getPositionInKingdom()));
                }
            } else if (!sidePlaced.getCorners().get(d).getVisibility() && !impossiblePositions.contains(toAdd)) {
                activePositions.remove(toAdd);
                impossiblePositions.add(d.createPosition(sidePlaced.getPositionInKingdom()));
            }
        }
    }

    /**
     * The updatePlacedSides(sidePlaced) method updates the list of Sides that are placed in the Kingdom.
     * @param sidePlaced The Side of the Card that the player has placed.
     */
    private void updatePlacedSides(Side sidePlaced) {
        this.placedSides.add(sidePlaced);
    }

    /**
     * the getVisual method prints the kingdom of the player based on the encoding chosen.
     * @param utfActive tells if the encoding is enabled or not.
     * @return a string representing the kingdom of the player.
     */
    public String[][] getVisual(boolean utfActive) {
        String[][] field = new String[100][100];
        Map<Resource, String> colourMap = new HashMap<>();
        Map<Resource, String> resMap = new HashMap<>();

        int xCoord = 50;
        int yCoord = 50;

        int minX = 100;
        int maxX = 0;
        int minY = 100;
        int maxY = 0;

        if (utfActive) {
            for (int i = 0; i < 100; i++)
                for (int j = 0; j < 100; j++)
                    field[i][j] = "⬛";//"⠀⠀"


            colourMap.put(Resource.ANIMAL, "🟦");
            colourMap.put(Resource.PLANT, "🟩");
            colourMap.put(Resource.INSECT, "🟪");
            colourMap.put(Resource.FUNGI, "🟥");
            colourMap.put(Resource.EMPTY, "🟨");

            resMap = Card.resourceToString(utfActive);
            resMap.put(Resource.EMPTY, "⬜");

        } else {
            for (int i = 0; i < 100; i++)
                for (int j = 0; j < 100; j++)
                    field[i][j] = "E";


            colourMap.put(Resource.ANIMAL, "B");
            colourMap.put(Resource.PLANT,  "G");
            colourMap.put(Resource.INSECT, "P");
            colourMap.put(Resource.FUNGI,  "R");
            colourMap.put(Resource.EMPTY,  "Y");

            resMap.put(Resource.ANIMAL,     "A");
            resMap.put(Resource.PLANT,      "P");
            resMap.put(Resource.INSECT,     "I");
            resMap.put(Resource.FUNGI,      "F");
            resMap.put(Resource.INKWELL,    "N");
            resMap.put(Resource.MANUSCRIPT, "M");
            resMap.put(Resource.QUILL,      "Q");
            resMap.put(Resource.EMPTY,      " ");
        }



        for (Position pos: activePositions) {
            int xActive = xCoord - pos.getY() * 2;
            int yActive = yCoord + pos.getX() * 2;

            if (xActive - 1 < minX)
                minX = xActive - 1;
            if (xActive + 1 > maxX)
                maxX = xActive + 1;

            if (yActive - 1 < minY)
                minY = yActive - 1;
            if (yActive + 1 > maxY)
                maxY = yActive + 1;

            if (utfActive) {
                for (int i = -1; i <= 1; i++)
                    for (int j = -1; j <= 1; j++)
                        field[xActive + i][yActive + j] = "⬜";
            } else {
                for (int i = -1; i <= 1; i++)
                    for (int j = -1; j <= 1; j++)
                        field[xActive + i][yActive + j] = "W";
            }
        }

        for (Side s: placedSides) {
            int sideXPos = xCoord - s.getPositionInKingdom().getY() * 2;
            int sideYPos = yCoord + s.getPositionInKingdom().getX() * 2;

            if (sideXPos - 1 < minX)
                minX = sideXPos - 1;
            if (sideXPos + 1 > maxX)
                maxX = sideXPos + 1;

            if (sideXPos - 1 < minY)
                minY = sideXPos - 1;
            if (sideXPos + 1 > maxY)
                maxY = sideXPos + 1;

            field[sideXPos + 1][sideYPos] = colourMap.get(s.getMainResource());
            field[sideXPos - 1][sideYPos] = colourMap.get(s.getMainResource());
            field[sideXPos][sideYPos + 1] = colourMap.get(s.getMainResource());
            field[sideXPos][sideYPos - 1] = colourMap.get(s.getMainResource());

            if (s.getClass().equals(Front.class)) {
                field[sideXPos][sideYPos] = colourMap.get(s.getMainResource());
            } else {
                field[sideXPos][sideYPos] = resMap.get(s.getMainResource());
            }


            if (!s.getTL().getVisibility())
                field[sideXPos - 1][sideYPos - 1] = colourMap.get(s.getMainResource());
            else if (s.getTL().getLinkedSide() == null)
                field[sideXPos - 1][sideYPos - 1] = resMap.get(s.getTL().getResource());

            if (!s.getTR().getVisibility())
                field[sideXPos - 1][sideYPos + 1] = colourMap.get(s.getMainResource());
            else if (s.getTR().getLinkedSide() == null)
                field[sideXPos - 1][sideYPos + 1] = resMap.get(s.getTR().getResource());

            if (!s.getBL().getVisibility())
                field[sideXPos + 1][sideYPos - 1] = colourMap.get(s.getMainResource());
            else if (s.getBL().getLinkedSide() == null)
                field[sideXPos + 1][sideYPos - 1] = resMap.get(s.getBL().getResource());

            if (!s.getBR().getVisibility())
                field[sideXPos + 1][sideYPos + 1] = colourMap.get(s.getMainResource());
            else if (s.getBR().getLinkedSide() == null)
                field[sideXPos + 1][sideYPos + 1] = resMap.get(s.getBR().getResource());
        }

        if (!startCardResources.isEmpty()){
            field[xCoord][yCoord] = resMap.get(startCardResources.get(0));
            if (startCardResources.size() >= 2) {
                field[xCoord + 1][yCoord] = resMap.get(startCardResources.get(1));
            }
            if (startCardResources.size() == 3) {
                field[xCoord - 1][yCoord] = resMap.get(startCardResources.get(2));
            }
        } else
            field[xCoord][yCoord] = colourMap.get(Resource.EMPTY);

        String[][] reducedField = new String[maxX - minX + 1][maxY - minY + 1];
        for (int i = 0; i < maxX - minX + 1; i++)
            if (maxY - minY + 1 >= 0) System.arraycopy(field[minX + i], minY, reducedField[i], 0, maxY - minY + 1);

        return reducedField;
    }
}
