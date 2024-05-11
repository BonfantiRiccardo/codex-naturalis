package it.polimi.ingsw.am37.messages;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.exceptions.*;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.server.ClientHandler;

public class PlaceMessage extends MessageToServer{
    private final String player;
    private final int cardId;
    private final String side;
    private final Position pos;

    public PlaceMessage(MessageId id, String player, int cardId, String side, Position pos) {
        super(id);
        this.player = player;
        this.cardId = cardId;
        this.side = side;
        this.pos = pos;
    }

    @Override
    public void decodeAndExecute(GameController c, ClientHandler ch) {
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
                        }
                    } else if (side.equalsIgnoreCase("b") && cardId == p.getStartCard().getId()){
                        try {
                            c.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getBack());
                            System.out.println("Correctly placed start card of player: " + player);
                            placed = true;
                        } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                                 AlreadyAssignedException e) {
                            ch.send(new ErrorMessage(MessageId.ERROR, e.getMessage()));
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
                                }
                            } else if (side.equalsIgnoreCase("b")) {
                                try {
                                    c.playerPlacesCard(p, card, card.getBack(), pos);
                                    placed = true;
                                } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                                         AlreadyAssignedException e) {
                                    ch.send(new ErrorMessage(MessageId.ERROR, e.getMessage()));
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
