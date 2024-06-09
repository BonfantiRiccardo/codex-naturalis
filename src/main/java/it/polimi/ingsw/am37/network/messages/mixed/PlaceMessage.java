package it.polimi.ingsw.am37.network.messages.mixed;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.exceptions.*;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToServer;
import it.polimi.ingsw.am37.network.server.ClientHandler;
/**
 * This message is sent by the client to the server when the player wants to place a card.
 * The server will place the card in the player's kingdom and send the updated kingdom view to all the players in the game.
 */
public class PlaceMessage extends MessageToServer {
    /**
     * The nickname of the player.
     */
    private final String player;
    /**
     * The id of the card.
     */
    private final int cardId;
    /**
     * The side of the card.
     */
    private final String side;
    /**
     * The position of the card.
     */
    private final Position pos;

    /**
     * Constructor.
     * @param id The message id.
     * @param player The nickname of the player.
     * @param cardId The id of the card.
     * @param side The side of the card.
     * @param pos The position of the card.
     */
    public PlaceMessage(MessageId id, String player, int cardId, String side, Position pos) {
        super(id);
        this.player = player;
        this.cardId = cardId;
        this.side = side;
        this.pos = pos;
    }

    /**
     * This method will place the card in the player's kingdom and send the updated kingdom view to all the players in the game.
     * If the player is not logged, the server will send an error message to the client.
     * @param c The game controller.
     * @param ch The client handler.
     */
    @Override
    public void decodeAndExecute(GameController c, ClientHandler ch) {
        if (c == null) {
            ch.send(new ErrorMessage(MessageId.ERROR, "You are not logged"));
            return;
        }

        boolean placed = false;

        for (Player p: c.getGameInstance().getParticipants()) {
            if (p.getNickname().equalsIgnoreCase(player)) {
                if (id.equals(MessageId.PLACE_SC)) {
                    if (side.equalsIgnoreCase("f") && cardId == p.getStartCard().getId()) {
                        try {
                            c.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getFront());
                            System.out.println("Correctly placed start card of player: " + player);
                            placed = true;
                        } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                                 AlreadyAssignedException e) {
                            ch.send(new ErrorMessage(MessageId.ERROR, e.getMessage()));
                            return;
                        }
                    } else if (side.equalsIgnoreCase("b") && cardId == p.getStartCard().getId()){
                        try {
                            c.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getBack());
                            System.out.println("Correctly placed start card of player: " + player);
                            placed = true;
                        } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                                 AlreadyAssignedException e) {
                            ch.send(new ErrorMessage(MessageId.ERROR, e.getMessage()));
                            return;
                        }
                    }

                } else if (id.equals(MessageId.PLACE)) {
                    for (StandardCard card: p.getHand()) {
                        if (card.getId() == cardId) {
                            if (side.equalsIgnoreCase("f")) {
                                try {
                                    c.playerPlacesCard(p, card, card.getFront(), pos);
                                    placed = true;
                                } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                                         AlreadyAssignedException e) {
                                    ch.send(new ErrorMessage(MessageId.ERROR, e.getMessage()));
                                    return;
                                }
                            } else if (side.equalsIgnoreCase("b")) {
                                try {
                                    c.playerPlacesCard(p, card, card.getBack(), pos);
                                    placed = true;
                                } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                                         AlreadyAssignedException e) {
                                    ch.send(new ErrorMessage(MessageId.ERROR, e.getMessage()));
                                    return;
                                }
                            }
                            break;
                        }
                    }
                }


                if(!placed) {
                    ch.send(new ErrorMessage(MessageId.ERROR, "Couldn't place the card because message is corrupted."));
                } else {

                    for (Player pl: c.getGameInstance().getParticipants()) {
                        if (pl.equals(p)) {
                            if (id.equals(MessageId.PLACE_SC))
                                c.getPlayerViews().get(p).acknowledgePlayer(p, "start card ok");
                            else if (id.equals(MessageId.PLACE))
                                c.getPlayerViews().get(p).acknowledgePlayer(p, "place ok");
                        } else
                            c.getPlayerViews().get(pl).updatesPlayersKingdomView(p, cardId, side, pos);
                    }
                }

                break;
            }
        }

    }

    @Override
    public String toString() {
        return "Received: " + super.toString() + " | player: " + player + " | placed id and side: " + cardId + side + " | in pos: " + pos;
    }
}
