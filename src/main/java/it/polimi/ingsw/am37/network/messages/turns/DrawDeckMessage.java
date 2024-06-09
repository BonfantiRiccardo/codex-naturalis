package it.polimi.ingsw.am37.network.messages.turns;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.exceptions.*;
import it.polimi.ingsw.am37.network.messages.mixed.ErrorMessage;
import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToServer;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.game.GameStatus;
import it.polimi.ingsw.am37.model.game.PlayerPoints;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.network.server.ClientHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Message used to draw a card from the deck.
 */
public class DrawDeckMessage extends MessageToServer {
    /**
     * The player that wants to draw a card.
     */
    private final String player;
    /**
     * The deck from which the player wants to draw the card.
     */
    private final String deck;

    /**
     * Constructor of the message.
     * @param id The id of the message.
     * @param player The player that wants to draw a card.
     * @param deck The deck from which the player wants to draw the card.
     */
    public DrawDeckMessage(MessageId id, String player, String deck) {
        super(id);
        this.player = player;
        this.deck = deck;
    }

    /**
     * Method that decodes the message and executes the action.
     * If the player is not logged, it sends an error message.
     * If the player is logged, it puts the card from the deck to the player's hand
     * If the deck is empty, it sends an error message.
     * If the deck is not empty, it sends the new card to the player and the new deck to the other players.
     * If the game is over, it sends the results to the players.
     * @param c The controller of the game.
     * @param ch The client handler that sent the message.
     */
    @Override
    public void decodeAndExecute(GameController c, ClientHandler ch) {
        if (c == null) {
            ch.send(new ErrorMessage(MessageId.ERROR, "You are not logged"));
            return;
        }

        boolean inEndGame = c.isEndGameStarted();

        boolean drawn = false;
        List<Integer> currentHand = new ArrayList<>();

        for (Player p: c.getGameInstance().getParticipants()) {
            if (p.getNickname().equalsIgnoreCase(player)) {

                for (StandardCard card: p.getHand())
                    currentHand.add(card.getId());

                if (deck.equalsIgnoreCase("r")) {
                    try {
                        c.playerDrawsCardFromDeck(p, c.getGameInstance().getRDeck());
                        System.out.println("Correctly drawn card for player: " + player);
                        drawn = true;
                    } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                             AlreadyAssignedException e) {
                        ch.send(new ErrorMessage(MessageId.ERROR, e.getMessage()));
                        return;
                    }
                } else if (deck.equalsIgnoreCase("g")) {
                    try {
                        c.playerDrawsCardFromDeck(p, c.getGameInstance().getGDeck());
                        System.out.println("Correctly drawn card for player: " + player);
                        drawn = true;
                    } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                             AlreadyAssignedException e) {
                        ch.send(new ErrorMessage(MessageId.ERROR, e.getMessage()));
                        return;
                    }
                }


                if(!drawn) {
                    ch.send(new ErrorMessage(MessageId.ERROR, "Couldn't draw the card because message is corrupted."));
                } else {

                    if (c.getGameInstance().getCurrentStatus().equals(GameStatus.OVER)) {
                        PlayerPoints[] results = c.getGameInstance().getGameWinner();
                        for (Player pl: c.getGameInstance().getParticipants())
                            c.getPlayerViews().get(pl).sendResults(results);

                        return;
                    }


                    int newCard = -1;
                    for (StandardCard card: p.getHand()) {
                        if (!currentHand.contains(card.getId())) {
                            newCard = card.getId();
                            break;
                        }

                    }

                    if (newCard != -1) {
                        if (deck.equalsIgnoreCase("r"))
                            c.getPlayerViews().get(p).updatePlayerHandAndDeckView(deck, c.getGameInstance().getRDeck().firstBack().getMainResource(), newCard);
                        else if (deck.equalsIgnoreCase("g"))
                            c.getPlayerViews().get(p).updatePlayerHandAndDeckView(deck, c.getGameInstance().getGDeck().firstBack().getMainResource(), newCard);
                    } else
                        ch.send(new ErrorMessage(MessageId.ERROR, "The card was drawn but something went wrong"));

                    for (Player pl: c.getGameInstance().getParticipants()) {
                        if (!pl.getNickname().equals(player)) {
                            if (deck.equalsIgnoreCase("r"))
                                c.getPlayerViews().get(pl).updatesDeckView(deck, c.getGameInstance().getRDeck().firstBack().getMainResource());
                            else if (deck.equalsIgnoreCase("g"))
                                c.getPlayerViews().get(pl).updatesDeckView(deck, c.getGameInstance().getGDeck().firstBack().getMainResource());
                        }
                    }

                    //notify if endgame
                    if (c.isEndGameStarted() && !inEndGame) {
                        for (Player pl: c.getGameInstance().getParticipants()) {
                            c.getPlayerViews().get(pl).acknowledgePlayer(pl, "endgame");
                        }
                    }

                    //notify last turn
                    if (c.getGameInstance().getTurnCounter() == c.getGameInstance().getLastTurn()) { //LAST TURN MIGHT BE UNINITIALIZED
                        for (Player pl: c.getGameInstance().getParticipants()) {
                            c.getPlayerViews().get(pl).acknowledgePlayer(pl, "last turn");
                        }
                    }

                    //notify next turn
                    c.getPlayerViews().get(c.getGameInstance().getCurrentTurn()).notifyTurn(c.getGameInstance().getCurrentTurn());
                }

                break;
            }
        }
    }

    /**
     * Method that returns the player that wants to draw a card.
     * @return The player that wants to draw a card.
     */
    @Override
    public String toString() {
        return "Received: " + super.toString() + " | player: " + player + " | deck: " + deck;
    }
}
