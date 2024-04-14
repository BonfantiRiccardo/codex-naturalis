package it.polimi.ingsw.am37.model.player;

import it.polimi.ingsw.am37.model.cards.*;
import it.polimi.ingsw.am37.model.cards.objective.*;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.decks.Deck;
import it.polimi.ingsw.am37.model.exceptions.*;
import it.polimi.ingsw.am37.model.game.*;
import it.polimi.ingsw.am37.model.sides.*;

import java.util.List;

/**
 * The Player class contains all the information and behaviour of a user of the application.
 */
public class Player {
    /**
     * The nickname attribute is a String that uniquely identifies the player.
     */
    private final String nickname;
    /**
     * The token attribute represents the player colour and is one of the values of the Token Enumeration.
     * It also uniquely identifies the player.
     */
    private final Token token;
    /**
     * The game attribute references the game that the player is participating to.
     */
    private GameModel game;
    /**
     * The hand attribute is the list of cards that the player currently has at its disposal, meaning that he can place
     * these cards.
     */
    private List<StandardCard> hand;
    /**
     * The startCard attribute is a reference to the start card that the player receives at the beginning of the game.
     */
    private StartCard startCard;
    /**
     * The privateObjective attribute is the ObjectiveCard that the player chooses at the beginning of the game.
     */
    private ObjectiveCard privateObjective;
    /**
     * The myKingdom attribute is a reference to the kingdom (also called play area) of the player. The Kingdom contains
     * the sides of the cards placed by the player and his resources.
     */
    private Kingdom myKingdom;
    /**
     * The isDisconnected attribute gives information about the current state of connection with the player.
     */
    private boolean isDisconnected;

    /**
     * The Player(nickname, token) constructor sets the nickname and the token of the player to the ones given as
     * parameters. It also sets the isDisconnected attribute to false.
     * @param nickname A string that uniquely identifies the player.
     * @param token A Token object that uniquely identifies the player.
     */
    public Player(String nickname, Token token) {
        this.nickname = nickname;
        this.token = token;
        isDisconnected = false;
    }

    /**
     * The getNickname() method returns the nickname of the player, which is a string.
     * @return The nickname attribute.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * The getToken() method returns the token related to the player.
     * @return The token attribute.
     */
    public Token getToken() {
        return token;
    }

    /**
     * The setGame(game) method checks if the player already has a linked game and if it hasn't, it links the player
     * to the game he is currently playing, which is given as a parameter. It is only called once, at the beginning of
     * the game. If it is called a second time, the method will throw an AlreadyAssignedException.
     * @param game A GameModel object where the player is part of the list of the participants.
     * @throws AlreadyAssignedException This attribute cannot be assigned twice.
     */
    public void setGame(GameModel game) throws AlreadyAssignedException {
        if (this.game != null) {
            throw new AlreadyAssignedException("The game has already been assigned");
        } else
            this.game = game;
    }

    /**
     * The getGame() method returns the game where the player is currently playing.
     * @return The game attribute.
     */
    public GameModel getGame() {
        return game;
    }

    /**
     * The setHand(hand) method checks if the hand of the player has been initialized and if it hasn't it assigns the hand
     * attribute to the list of cards given as parameters. It is only called once, at the beginning of the game. If
     * it is called a second time, the method will throw an AlreadyAssignedException.
     * @param hand A list of card that will become the initial hand of the player.
     * @throws AlreadyAssignedException This attribute cannot be assigned twice.
     */
    public void setHand(List<StandardCard> hand) throws AlreadyAssignedException {
        if(this.hand != null) {
            throw new AlreadyAssignedException("The hand has already been created");
        } else {
            this.hand = hand;
        }
    }

    /**
     * The getHand() method returns the current hand of the player, which is a list of three GameCards.
     * @return The hand attribute.
     */
    public List<StandardCard> getHand() {
        return hand;
    }

    /**
     * The setStartCard(startCard) method checks if the startCard has already been assigned to the player and if it
     * hasn't it assigns the startCard attribute to the card given as a parameter. *It also lets him choose a side of
     * the card to place. Finally, it creates the player's kingdom... * It is only called once, at the beginning of the
     * game. If it is called a second time, the method will throw an AlreadyAssignedException.
     * @param startCard A card of type startCard.
     * @throws AlreadyAssignedException This attribute cannot be assigned twice.
     */
    public void setStartCard(StartCard startCard) throws AlreadyAssignedException {
        if(this.startCard != null) {
            throw new AlreadyAssignedException("The startCard has already been assigned");
        } else {
            this.startCard = startCard;
        }
    }

