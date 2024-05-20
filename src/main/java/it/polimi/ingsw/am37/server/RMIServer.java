package it.polimi.ingsw.am37.server;

import it.polimi.ingsw.am37.client.RMIClientSkeleton;
import it.polimi.ingsw.am37.controller.*;
import it.polimi.ingsw.am37.exceptions.*;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.game.GameStatus;
import it.polimi.ingsw.am37.model.game.PlayerPoints;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RMIServer extends UnicastRemoteObject implements RMIServerStub {
    private final Map<Integer, RMIClientSkeleton> clients;

    private final MultipleMatchesHandler multipleMatchesHandler;

    public RMIServer(MultipleMatchesHandler multipleMatchesHandler) throws RemoteException {
        clients = new HashMap<>();
        this.multipleMatchesHandler = multipleMatchesHandler;
    }

    public synchronized void join(int id, RMIClientSkeleton client) throws RemoteException {
        clients.put(id, client);
    }

    public synchronized void leave(int id, RMIClientSkeleton client) throws RemoteException {
        clients.remove(id, client);
    }

    @Override
    public void createGame(RMIClientSkeleton client, int clientId, String name, int numOfPlayers) throws RemoteException {
        System.out.println("Received RMI call: id createGame | client: " + clientId + " | name: " + name + " | numPlayers: " + numOfPlayers);
        Player p = new Player(name);
        if ((2 <= numOfPlayers) && (numOfPlayers <= 4)) {
            GameController controller = new GameController(p,numOfPlayers);

            multipleMatchesHandler.addClient(client, controller );//DON'T SAVE THE CLIENT, ONLY SAVE CONTROLLER_HASH (PLAYER ID WITH NAME)
            join(clientId, client);

            multipleMatchesHandler.addLobby(controller.hashCode(), controller);

            controller.setVirtualView(p, new RMIVirtualView(client));

            controller.getPlayerViews().get(p).updateLobbyView(p, controller.getAddedPlayers(), controller.hashCode(), controller.getNumOfPlayers());

        }
        else
            client.errorMessage("The player number is invalid, game not created." );
    }

    public void availableLobbies(RMIClientSkeleton client) throws RemoteException {
        System.out.println("Received RMI call: id availableLobbies");
        if(multipleMatchesHandler.getLobbyList().isEmpty()) {
            client.errorMessage("There are no active games.");
        } else {
            client.receiveLobbies(new ArrayList<>(multipleMatchesHandler.getLobbyList().keySet()));
        }
    }

    @Override
    public void joinGame(RMIClientSkeleton client, int clientId, int controllerHash, String name) throws RemoteException {
        try {
            System.out.println("Received RMI call: id joinGame | client: " + clientId +" | controller: " + controllerHash + " | name: " + name);
            GameController controller = multipleMatchesHandler.getLobbyList().get(controllerHash);
            if (controller != null) {
                Player p = new Player(name);

                synchronized (controller) {
                    controller.addPlayer(p);

                    controller.setVirtualView(p, new RMIVirtualView(client));
                }

                multipleMatchesHandler.addClient(client, controller);
                join(clientId, client);

                System.out.println("correctly added: " + p.getNickname());

                controller.getPlayerViews().get(p).updateLobbyView(p, controller.getAddedPlayers(), controllerHash, controller.getNumOfPlayers());

                for (Player pl: controller.getAddedPlayers())
                    if (!pl.equals(p))
                        controller.getPlayerViews().get(pl).playerAdded(p);

                if (controller.isGameStarted()) {
                    multipleMatchesHandler.removeLobby(controllerHash);

                    for (Player pl: controller.getGameInstance().getParticipants())
                        controller.getPlayerViews().get(pl).sendInitial(controller.getGameInstance().getAvailableGCards(),
                                controller.getGameInstance().getAvailableRCards(), pl.getStartCard(), pl.getHand(),
                                controller.getGameInstance().getPublicObjectives(), pl.getObjectivesToChooseFrom(),
                                controller.getGameInstance().getGDeck().firstBack().getMainResource(),
                                controller.getGameInstance().getRDeck().firstBack().getMainResource());
                }
            } else throw new IncorrectUserActionException("The lobby you chose is not an active lobby.");

        } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                 AlreadyAssignedException | NullPointerException e) {
            client.errorMessage(e.getMessage());
        }
    }

    @Override
    public void placeCard(int clientId, String player, int cardId, String side, Position pos) throws RemoteException {
        boolean placed = false;
        GameController c = multipleMatchesHandler.getmapRMI().get(clients.get(clientId));
        if (c == null) {
            clients.get(clientId).errorMessage("You are not logged");
            return;
        }

        try {
            for (Player p: c.getGameInstance().getParticipants()) {
                if (p.getNickname().equalsIgnoreCase(player)) {
                    if (86>=cardId && cardId>=81) {
                        System.out.println("Received RMI call: id place start card | client: " + clientId + " | name: " + player
                                            + " | cardId: " + cardId + " | side: " + side + " | position: " + pos);

                        if (side.equalsIgnoreCase("f") && cardId == p.getStartCard().getId()) {
                            c.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getFront()); //CATCH
                            System.out.println("Correctly placed start card of player: " + player);
                            placed = true;

                        } else if (side.equalsIgnoreCase("b") && cardId == p.getStartCard().getId()){
                            c.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getBack()); //CATCH
                            System.out.println("Correctly placed start card of player: " + player);
                            placed = true;
                        }

                    } else if (cardId<=81 && cardId>=1) {
                        System.out.println("Received RMI call: id place card | client: " + clientId + " | name: " + player
                                + " | cardId: " + cardId + " | side: " + side + " | position: " + pos);

                        for (StandardCard card: p.getHand()) {
                            if (card.getId() == cardId) {
                                if (side.equalsIgnoreCase("f")) {
                                    c.playerPlacesCard(p, card, card.getFront(), pos); //CATCH
                                    placed = true;
                                } else if (side.equalsIgnoreCase("b")) {
                                    c.playerPlacesCard(p, card, card.getBack(), pos); //CATCH
                                    placed = true;
                                }
                                break;
                            }
                        }

                    }



                    if(!placed) {
                        clients.get(clientId).errorMessage("Couldn't place the card because message is corrupted.");
                    } else {
                        for (Player pl: c.getGameInstance().getParticipants()) {
                            if (pl.equals(p)) {
                                if (86>=cardId && cardId>=81)
                                    c.getPlayerViews().get(p).acknowledgePlayer(p, "start card ok");
                                else if (cardId<=81 && cardId>=1)
                                    c.getPlayerViews().get(p).acknowledgePlayer(p, "place ok");
                            } else
                                c.getPlayerViews().get(pl).updatesPlayersKingdomView(p, cardId, side, pos);
                        }
                    }

                    break;
                }
            }
        } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                              AlreadyAssignedException e) {
            clients.get(clientId).errorMessage(e.getMessage());
        }
    }

    @Override
    public void chooseToken(int clientId, String player, Token token) throws RemoteException {
        GameController c = multipleMatchesHandler.getmapRMI().get(clients.get(clientId));
        if (c == null) {
            clients.get(clientId).errorMessage("You are not logged");
            return;
        }

        for (Player p: c.getGameInstance().getParticipants()) {
            if (p.getNickname().equalsIgnoreCase(player)) {
                try {
                    c.playerChoosesToken(p, token);
                } catch (AlreadyAssignedException | IncorrectUserActionException | WrongGamePhaseException |
                         NoCardsException e) {
                    clients.get(clientId).errorMessage(e.getMessage());
                    return;
                }

                c.getPlayerViews().get(p).acknowledgePlayer(p, "token ok");

                for (Player pl: c.getGameInstance().getParticipants())
                    if (!pl.getNickname().equals(player))
                        c.getPlayerViews().get(pl).nowUnavailableToken(p, token);

                break;
            }
        }
    }

    @Override
    public void chooseObjective(int clientId, String player, int cardId) throws RemoteException {
        GameController c = multipleMatchesHandler.getmapRMI().get(clients.get(clientId));

        if (c == null) {
            clients.get(clientId).errorMessage("You are not logged / Controller not found");
            return;
        }

        boolean chosen = false;
        for (Player p: c.getGameInstance().getParticipants()) {
            if (p.getNickname().equalsIgnoreCase(player)) {
                for (ObjectiveCard oc: p.getObjectivesToChooseFrom()) {
                    if (oc.getId() == cardId) {
                        try {
                            c.playerChoosesObjective(p, oc);
                            chosen = true;
                        } catch (AlreadyAssignedException | IncorrectUserActionException | WrongGamePhaseException |
                                 NoCardsException e) {
                            clients.get(clientId).errorMessage(e.getMessage());
                            return;
                        }

                        c.getPlayerViews().get(p).acknowledgePlayer(p, "objective ok");

                        if (c.getGameInstance().getCurrentTurn() != null) {
                            for (Player pl: c.getGameInstance().getParticipants())
                                c.getPlayerViews().get(pl).sendPlayersInOrder(c.getGameInstance().getParticipants());

                            c.getPlayerViews().get(c.getGameInstance().getCurrentTurn()).notifyTurn(c.getGameInstance().getCurrentTurn());
                        }
                    }
                }

                if(!chosen)
                    clients.get(clientId).errorMessage("Couldn't choose the card because message is corrupted.");

                break;
            }
        }
    }

    @Override
    public void drawCardFromDeck(int clientId, String player, String  deck) throws RemoteException {
        GameController c = multipleMatchesHandler.getmapRMI().get(clients.get(clientId));
        if (c == null) {
            clients.get(clientId).errorMessage("You are not logged");
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
                        clients.get(clientId).errorMessage(e.getMessage());
                        return;
                    }
                } else if (deck.equalsIgnoreCase("g")) {
                    try {
                        c.playerDrawsCardFromDeck(p, c.getGameInstance().getGDeck());
                        System.out.println("Correctly drawn card for player: " + player);
                        drawn = true;
                    } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                             AlreadyAssignedException e) {
                        clients.get(clientId).errorMessage(e.getMessage());
                        return;
                    }
                }


                if(!drawn) {
                    clients.get(clientId).errorMessage("Couldn't draw the card because message is corrupted.");
                } else {
                    if (c.getGameInstance().getCurrentStatus().equals(GameStatus.OVER)) {
                        PlayerPoints[] results = c.getGameInstance().getGameWinner();
                        for (Player pl: c.getGameInstance().getParticipants()) {
                            c.getPlayerViews().get(pl).sendResults(results);

                            multipleMatchesHandler.removeClient(clients.get(clientId));
                            clients.remove(clientId);
                        }

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
                        clients.get(clientId).errorMessage("The card was drawn but something went wrong");

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
    public void drawCardFromAvailable(int clientId, String player, int cardId) throws RemoteException {
        GameController c = multipleMatchesHandler.getmapRMI().get(clients.get(clientId));
        boolean drawnGold = false;
        boolean drawnResource = false;

        if (c == null) {
            clients.get(clientId).errorMessage("You are not logged");
            return;
        }

        boolean goldEmpty = c.getGameInstance().getRDeck().isEmpty();
        boolean resourceEmpty = c.getGameInstance().getGDeck().isEmpty();

        try {
            for (Player p: c.getGameInstance().getParticipants()) {
                if (p.getNickname().equalsIgnoreCase(player)) {
                    for (StandardCard sc: c.getGameInstance().getAvailableGCards()) {
                        if (cardId == sc.getId()) {
                            c.playerDrawsCardFromAvailable(p, sc);
                            System.out.println("Correctly drawn gold card for player: " + player);
                            drawnGold = true;
                            break;
                        }
                    }

                    for (StandardCard sc: c.getGameInstance().getAvailableRCards()) {
                        if (cardId == sc.getId()) {
                            c.playerDrawsCardFromAvailable(p, sc);
                            System.out.println("Correctly drawn resource card for player: " + player);
                            drawnResource = true;
                            break;
                        }
                    }

                    if(!drawnResource && !drawnGold) {
                        clients.get(clientId).errorMessage("Couldn't draw the card because message is corrupted.");
                    } else {

                        if (c.getGameInstance().getCurrentStatus().equals(GameStatus.OVER)) {
                            PlayerPoints[] results = c.getGameInstance().getGameWinner();
                            for (Player pl: c.getGameInstance().getParticipants()) {
                                c.getPlayerViews().get(pl).sendResults(results);

                                multipleMatchesHandler.removeClient(clients.get(clientId));
                                clients.remove(clientId);
                            }
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
                        if (c.isEndGameStarted()) {
                            for (Player pl: c.getGameInstance().getParticipants()) {
                                c.getPlayerViews().get(pl).acknowledgePlayer(pl, "endgame");
                            }
                        }

                        //notify turn
                        c.getPlayerViews().get(c.getGameInstance().getCurrentTurn()).notifyTurn(c.getGameInstance().getCurrentTurn());
                    }

                    break;
                }
            }
        } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                 AlreadyAssignedException e) {
            clients.get(clientId).errorMessage(e.getMessage());
        }
    }

}

