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

public class DrawAvailableMessage extends MessageToServer {
    private final String player;
    private final int cardId;

    public DrawAvailableMessage(MessageId id, String player, int cardId) {
        super(id);
        this.player = player;
        this.cardId = cardId;
    }

    @Override
    public void decodeAndExecute(GameController c, ClientHandler ch) {
        boolean drawnGold = false;
        boolean drawnResource = false;

        if (c == null) {
            ch.send(new ErrorMessage(MessageId.ERROR, "You are not logged"));
            return;
        }

        boolean inEndGame = c.isEndGameStarted();
        boolean goldEmpty = c.getGameInstance().getRDeck().isEmpty();
        boolean resourceEmpty = c.getGameInstance().getGDeck().isEmpty();

        for (Player p: c.getGameInstance().getParticipants()) {
            if (p.getNickname().equalsIgnoreCase(player)) {
                for (StandardCard sc: c.getGameInstance().getAvailableGCards()) {
                    if (cardId == sc.getId()) {
                        try {
                            c.playerDrawsCardFromAvailable(p, sc);
                            System.out.println("Correctly drawn gold card for player: " + player);
                            drawnGold = true;
                            break;
                        } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                                 AlreadyAssignedException e) {
                            ch.send(new ErrorMessage(MessageId.ERROR, e.getMessage()));
                            return;
                        }
                    }
                }

                for (StandardCard sc: c.getGameInstance().getAvailableRCards()) {
                    if (cardId == sc.getId()) {
                        try {
                            c.playerDrawsCardFromAvailable(p, sc);
                            System.out.println("Correctly drawn resource card for player: " + player);
                            drawnResource = true;
                            break;
                        } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                                 AlreadyAssignedException e) {
                            ch.send(new ErrorMessage(MessageId.ERROR, e.getMessage()));
                            return;
                        }
                    }
                }

                if(!drawnResource && !drawnGold) {
                    ch.send(new ErrorMessage(MessageId.ERROR, "Couldn't draw the card because message is corrupted."));
                } else {

                    if (c.getGameInstance().getCurrentStatus().equals(GameStatus.OVER)) {
                        PlayerPoints[] results = c.getGameInstance().getGameWinner();
                        for (Player pl: c.getGameInstance().getParticipants())
                            c.getPlayerViews().get(pl).sendResults(results);

                        return;             //SEND RESULTS IF THE GAME IS OVER
                    }

                    for (Player pl: c.getGameInstance().getParticipants()) {
                        if (drawnResource) {
                            if (!resourceEmpty)
                                c.getPlayerViews().get(pl).updatesAvailableCardView("r", c.getGameInstance().getRDeck().firstBack().getMainResource(), "resource", c.getGameInstance().getAvailableRCards());
                            else if (!goldEmpty)
                                c.getPlayerViews().get(pl).updatesAvailableCardView("g", c.getGameInstance().getGDeck().firstBack().getMainResource(), "resource", c.getGameInstance().getAvailableRCards());
                            else
                                c.getPlayerViews().get(pl).updatesAvailableCardView("none", c.getGameInstance().getGDeck().firstBack().getMainResource(), "resource", c.getGameInstance().getAvailableRCards());

                        } else if (drawnGold) {
                            if (!goldEmpty)
                                c.getPlayerViews().get(pl).updatesAvailableCardView("g", c.getGameInstance().getGDeck().firstBack().getMainResource(), "gold", c.getGameInstance().getAvailableGCards());
                            else if (!resourceEmpty)
                                c.getPlayerViews().get(pl).updatesAvailableCardView("r", c.getGameInstance().getRDeck().firstBack().getMainResource(), "gold", c.getGameInstance().getAvailableGCards());
                            else
                                c.getPlayerViews().get(pl).updatesAvailableCardView("none", c.getGameInstance().getGDeck().firstBack().getMainResource(), "gold", c.getGameInstance().getAvailableGCards());

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

                    //notify turn
                    c.getPlayerViews().get(c.getGameInstance().getCurrentTurn()).notifyTurn(c.getGameInstance().getCurrentTurn());
                }

                break;
            }
        }
    }

    @Override
    public String toString() {
        return "Received: " + super.toString() +  " | player: " + player + " | cardId: " + cardId;
    }
}