    /**
     * The chooseStartCardSide() waits for the player to choose the StartCard Side he wants to place down and then calls
     * the instantiateMyKingdom(sC, startSide) method to create the Players Kingdom.
     */
    public void chooseStartCardSide() {
        try {
            instantiateMyKingdom(startCard, startCard.getFront());
            //game.getController().playerHasToChooseStartCardSide(this, startCard);//talks to controller that sends request to client
        } catch (/*WrongGamePhaseException |*/ AlreadyAssignedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The getStartCard() method returns the startCard assigned to the player at the beginning of the game.
     * @return The startCard attribute.
     */
    public Card getStartCard() {
        return startCard;
    }

    /**
     * The chooseObjective(privateObjective) method checks if the privateObjective has already been chosen by the player
     * and if it hasn't it lets the player decide which card of the two given as a parameter he wants to keep as his
     * private objective. It is only called once, at the beginning of the game. If it is called a second time, the
     * method will throw an AlreadyAssignedException.
     * @param objectiveArray An array of two ObjectiveCards.
     */
    public void chooseObjective(ObjectiveCard[] objectiveArray) {
        try {
            setPrivateObjective(objectiveArray[0]);
            //game.getController().playerHasToChooseObjective(this, objectiveArray); //talks to the controller and asks client which card
        } catch (/*WrongGamePhaseException |*/ AlreadyAssignedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The setPrivateObjective(c) method sets the Player's private ObjectiveCard to the one given as a parameter.
     * @param c The ObjectiveCard that the player has chosen.
     * @throws AlreadyAssignedException Throws this exception if the private Objective has already been assigned.
     */
    public void setPrivateObjective(ObjectiveCard c) throws AlreadyAssignedException {
        if(this.privateObjective != null) {
            throw new AlreadyAssignedException("The privateObjective has already been assigned");
        } else {
            this.privateObjective = c;
        }
    }

    /**
     * The getPrivateObjective() method returns the private objective of the player and will be called during the
     * points' final calculation after the endgame phase.
     * @return The privateObjective attribute.
     */
    public ObjectiveCard getPrivateObjective() {
        return privateObjective;
    }

    /**
     * The instantiateMyKingdom(sC, startSide) method creates a new Kingdom for the Player with the StartCard and Side
     * that are given as parameters.
     * @param sC The StartCard that the Player drew.
     * @param startSide The StartCard Side that the Player chose.
     * @throws AlreadyAssignedException Throws this exception if the Kingdom has already been created.
     */
    public void instantiateMyKingdom(StartCard sC, Side startSide) throws AlreadyAssignedException {
        if (this.myKingdom != null) {
            throw new AlreadyAssignedException("The Kingdom cannot be created twice");
        } else
            this.myKingdom = new Kingdom(sC, startSide);
    }

    /**
     * The getMyKingdom() method returns a reference to the kingdom of the player.
     * @return The myKingdom attribute.
     */
    public Kingdom getMyKingdom() {
        return myKingdom;
    }

    /**
     * The drawCardFromDeck(deck) method adds a Card to the hand of the player by drawing it from the deck given as a
     * parameter.
     * @param deck The deck from which the Player has chosen to draw the Card.
     */
    public void drawCardFromDeck(Deck deck) throws NoCardsException {
        if (hand.size() >= 3)
            System.out.println("You cannot draw, you already have 3 Cards in your hand");
        else {
            hand.add((StandardCard) deck.drawCard());
            System.out.println("Your hand now: ");
            for (StandardCard sC : hand) System.out.println(sC.toString());
        }
    }

    /**
     * The drawCardFromAvailable(card) method adds the available Card chosen by the player to his hand. Then it removes
     * the available Card from the list of available Cards and draws a new Card from the corresponding deck and adds it
     * to the available Cards.
     * @param card The card that the Player wants to draw.
     */
    public void drawCardFromAvailable(StandardCard card) throws NoCardsException {
        if (hand.size() >= 3)
            System.out.println("You cannot draw, you already have 3 Cards in your hand");
        else if (game.getAvailableGCards().contains(card)) {
            hand.add(card);
            game.getAvailableGCards().remove(card);
            if (!game.getGDeck().isEmpty()) {
                game.getAvailableGCards().add((StandardCard) game.getGDeck().drawCard());
            } else if (!game.getRDeck().isEmpty()) {
                game.getAvailableGCards().add((StandardCard) game.getRDeck().drawCard());
            }
            System.out.println("Your hand now: ");
            for (StandardCard sC : hand) System.out.println(sC.toString());

        } else if (game.getAvailableRCards().contains(card)) {
            hand.add(card);
            game.getAvailableRCards().remove(card);
            if (!game.getRDeck().isEmpty()) {
                game.getAvailableRCards().add((StandardCard) game.getRDeck().drawCard());
            } else if (!game.getGDeck().isEmpty()) {
                game.getAvailableRCards().add((StandardCard) game.getGDeck().drawCard());
            }
            System.out.println("Your hand now: ");
            for (StandardCard sC : hand) System.out.println(sC.toString());
        } else
            System.out.println("The Card you want to draw is not available");
    }

    /**
     * The placeCard(placed, position) method checks if the Side given as a parameter is the Side of one of the Card in
     * the Player's hand, then it checks if the Position given as a parameter is part of the list of currently active
     * positions in the Kingdom. Finally, it checks for any given placementCondition of the Card (only if the Side
     * placed is a Front) and if it is fulfilled it places the Card and updates the Kingdom by calling the
     * updateKingdom() method. If any of these check fails it doesn't place the Card and lets the player know that the
     * command failed.
     * @param card The Card that the Player wants to place from his hand.
     * @param toPlace The Side of the Card that the Player wants to place.
     * @param position The Position where the Player wants to place the Side.
     */
    public void placeCard(StandardCard card, Side toPlace, Position position) {
        int cornersLinked = 0;

        if (!hand.contains(card)) {
            System.out.println("You do not possess this Card, you cannot place it.");
        } else if (hand.contains(card) && !myKingdom.getActivePositions().contains(position)) {
            System.out.println("You cannot place the Card in this position.");
        } else if (hand.contains(card) && myKingdom.getActivePositions().contains(position)) {
            if (card.getFront().equals(toPlace)) {
                Front f = (Front) toPlace;
                if (f.isPlacementConditionSatisfied(myKingdom.getOnFieldResources())) {
                    toPlace.placeInPosition(position.getX(), position.getY());              //OVERLOAD THE METHOD TO NOT DUPLICATE CODE
                    hand.remove(card);
                    for (Side s : myKingdom.getPlacedSides()) {
                        for (Direction d : Direction.values()) {
                            if (d.createPosition(s.getPositionInKingdom()).equals(position)) {
                                s.getCorners().get(d).setLinkedSide(toPlace);
                                cornersLinked++;
                            }
                        }
                    }
                    addPoints(f, cornersLinked);
                    myKingdom.updateKingdom(card, toPlace, position);

                } else
                    System.out.println("You don't have enough resources to satisfy the placement condition for the front of this card, you cannot place it.");
            } else if (card.getBack().equals(toPlace)) {
                toPlace.placeInPosition(position.getX(), position.getY());
                hand.remove(card);
                for (Side s : myKingdom.getPlacedSides()) {
                    for (Direction d : Direction.values()) {
                        if (d.createPosition(s.getPositionInKingdom()).equals(position))
                            s.getCorners().get(d).setLinkedSide(toPlace);
                    }
                }
                myKingdom.updateKingdom(card, toPlace, position);
            } else
                System.out.println("The Side given as a parameter does not belong to the Card given as a parameter, you cannot place it.");

        }
    }

    /**
     * The addPoints(placed) method is called by the placeCard(placed, position) method if the command is successful and
     * only if the Side placed is a Front. It updates the points of the Player in the scoreboard.
     * @param placed The Side that the Player placed.
     */
    private void addPoints(Front placed, int cornersLinked) {
        int toAdd = 0;

        if (placed.getBonus() == null)
            toAdd = placed.getPointsGivenOnPlacement();
        else if (placed.getBonus().equals(Bonus.CORNER))
            toAdd = placed.getPointsGivenOnPlacement() * cornersLinked;
        else {
            if (placed.getBonus().equals(Bonus.INKWELL)) {
                toAdd = (myKingdom.getOnFieldResources().get(Resource.INKWELL) + 1) * placed.getPointsGivenOnPlacement();
            } else if (placed.getBonus().equals(Bonus.QUILL)) {
                toAdd = (myKingdom.getOnFieldResources().get(Resource.QUILL) + 1) * placed.getPointsGivenOnPlacement();
            } else if (placed.getBonus().equals(Bonus.MANUSCRIPT))
                toAdd = (myKingdom.getOnFieldResources().get(Resource.MANUSCRIPT) + 1) * placed.getPointsGivenOnPlacement();
        }

        game.getScoreboard().addPoints(token, toAdd);
    }

    /**
     * The isDisconnected() method returns the current state of the connection with the player's client.
     * @return true if the player is disconnected, false if the player is connected.
     */
    public boolean isDisconnected() {
        return isDisconnected;
    }

    /**
     * The setDisconnected(connectionStatus) method updates the connection status of the player's client to the one
     * given as a parameter.
     * @param connectionStatus A boolean that is true if the player's client has disconnected, false if it has
     *                         reconnected.
     */
    public void setDisconnected(boolean connectionStatus) {
        isDisconnected = connectionStatus;
    }

}
