package it.polimi.ingsw.am37.messages.turns;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.exceptions.*;
import it.polimi.ingsw.am37.messages.ErrorMessage;
import it.polimi.ingsw.am37.messages.MessageId;
import it.polimi.ingsw.am37.messages.MessageToServer;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.server.ClientHandler;

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
        boolean drawn = false;

        for (Player p: c.getGameInstance().getParticipants()) {
            if (p.getNickname().equalsIgnoreCase(player)) {
                    if (deck.equalsIgnoreCase("r")) {
                        try {
                            c.playerDrawsCardFromDeck(p, c.getGameInstance().getRDeck());
                            System.out.println("Correctly drawn card for player: " + player);
                            drawn = true;
                        } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                                 AlreadyAssignedException e) {
                            ch.send(new ErrorMessage(MessageId.ERROR, e.getMessage()));
                        }
                    } else if (deck.equalsIgnoreCase("g")) {
                        try {
                            c.playerDrawsCardFromDeck(p, c.getGameInstance().getGDeck());
                            System.out.println("Correctly drawn card for player: " + player);
                            drawn = true;
                        } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                                 AlreadyAssignedException e) {
                            ch.send(new ErrorMessage(MessageId.ERROR, e.getMessage()));
                        }
                    }


                if(!drawn) {
                    ch.send(new ErrorMessage(MessageId.ERROR, "Couldn't place the card because message is corrupted."));
                } else {
                    for (Player pl: c.getGameInstance().getParticipants())      //SHOULD ALSO SEND THE NEW CARD TO THE CLIENT
                        if (deck.equalsIgnoreCase("r"))
                            c.getPlayerViews().get(pl).updatesDeckView(deck, c.getGameInstance().getRDeck().firstBack().getMainResource());
                        else if (deck.equalsIgnoreCase("g"))
                            c.getPlayerViews().get(pl).updatesDeckView(deck, c.getGameInstance().getGDeck().firstBack().getMainResource());
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
