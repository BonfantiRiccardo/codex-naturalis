package it.polimi.ingsw.am37.model.player;

import it.polimi.ingsw.am37.model.cards.*;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.decks.Deck;
import it.polimi.ingsw.am37.model.exceptions.AlreadyAssignedException;
import it.polimi.ingsw.am37.model.game.GameModel;
import it.polimi.ingsw.am37.model.sides.*;

import java.util.List;
import java.util.Scanner;

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
    private List<Card> hand;
    /**
     * The startCard attribute is a reference to the start card that the player receives at the beginning of the game.
     */
    private Card startCard;
    /**
     * The privateObjective attribute is the ObjectiveCard that the player chooses at the beginning of the game.
     */
    private  Card privateObjective;
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
    public void setHand(List<Card> hand) throws AlreadyAssignedException {
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
    public List<Card> getHand() {
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
    public void setStartCard(Card startCard) throws AlreadyAssignedException {
        if(this.startCard != null) {
            throw new AlreadyAssignedException("The startCard has already been assigned");
        } else {
            this.startCard = startCard;
        }
    }

    public void chooseStartCardSide() {
        //TODO
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
     * @param privateObjective An array of two ObjectiveCards.
     * @throws AlreadyAssignedException This attribute cannot be assigned twice.
     */
    public void chooseObjective(Card[] privateObjective) throws AlreadyAssignedException {
        if(this.privateObjective != null) {
            throw new AlreadyAssignedException("The privateObjective has already been assigned");
        } else {
            Scanner in = new Scanner(System.in);
            System.out.println("Choose one of these two objective (type 0 for the first and 1 for the second):");
            System.out.println(privateObjective[0]);
            System.out.println(privateObjective[1]);
            this.privateObjective = privateObjective[in.nextInt()];
            System.out.println("You chose: " + this.privateObjective);
        }
    }

    /**
     * The getPrivateObjective() method returns the private objective of the player and will be called during the
     * points' final calculation after the endgame phase.
     * @return The privateObjective attribute.
     */
    public Card getPrivateObjective() {
        return privateObjective;
    }

    private void instantiateMyKingdom(StartCard sC, Side startSide) throws AlreadyAssignedException {
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


    public void drawCardFromDeck(Deck deck) {
        //TODO
        //check se la lista hand ha 3 carte allora ritorna "Impossibile pescare le carte in mano sono già 3"

        //altrimenti hand.add(deck.drawCard());
    }

    public void drawCardFromAvailable(Card card) {
        //TODO
        //check se la lista hand ha 3 carte allora ritorna "Impossibile pescare le carte in mano sono già 3"

        //altrimenti hand.add(card);        game.getAvail..().remove(card);     game.getAvail..().add(deck.drawCard());
    }

    public void placeCard(Side placed, Position position) {
        //TODO
        //controllo sulla piazzabilità dalla lista di posizioni, se tutto ok linko i corner delle carte sotto al side
        // piazzato (li trovo tramite posizioni adiacenti)
        //aggiunge placed alla lista di sides piazzati in kingdom
        //aggiungo le nuove posizioni aperte e aggiorno la lista di quelle impossibili
        //aggiorno le risorse visibili

        //altrimenti dà errore di piazzamento

        //chiama addPoints per aggiornare i punti passando placed
    }

    private void addPoints(Side placed) {
        //TODO
        //controlla se e quanti sono i punti da aggiungere a seguito del piazzamento di questa carta

        //currPoints = game.getScoreboard().getParticipantsPoints().get(token);
        //game.getScoreboard().getParticipantsPoints().replace(token, currPoints + toAdd)
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
