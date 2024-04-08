package it.polimi.ingsw.am37.model.player;

import it.polimi.ingsw.am37.model.cards.objective.Direction;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.sides.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Kingdom {
    private final Hashtable<Resource, Integer> onFieldResources;
    private final List<Position> activePositions;
    private final List<Side> placedSides;
    private final List<Position> impossiblePositions;

    public Kingdom(StartCard startCard, Side startCardSide) {
        placedSides = new ArrayList<>();
        placedSides.add(startCardSide);
        activePositions = new ArrayList<>();
        onFieldResources = new Hashtable<>();
        impossiblePositions = new ArrayList<>();

        for (Direction d : startCardSide.getCorners().keySet()) {
            if (startCardSide.getCorners().get(d).getVisibility()) {
                switch (d) {
                    case Direction.TOPLEFT: activePositions.add(new Position(-1, 1));
                    case Direction.TOPRIGHT: activePositions.add(new Position(1, 1));
                    case Direction.BOTTOMLEFT: activePositions.add(new Position(-1, -1));
                    case Direction.BOTTOMRIGHT: activePositions.add(new Position(1, -1));
                }
                if (startCardSide.getCorners().get(d).getResource() != Resource.EMPTY) {
                    onFieldResources.put(startCardSide.getCorners().get(d).getResource(), 1);
                }
            } else if (!startCardSide.getCorners().get(d).getVisibility()) {
                switch (d) {
                    case Direction.TOPLEFT: impossiblePositions.add(new Position(-1, 1));
                    case Direction.TOPRIGHT: impossiblePositions.add(new Position(1, 1));
                    case Direction.BOTTOMLEFT: impossiblePositions.add(new Position(-1, -1));
                    case Direction.BOTTOMRIGHT: impossiblePositions.add(new Position(1, -1));
                }
            }

            if (startCardSide.equals(startCard.getFront())) {
                for (int i = 0; i < startCard.getBackResource().size(); i++) {
                    if (!onFieldResources.containsKey(startCard.getBackResource().get(i))) {
                        onFieldResources.put(startCard.getBackResource().get(i), 1);
                    } else {
                        onFieldResources.replace(startCard.getBackResource().get(i), onFieldResources.get(startCard.getBackResource().get(i)) + 1);
                    }
                }
            }
        }
    }

    public Hashtable<Resource, Integer> getOnFieldResources() {
        return onFieldResources;
    }

    public void updateOnFieldResources(Side sidePlaced) {       //DA RIFARE CON LE DIREZIONI
        if (!sidePlaced.getTL().getResource().equals("EMPTY")) {
            if (!onFieldResources.containsKey(sidePlaced.getTL().getResource())) {
                onFieldResources.put(sidePlaced.getTL().getResource(), 1);
            } else {
                onFieldResources.replace(sidePlaced.getTL().getResource(), onFieldResources.get(sidePlaced.getTL().getResource()) + 1);
            }
        }
        if (!sidePlaced.getTR().getResource().equals("EMPTY")) {
            if (!onFieldResources.containsKey(sidePlaced.getTR().getResource())) {
                onFieldResources.put(sidePlaced.getTR().getResource(), 1);
            } else {
                onFieldResources.replace(sidePlaced.getTR().getResource(), onFieldResources.get(sidePlaced.getTR().getResource()) + 1);
            }
        }
        if (!sidePlaced.getBL().getResource().equals("EMPTY")) {
            if (!onFieldResources.containsKey(sidePlaced.getBL().getResource())) {
                onFieldResources.put(sidePlaced.getBL().getResource(), 1);
            } else {
                onFieldResources.replace(sidePlaced.getBL().getResource(), onFieldResources.get(sidePlaced.getBL().getResource()) + 1);
            }
        }
        if (!sidePlaced.getBR().getResource().equals("EMPTY")) {
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

    public List<Position> getActivePositions() {
        return activePositions;
    }

    public void updateActivePositions(Side sidePlaced, Position position) {
        //TODO aggiorna le posizioni su cui è piazzabile una carta aggiungendo quelli nuovi e togliendo quello usato
    }

    public List<Side> getPlacedSides() {
        return placedSides;
    }

    public void setPlacedSides(Side sidePlaced) {
        this.placedSides.add(sidePlaced);
    }

    public List<Position> getImpossiblePositions() {
        return impossiblePositions;
    }
}

