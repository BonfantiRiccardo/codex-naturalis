package it.polimi.ingsw.am37.network.server;

import it.polimi.ingsw.am37.controller.virtualview.RMIVirtualView;
import it.polimi.ingsw.am37.network.client.RMIClientSkeleton;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is the server that manages the RMI connections with the clients.
 * It implements the RMIServerStub interface.
 */
public class RMIServer extends UnicastRemoteObject implements RMIServerStub {
    /**
     * This map contains the clients that are connected to the server.
     * The key is the client's id and the value is the client's RMIClientSkeleton.
     */
    private final Map<Integer, RMIClientSkeleton> clients;

    /**
     * This object is used to manage the multiple matches that are played at the same time.
     */
    private final MultipleMatchesHandler multipleMatchesHandler;

    /**
     * This thread is used to start the pinging thread that checks if the clients are still connected to the server.
     */
    private final Thread pingThread;
    /**
     * This map contains the timers that are used to check if the clients are still connected to the server.
     * The key is the client's id and the value is the client's Timer.
     */
    private final Map<Integer, Timer> disconnectionTimer = new HashMap<>();

    /**
     * This constructor initializes the RMIServer.
     * @param multipleMatchesHandler This object is used to manage the multiple matches that are played at the same time.
     * @throws RemoteException This exception is thrown when there is a problem with the connection.
     */
    public RMIServer(MultipleMatchesHandler multipleMatchesHandler) throws RemoteException {
        clients = new ConcurrentHashMap<>();
        this.multipleMatchesHandler = multipleMatchesHandler;

        pingThread = new Thread(this::startPingingClient);
    }

    /**
     * This method is used to start the server.
     * It starts the pinging thread that checks if the clients are still connected to the server.
     * It also starts the RMI registry.
     * @param id This is the id of the client that is connected to the server.
     * @param client This is the RMIClientSkeleton of the client that is connected to the server.
     * @throws RemoteException This exception is thrown when there is a problem with the connection.
     */
    public synchronized void join(int id, RMIClientSkeleton client) throws RemoteException {
        clients.put(id, client);
        disconnectionTimer.put(id, new Timer());
    }

    /**
     * This method is used to stop the server.
     * It stops the pinging thread that checks if the clients are still connected to the server.
     * It also stops the RMI registry.
     * @param id This is the id of the client that is connected to the server.
     * @param client This is the RMIClientSkeleton of the client that is connected to the server.
     * @throws RemoteException This exception is thrown when there is a problem with the connection.
     */
    public synchronized void leave(int id, RMIClientSkeleton client) throws RemoteException {
        clients.remove(id, client);
        disconnectionTimer.put(id, new Timer());
    }

    /**
     * This method is used to create a new game.
     * It creates a new game controller and adds it to the multipleMatchesHandler.
     * It also adds the client to the multipleMatchesHandler.
     * It sets the virtual view of the player and updates the lobby view.
     * It also sends a message to the other players that a new player has joined the game.
     * If the number of players is invalid, it sends an error message to the client.
     * It also sends an error message to the client if there is a problem with the connection.
     * @param client This is the RMIClientSkeleton of the client that is connected to the server.
     * @param clientId This is the id of the client that is connected to the server.
     * @param name This is the name of the player that is creating the game.
     * @param numOfPlayers This is the number of players that will play the game.
     * @throws RemoteException This exception is thrown when there is a problem with the connection.
     */
    @Override
    public void createGame(RMIClientSkeleton client, int clientId, String name, int numOfPlayers) throws RemoteException {
        System.out.println("Received RMI call: id createGame | client: " + clientId + " | name: " + name + " | numPlayers: " + numOfPlayers);
        Player p = new Player(name);
        if ((2 <= numOfPlayers) && (numOfPlayers <= 4)) {
            GameController controller = new GameController(p,numOfPlayers);

            multipleMatchesHandler.addClient(client, controller );//DON'T SAVE THE CLIENT, ONLY SAVE CONTROLLER_HASH (PLAYER ID WITH NAME)
            join(clientId, client);

            multipleMatchesHandler.addLobby(controller.hashCode(), controller);

            controller.setVirtualView(p, new RMIVirtualView(client, this));

            controller.getPlayerViews().get(p).updateLobbyView(p, controller.getAddedPlayers(), controller.hashCode(), controller.getNumOfPlayers());

        }
        else
            client.errorMessage("The player number is invalid, game not created." );
    }

    /**
     * This method is used to get the available lobbies.
     * It sends the available lobbies to the client.
     * If there are no available lobbies, it sends an error message to the client.
     * It also sends an error message to the client if there is a problem with the connection.
     * @param client This is the RMIClientSkeleton of the client that is connected to the server.
     * @throws RemoteException This exception is thrown when there is a problem with the connection.
     */
    public void availableLobbies(RMIClientSkeleton client) throws RemoteException {
        System.out.println("Received RMI call: id availableLobbies");
        if(multipleMatchesHandler.getLobbyList().isEmpty()) {
            client.errorMessage("There are no active games.");
        } else {
            client.receiveLobbies(new ArrayList<>(multipleMatchesHandler.getLobbyList().keySet()));
        }
    }

