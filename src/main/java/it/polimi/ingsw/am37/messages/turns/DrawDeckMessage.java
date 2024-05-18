package it.polimi.ingsw.am37.messages.turns;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.exceptions.*;
import it.polimi.ingsw.am37.messages.ErrorMessage;
import it.polimi.ingsw.am37.messages.MessageId;
import it.polimi.ingsw.am37.messages.MessageToServer;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.game.GameStatus;
import it.polimi.ingsw.am37.model.game.PlayerPoints;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.server.ClientHandler;

import java.util.ArrayList;
import java.util.List;

public class DrawDeckMessage extends MessageToServer {
    private final String player;
    private final String deck;

    public DrawDeckMessage(MessageId id, String player, String deck) {
        super(id);
        this.player = player;
        this.deck = deck;
    }

    @Override
    public void decodeAndExecute(GameController c, ClientHandler ch) {
        if (c == null) {
            ch.send(new ErrorMessage(MessageId.ERROR, "You are not logged"));
            return;
        }

        boolean drawn = false;
        List<Integer> currentHand = new ArrayList<>();

        for (Player p: c.getGameInstance().getParticipants()) {
            if (p.getNickname().equalsIgnoreCase(player)) {

                //SAVE THE HAND IDS SO THAT I KNOW WHAT CaRD WAS ADDED
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

                        return;             //SEND RESULTS IF THE GAME IS OVER
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
                        ch.send(new ErrorMessage(MessageId.ERROR, "the card was drawn but something went wrong"));

                    for (Player pl: c.getGameInstance().getParticipants()) {
                        if (!pl.getNickname().equals(player)) {
                            if (deck.equalsIgnoreCase("r"))
                                c.getPlayerViews().get(pl).updatesDeckView(deck, c.getGameInstance().getRDeck().firstBack().getMainResource());
                            else if (deck.equalsIgnoreCase("g"))
                                c.getPlayerViews().get(pl).updatesDeckView(deck, c.getGameInstance().getGDeck().firstBack().getMainResource());
                        }
                    }

                    //notify if endgame
                    if (c.isEndGameStarted()) {
                        for (Player pl: c.getGameInstance().getParticipants()) {
                            c.getPlayerViews().get(pl).acknowledgePlayer(pl, "endgame");
                        }
                    }

                    //notify next turn
                    c.getPlayerViews().get(c.getGameInstance().getCurrentTurn()).notifyTurn(c.getGameInstance().getCurrentTurn());
                }

                break;
            }
        }
    }

    @Override
    public String toString() {
        return "Received: " + super.toString() + " | player: " + player + " | deck: " + deck;
    }
}
