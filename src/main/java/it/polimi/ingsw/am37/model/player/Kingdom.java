package it.polimi.ingsw.am37.model.player;

import it.polimi.ingsw.am37.model.cards.objective.Direction;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.sides.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * The Kingdom class contains all the information and behaviour of the play area of a Player. The play area is where the
 * Player places his cards.
 */
public class Kingdom {
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
                activePositions.add(createPosition(d, startCardSide.getPositionInKingdom()));
                if (startCardSide.getCorners().get(d).getResource() != Resource.EMPTY) {
                    onFieldResources.put(startCardSide.getCorners().get(d).getResource(), onFieldResources.get(startCardSide.getCorners().get(d).getResource()) + 1);
                }
            } else if (!startCardSide.getCorners().get(d).getVisibility()) {
                impossiblePositions.add(createPosition(d, startCardSide.getPositionInKingdom()));
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
     * @param sidePlaced The Side of the Card that the player has placed.
     * @param position The Position where the Card is placed.
     */
    public void updateKingdom(Side sidePlaced, Position position) {
        updateOnFieldResources(sidePlaced);
        updatePlacedSides(sidePlaced);
        updateActivePositions(sidePlaced, position);
    }

    /**
     * The updateOnFieldResources(sidePlaced) method updates the table of Resources of the Player adding the ones
     * present on the Side of the Card placed and removing the ones covered by this Card's placement.
     * @param sidePlaced The Side of the Card that the player has placed.
     */
    private void updateOnFieldResources(Side sidePlaced) {       //DA RIFARE CON LE DIREZIONI

        for (Direction d : sidePlaced.getCorners().keySet()) {
            if(!sidePlaced.getCorners().get(d).getResource().equals(Resource.EMPTY)) {
                onFieldResources.replace(sidePlaced.getCorners().get(d.opposite()).getResource(), onFieldResources.get(sidePlaced.getCorners().get(d.opposite()).getResource()) + 1);
            }

            for (Side s: placedSides) {
                if(createPosition(d, sidePlaced.getPositionInKingdom()).getX() == s.getPositionInKingdom().getX() &&
                        createPosition(d, sidePlaced.getPositionInKingdom()).getY() == s.getPositionInKingdom().getY()) {
                    s.getCorners().get(d.opposite()).setLinkedSide(sidePlaced);
                    if(!s.getCorners().get(d.opposite()).getResource().equals(Resource.EMPTY)) {
                        onFieldResources.replace(s.getCorners().get(d.opposite()).getResource(), onFieldResources.get(s.getCorners().get(d.opposite()).getResource()) - 1);
                    }
                }
            }

        }
        //NON è GESTITO IL CASO BACK, NON HO MODO DI SAPERE SE IL SIDE è BACK E NON POSSO FARE CAST PROPRIO PER QUESTO.

        if (!sidePlaced.getTL().getResource().equals(Resource.EMPTY)) {
            if (!onFieldResources.containsKey(sidePlaced.getTL().getResource())) {
                onFieldResources.put(sidePlaced.getTL().getResource(), 1);
            } else {
                onFieldResources.replace(sidePlaced.getTL().getResource(), onFieldResources.get(sidePlaced.getTL().getResource()) + 1);
            }
        }
        if (!sidePlaced.getTR().getResource().equals(Resource.EMPTY)) {
            if (!onFieldResources.containsKey(sidePlaced.getTR().getResource())) {
                onFieldResources.put(sidePlaced.getTR().getResource(), 1);
            } else {
                onFieldResources.replace(sidePlaced.getTR().getResource(), onFieldResources.get(sidePlaced.getTR().getResource()) + 1);
            }
        }
        if (!sidePlaced.getBL().getResource().equals(Resource.EMPTY)) {
            if (!onFieldResources.containsKey(sidePlaced.getBL().getResource())) {
                onFieldResources.put(sidePlaced.getBL().getResource(), 1);
            } else {
                onFieldResources.replace(sidePlaced.getBL().getResource(), onFieldResources.get(sidePlaced.getBL().getResource()) + 1);
            }
        }
        if (!sidePlaced.getBR().getResource().equals(Resource.EMPTY)) {
            if (!onFieldResources.containsKey(sidePlaced.getBR().getResource())) {
                onFieldResources.put(sidePlaced.getBR().getResource(), 1);
            } else {
                onFieldResources.replace(sidePlaced.getBR().getResource(), onFieldResources.get(sidePlaced.getBR().getResource()) + 1);
            }
        }
        if (!onFieldResources.containsKey(sidePlaced.getMainResource())) {
            onFieldResources.put(sidePlaced.getMainResource(), 1);
        } else {
            onFieldResources.replace(sidePlaced.getMainResource(), onFieldResources.get(sidePlaced.getMainResource()) + 1);
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
            Position toAdd = createPosition(d, sidePlaced.getPositionInKingdom());
            if (sidePlaced.getCorners().get(d).getVisibility()) {
                if (!impossiblePositions.contains(toAdd) && !activePositions.contains(toAdd)) {
                    activePositions.add(createPosition(d, sidePlaced.getPositionInKingdom()));
                }
            } else if (!sidePlaced.getCorners().get(d).getVisibility() && !impossiblePositions.contains(toAdd)) {
                activePositions.remove(toAdd);
                impossiblePositions.add(createPosition(d, sidePlaced.getPositionInKingdom()));
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
     * The createPosition(d, p) method creates a new Position based on the direction that is given as a parameter.
     * @param d The Direction relative to the placed Side where the Position needs to be created.
     * @param p The Position of the Side that has just been placed.
     * @return A new Position, relative to the Position of the Side, that is in the Direction given as parameter.
     */
    private Position createPosition(Direction d, Position p) {
        return switch (d) {
            case Direction.TOPLEFT -> new Position(p.getX() - 1, p.getY() + 1);
            case Direction.TOPRIGHT -> new Position(p.getX() + 1, p.getY() + 1);
            case Direction.BOTTOMLEFT -> new Position(p.getX() - 1, p.getY() - 1);
            case Direction.BOTTOMRIGHT -> new Position(p.getX() + 1, p.getY() - 1);
        };
    }
}