    /**
     * This method is used to join a game.
     * It adds the player to the game controller and sets the virtual view of the player.
     * It also sends a message to the other players that a new player has joined the game.
     * If the lobby is not active, it sends an error message to the client.
     * It also sends an error message to the client if there is a problem with the connection.
     * @param client This is the RMIClientSkeleton of the client that is connected to the server.
     * @param clientId This is the id of the client that is connected to the server.
     * @param controllerHash This is the hash of the game controller that the player wants to join.
     * @param name This is the name of the player that is joining the game.
     * @throws RemoteException This exception is thrown when there is a problem with the connection.
     */
    @Override
    public void joinGame(RMIClientSkeleton client, int clientId, int controllerHash, String name) throws RemoteException {
        try {
            System.out.println("Received RMI call: id joinGame | client: " + clientId +" | controller: " + controllerHash + " | name: " + name);
            GameController controller = multipleMatchesHandler.getLobbyList().get(controllerHash);
            if (controller != null) {
                Player p = new Player(name);

                synchronized (controller) {
                    controller.addPlayer(p);

                    controller.setVirtualView(p, new RMIVirtualView(client, this));
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

    /**
     * This method is used to place a card.
     * It places the card in the player's kingdom.
     * It also sends a message to the other players that a player has placed a card.
     * If the card is not placed, it sends an error message to the client.
     * It also sends an error message to the client if there is a problem with the connection.
     * @param clientId This is the id of the client that is connected to the server.
     * @param player This is the name of the player that is placing the card.
     * @param cardId This is the id of the card that is being placed.
     * @param side This is the side of the card that is being placed.
     * @param pos This is the position of the card that is being placed.
     * @throws RemoteException This exception is thrown when there is a problem with the connection.
     */
    @Override
    public void placeCard(int clientId, String player, int cardId, String side, Position pos) throws RemoteException {
        boolean placed = false;
        GameController c = multipleMatchesHandler.getMapRMI().get(clients.get(clientId));
        if (c == null && clients.get(clientId) != null) {
            clients.get(clientId).errorMessage("You are not logged");
            return;
        }

        try {
            assert c != null;
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

    /**
     * This method is used to make the player choose a token.
     * It also updates the other players that a player has chosen a token.
     * If the token is not chosen, it sends an error message to the client.
     * It also sends an error message to the client if there is a problem with the connection.
     * @param clientId This is the id of the client that is connected to the server.
     * @param player This is the name of the player that is choosing the token.
     * @param token This is the token that is being chosen.
     * @throws RemoteException This exception is thrown when there is a problem with the connection.
     */
    @Override
    public void chooseToken(int clientId, String player, Token token) throws RemoteException {
        GameController c = multipleMatchesHandler.getMapRMI().get(clients.get(clientId));
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

    /**
     * This method is used to make the player choose an objective.
     * It also updates the other players that a player has chosen an objective.
     * If the objective is not chosen, it sends an error message to the client.
     * It also sends an error message to the client if there is a problem with the connection.
     * @param clientId This is the id of the client that is connected to the server.
     * @param player This is the name of the player that is choosing the objective.
     * @param cardId This is the id of the objective that is being chosen.
     * @throws RemoteException This exception is thrown when there is a problem with the connection.
     */
    @Override
    public void chooseObjective(int clientId, String player, int cardId) throws RemoteException {
        GameController c = multipleMatchesHandler.getMapRMI().get(clients.get(clientId));

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

    /**
     * This method is used to make the player draw a card from the deck.
     * If the player is not logged, it sends an error message to the client.
     * It also updates the other players that a player has drawn a card from the deck.
     * If the card is not drawn, it sends an error message to the client.
     * It also sends an error message to the client if there is a problem with the connection.
     * if in the endgame, it sends the results to the players.
     * if it is the last turn, it sends a message to the players that it is the last turn.
     * notifies the turn to the players.
     * @param clientId This is the id of the client that is connected to the server.
     * @param player This is the name of the player that is drawing the card.
     * @param deck This is the deck from which the card is being drawn.
     * @throws RemoteException This exception is thrown when there is a problem with the connection.
     */
    @Override
    public void drawCardFromDeck(int clientId, String player, String  deck) throws RemoteException {
        GameController c = multipleMatchesHandler.getMapRMI().get(clients.get(clientId));
        if (c == null) {
            clients.get(clientId).errorMessage("You are not logged");
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
                            leave(clientId, clients.get(clientId));
                        }

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
                        clients.get(clientId).errorMessage("The card was drawn but something went wrong");

                    for (Player pl: c.getGameInstance().getParticipants()) {
                        if (!pl.getNickname().equals(player)) {
                            if (deck.equalsIgnoreCase("r"))
                                c.getPlayerViews().get(pl).updatesDeckView(deck, c.getGameInstance().getRDeck().firstBack().getMainResource());
                            else if (deck.equalsIgnoreCase("g"))
                                c.getPlayerViews().get(pl).updatesDeckView(deck, c.getGameInstance().getGDeck().firstBack().getMainResource());
                        }
                    }


                    if (c.isEndGameStarted() && !inEndGame) {
                        for (Player pl: c.getGameInstance().getParticipants()) {
                            c.getPlayerViews().get(pl).acknowledgePlayer(pl, "endgame");
                        }
                    }


                    if (c.getGameInstance().getTurnCounter() == c.getGameInstance().getLastTurn()) {
                        for (Player pl: c.getGameInstance().getParticipants()) {
                            c.getPlayerViews().get(pl).acknowledgePlayer(pl, "last turn");
                        }
                    }


                    c.getPlayerViews().get(c.getGameInstance().getCurrentTurn()).notifyTurn(c.getGameInstance().getCurrentTurn());
                }

                break;
            }
        }
    }

    /**
     * This method is used to make the player draw a card from the available cards.
     * If the player is not logged, it sends an error message to the client.
     * It also updates the other players that a player has drawn a card from the available cards.
     * If the card is not drawn, it sends an error message to the client.
     * It also sends an error message to the client if there is a problem with the connection.
     * if in the endgame, it sends the results to the players.
     * if it is the last turn, it sends a message to the players that it is the last turn.
     * notifies the turn to the players.
     * @param clientId This is the id of the client that is connected to the server.
     * @param player This is the name of the player that is drawing the card.
     * @param cardId This is the id of the card that is being drawn.
     * @throws RemoteException This exception is thrown when there is a problem with the connection.
     */
    @Override
    public void drawCardFromAvailable(int clientId, String player, int cardId) throws RemoteException {
        GameController c = multipleMatchesHandler.getMapRMI().get(clients.get(clientId));
        boolean drawnGold = false;
        boolean drawnResource = false;

        if (c == null) {
            clients.get(clientId).errorMessage("You are not logged");
            return;
        }

        boolean inEndGame = c.isEndGameStarted();
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
                                leave(clientId, clients.get(clientId));
                            }
                            return;
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


                        if (c.isEndGameStarted() && !inEndGame) {
                            for (Player pl: c.getGameInstance().getParticipants()) {
                                c.getPlayerViews().get(pl).acknowledgePlayer(pl, "endgame");
                            }
                        }


                        if (c.getGameInstance().getTurnCounter() == c.getGameInstance().getLastTurn()) { //LAST TURN MIGHT BE UNINITIALIZED
                            for (Player pl: c.getGameInstance().getParticipants()) {
                                c.getPlayerViews().get(pl).acknowledgePlayer(pl, "last turn");
                            }
                        }


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

    /**
     * This method is used to send a message to the player to make sure that the connection is still active.
     * If the player is disconnected, it sends a message to the other players that the player is disconnected.
     * @param clientId This is the id of the client that is connected to the server.
     * @throws RemoteException This exception is thrown when there is a problem with the connection.
     */
    public void ping(int clientId) throws RemoteException {
        if (disconnectionTimer.get(clientId) != null) {
            disconnectionTimer.get(clientId).cancel();
            disconnectionTimer.put(clientId, new Timer());

            disconnectionTimer.get(clientId).schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("\nOne of the player did not send a ping to the server for 15 seconds, it is considered disconnected.");
                    playerDisconnected(clients.get(clientId));
                    pingThread.interrupt();
                }
            }, 15000);
        }
    }

    /**
     * This method is used to disconnect the player from the server.
     * It removes the player from the clients map.
     * It also sends a message to the other players that the player is disconnected.
     * @param cs This is the RMIClientSkeleton of the client that is disconnected from the server.
     */
    public void playerDisconnected(RMIClientSkeleton cs) {
        GameController c = multipleMatchesHandler.getMapRMI().get(cs);
        clients.remove(cs.hashCode(), cs);

        if (c != null) {
            for (Player p: c.getAddedPlayers())
                c.getPlayerViews().get(p).playerDisconnection();

        }

    }

    /**
     * This method is used to start the pinging thread that checks if the clients are still connected to the server.
     * It sends a ping to the clients every 5 seconds.
     */
    public void startPingingClient() {
        while (true) {
            try {
                Thread.sleep(5000);
                for (RMIClientSkeleton cs: clients.values()) {
                    new Thread(() -> {
                        try {
                            cs.ping();
                        } catch (RemoteException e) {
                            playerDisconnected(cs);
                        }
                    });
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * This method is used to get the multipleMatchesHandler.
     * @return The multipleMatchesHandler.
     */
    public MultipleMatchesHandler getMultipleMatchesHandler() {
        return multipleMatchesHandler;
    }
}

