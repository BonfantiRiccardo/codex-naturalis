package it.polimi.ingsw.am37.network.client;

import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.GoldCard;
import it.polimi.ingsw.am37.model.cards.placeable.ResourceCard;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Kingdom;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.network.server.RMIServerStub;
import it.polimi.ingsw.am37.view.clientmodel.ClientSidePlayer;
import it.polimi.ingsw.am37.view.virtualserver.RMIVirtualServer;
import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;

import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * the ClientRMI class implements the RMIClientSkeleton and implements the method called by the player during the game.
 */
public class ClientRMI extends UnicastRemoteObject implements RMIClientSkeleton, ClientConnectionInterface {
    /**
     * the ip attribute is the ip of the server connecting to.
     */
    private final String ip;
    /**
     * the port attribute is the port of the server connecting to.
     */
    private final int port;
    /**
     * the v attribute is the view connecting to.
     */
    private final View v;

    /**
     * ClientRMI is a setter method for the server's port and ip, and the for the view.
     * @param ip is the server's ip.
     * @param port is the server's port.
     * @param v is the view.
     * @throws RemoteException when the connection is lost.
     */
    public ClientRMI(String ip, int port, View v) throws RemoteException {
        this.ip = ip;
        this.port = port;
        this.v = v;
    }

    /**
     * the startClient method is called by the ClientMain to connect the Client and the view.
     * @throws RemoteException when the connection is lost.
     */
    @Override
    public void startClient() throws RemoteException {
        Registry reg = LocateRegistry.getRegistry(ip, port);

        try {
            RMIServerStub server = (RMIServerStub) reg.lookup("RMIServer");
            System.out.println("Connection established on port: " + port);

            RMIVirtualServer vs = new RMIVirtualServer(server, this);
            v.setVirtualServer(vs);

            while (true) {
                boolean choice = v.handleGame();

                if (!choice)
                    break;
            }

        } catch (Exception e) {
            System.out.println("\nConnection to server was lost, closing program\n");
        }
    }
    /**
     * the updateLobbyView method sends to the player all the infos about the lobby, for example the nicknames of the players
     * in the lobby, the number of players in the lobby or the maximum capacity of the lobby.
     * @param yourNickname is the player receiving the message.
     * @param playerNickname is the list of players in the lobby.
     * @param lobbyNum is the number of players in the lobby.
     * @param totalPlayers is the maximum number of players the lobby can contain.
     */
    @Override
    public void updateLobbyView(String yourNickname, List<String> playerNickname, int lobbyNum, int totalPlayers) throws RemoteException {
        v.getLocalGameInstance().setMe(new ClientSidePlayer(yourNickname));

        for (String nick : playerNickname)
            if (!yourNickname.equals(nick))
                v.getLocalGameInstance().addPlayer(new ClientSidePlayer(nick));

        v.getLocalGameInstance().setNumOfLobby(lobbyNum);

        v.getLocalGameInstance().setNumOfPlayers(totalPlayers);

        v.setState(ViewState.WAIT_IN_LOBBY);
    }

    /**
     * the receiveLobbies method sets the list of available lobbies and change the state of the view to "choose_lobby".
     * @param Lobbies is the list of Lobbies.
     * @throws RemoteException when the connection is lost.
     */
    @Override
    public void receiveLobbies(ArrayList<Integer> Lobbies) throws RemoteException {
        v.getLocalGameInstance().setListOfLobbies(Lobbies);
        v.setState(ViewState.CHOOSE_LOBBY);
    }
    /**
     * the playerAdded method adds a player to the lobby and notifies the other player about it.
     * @param player is the player added.
     */
    @Override
    public void playerAdded(String player) throws RemoteException {
        v.getLocalGameInstance().addPlayer(new ClientSidePlayer(player));
        synchronized (v){
            v.notify();
        }
    }
    /**
     * the sendInitial method sends to the player the starting setup of the game, including the available gold and resource
     * cards on the field, the top of the decks, his hand, his starting card, the public and private objectives.
     * @param availableGold is the list of available gold cards the player can draw.
     * @param availableResource is the list of available resource cards the player can draw.
     * @param startCard is the player's starting card.
     * @param hand is the player's hand.
     * @param publicObjectives is the public objectives the player has to complete.
     * @param privateObjectives is the private objectives the player has to choose from.
     * @param goldDeckBack is the top of the gold deck from which the player can draw.
     * @param resourceDeckBack is the top of the gold deck from which the player can draw.
     */
    @Override
    public void updateInitialPhase(List<Integer> availableGold, List<Integer> availableResource, int startCard, List<Integer> hand,
                                   List<Integer> publicObjectives, List<Integer> privateObjectives, Resource goldDeckBack, Resource resourceDeckBack) throws RemoteException {
        List<StandardCard> availableRes = new ArrayList<>();

        for (ResourceCard rc : v.getLocalGameInstance().getResourceCards()) {
            if (rc.getId() == availableResource.get(0))
                availableRes.add(rc);
            else if (rc.getId() == availableResource.get(1))
                availableRes.add(rc);

            if (availableRes.size() == 2)
                break;
        }

        v.getLocalGameInstance().setAvailableResourceCards(availableRes);

        List<StandardCard> availableG = new ArrayList<>();
        for (GoldCard gc : v.getLocalGameInstance().getGoldCards()) {
            if (gc.getId() == availableGold.get(0))
                availableG.add(gc);
            else if (gc.getId() == availableGold.get(1))
                availableG.add(gc);

            if (availableG.size() == 2)
                break;
        }

        v.getLocalGameInstance().setAvailableGoldCards(availableG);

        List<StandardCard> myHand = new ArrayList<>();
        for (int i : hand) {
            if (i > 0 && i <= 40) {
                for (ResourceCard rc : v.getLocalGameInstance().getResourceCards())
                    if (rc.getId() == i)
                        myHand.add(rc);

            } else if (i > 40 && i <= 80)
                for (GoldCard gc : v.getLocalGameInstance().getGoldCards())
                    if (gc.getId() == i)
                        myHand.add(gc);

            if (myHand.size() == 3)
                break;
        }

        v.getLocalGameInstance().setMyHand(myHand);


        for (StartCard sc : v.getLocalGameInstance().getStartCards())
            if (sc.getId() == startCard) {
                v.getLocalGameInstance().setMyStartCard(sc);
                break;
            }


        List<ObjectiveCard> publicObj = new ArrayList<>();
        List<ObjectiveCard> privateObj = new ArrayList<>();

        for (ObjectiveCard oc : v.getLocalGameInstance().getObjectiveCards()) {
            if (oc.getId() == publicObjectives.get(0))
                publicObj.add(oc);
            else if (oc.getId() == publicObjectives.get(1))
                publicObj.add(oc);

            if (oc.getId() == privateObjectives.get(0))
                privateObj.add(oc);
            else if (oc.getId() == privateObjectives.get(1))
                privateObj.add(oc);

            if (publicObj.size() == 2 && privateObj.size() == 2)
                break;
        }
        v.getLocalGameInstance().setPrivateObjectives(privateObj);
        v.getLocalGameInstance().setPublicObjectives(publicObj);


        v.getLocalGameInstance().setTopOfGoldDeck(goldDeckBack);
        v.getLocalGameInstance().setTopOfResourceDeck(resourceDeckBack);


        v.setState(ViewState.PLACE_SC);
        synchronized (v){
            v.notify();
        }
    }
    /**
     * the nowUnavailableToken method set to the player the token he chose, removing it from the list of the available
     * ones to choose from.
     * @param player is the player choosing the token.
     * @param token is the token chosen.
     */
    @Override
    public void sendNowUnavailableToken(String player, Token token) throws RemoteException {
        for (ClientSidePlayer p : v.getLocalGameInstance().getPlayers())
            if (p.getNickname().equals(player))
                p.setToken(token);

        v.getLocalGameInstance().removeToken(token);
    }
    /**
     * the sendPlayersInOrder method sends a randomly generated list of the players, ordered according to their
     * turns in the game.
     * @param playersInOrder is the ordered list of the players.
     */
    @Override
    public void sendPlayersInOrder(List<String> playersInOrder) {
        v.getLocalGameInstance().setPlayersInOrder(playersInOrder);

        if (v.getLocalGameInstance().getMe().getNickname().equals(playersInOrder.getFirst()))
            v.getLocalGameInstance().getMe().setHasBlackToken(true);

        for (ClientSidePlayer p: v.getLocalGameInstance().getPlayers())
            if (p.getNickname().equals(playersInOrder.getFirst()))
                p.setHasBlackToken(true);

    }

    /**
     * the notifyPlayer method sends to the player confirmations about the previews action of the player, for example
     * if the player has drawn a card, if he has placed a card, if he has chosen the objectives, if he has chosen
     * his token or if it's his turn.
     * @param message is the message notified to the player.
     */
    @Override
    public void notifyPlayer(String message) throws RemoteException {
        switch (message) {
            case "start card ok" -> v.setState(ViewState.CHOOSE_TOKEN);

            case "token ok" -> v.setState(ViewState.CHOOSE_OBJECTIVE);

            case "objective ok" -> {
                if (v.getState().equals(ViewState.CHOOSE_OBJECTIVE))
                    v.setState(ViewState.NOT_TURN);
            }

            case "your turn" -> v.setState(ViewState.PLACE);

            case "place ok" -> v.setState(ViewState.DRAW);

            case "draw ok" -> v.setState(ViewState.NOT_TURN); //TO DELETE SINCE I CAN'T JUST NOTIFY THE PLAYER, I HAVE TO SEND THE NEW CARD
                                                    //MAYBE USEFUL IN DRAW FROM AVAILABLE
            case  "endgame" -> {
                //add to updates
                PropertyChangeEvent evt = new PropertyChangeEvent(
                        this,
                        "ENDGAME",
                        null,
                        null);
                v.propertyChange(evt);
            }
            case  "last turn" -> {
                //add to updates
                PropertyChangeEvent evt = new PropertyChangeEvent(
                        this,
                        "LAST_TURN",
                        null,
                        null);
                v.propertyChange(evt);
            }
        }
    }

    /**
     * the updatesPlayersKingdomView method places a card and update the View of the player right after placing it,
     * either if it's a starting card or another card.
     * @param player is the player who places the card.
     * @param cardId is the cardId of the card to be placed.
     * @param side is the side of the card to be placed.
     * @param pos is the position where the player wants to place the card.
     */
    @Override
    public void updateKingdom(String player, int cardId, String side, Position pos) throws RemoteException {
        if (cardId >= 81 && cardId <= 86) {
            StartCard placed = null;
            for (StartCard sc : v.getLocalGameInstance().getStartCards())
                if (cardId == sc.getId()) {
                    placed = sc;
                    break;
                }

            assert placed != null;

            for (ClientSidePlayer p : v.getLocalGameInstance().getPlayers()) {
                if (p.getNickname().equals(player)) {
                    if (side.equalsIgnoreCase("f"))
                        p.setKingdom(new Kingdom(placed, placed.getFront()));
                    else if (side.equalsIgnoreCase("b"))
                        p.setKingdom(new Kingdom(placed, placed.getBack()));

                    break;
                }
            }

            if (v.getLocalGameInstance().getMe().getNickname().equals(player)) {
                if (side.equalsIgnoreCase("f"))
                    v.getLocalGameInstance().getMe().setKingdom(new Kingdom(placed, placed.getFront()));
                else if (side.equalsIgnoreCase("b"))
                    v.getLocalGameInstance().getMe().setKingdom(new Kingdom(placed, placed.getBack()));

            }

        } else if (cardId >= 1 && cardId <= 80)
            v.getLocalGameInstance().placeCard(player, cardId, side, pos);
    }

    /**
     * the updatePlayerHandAndDeckView method updates the player's hand and the new top of the deck right after the player
     * has drawn a card from the deck.
     * @param deck is a parameter which gives information about the nature of the deck, if it's gold or resource.
     * @param resource is the new top of the deck requested.
     * @param cardId is the card the player has drawn and that now is in his hand.
     */
    @Override
    public void updateHandAndDeckView(String deck, Resource resource, int cardId) throws RemoteException {
        if (cardId >= 1 && cardId <= 40) {
            for (ResourceCard rc: v.getLocalGameInstance().getResourceCards())
                if (rc.getId() == cardId)
                    v.getLocalGameInstance().getMyHand().add(rc);

        } else if (cardId >= 41 && cardId <= 80) {
            for (GoldCard gc: v.getLocalGameInstance().getGoldCards())
                if (gc.getId() == cardId)
                    v.getLocalGameInstance().getMyHand().add(gc);
        }

        if (deck.equalsIgnoreCase("r"))
            v.getLocalGameInstance().setTopOfResourceDeck(resource);
        else if (deck.equalsIgnoreCase("g"))
            v.getLocalGameInstance().setTopOfGoldDeck(resource);

        v.getLocalGameInstance().nextTurn();

        v.setState(ViewState.NOT_TURN);
    }

    /**
     * the updatesDeckView sends to the player the top of the resource or gold deck, based on which one is requested.
     * @param deck is a parameter which gives information about the nature of the deck, if it's gold or resource.
     * @param resource is the top of the deck requested.
     */
    @Override
    public void updateDeckView(String deck, Resource resource) throws RemoteException {
        if (deck.equalsIgnoreCase("r"))
            v.getLocalGameInstance().setTopOfResourceDeck(resource);
        else if (deck.equalsIgnoreCase("g"))
            v.getLocalGameInstance().setTopOfGoldDeck(resource);

        v.getLocalGameInstance().nextTurn();
    }

    /**
     * the updatesAvailableCardView method sends to the player the new available cards on the field.
     * @param deck is a parameter which gives information about the nature of the deck, if it's gold or resource.
     * @param topOfDeck is the resource of the new top of the deck requested.
     * @param availableChanged is the new available card that has changed.
     * @param available is the new list of the available cards on the field.
     */
    @Override
    public void updateAvailable(String deck, Resource topOfDeck, String availableChanged, List<Integer> available) throws RemoteException {
        List<StandardCard> newAvailable = new ArrayList<>();

        if (availableChanged.equalsIgnoreCase("resource")) {
            for (ResourceCard rc : v.getLocalGameInstance().getResourceCards())
                if (available.contains(rc.getId())) {
                    newAvailable.add(rc);

                    if (newAvailable.size() == 2)
                        break;
                }

            v.getLocalGameInstance().setAvailableResourceCards(newAvailable);
        } else if (availableChanged.equalsIgnoreCase("gold")) {
            for (GoldCard gc : v.getLocalGameInstance().getGoldCards())
                if (available.contains(gc.getId())) {
                    newAvailable.add(gc);

                    if (newAvailable.size() == 2)
                        break;
                }

            v.getLocalGameInstance().setAvailableGoldCards(newAvailable);
        }

        if (deck.equalsIgnoreCase("r"))
            v.getLocalGameInstance().setTopOfResourceDeck(topOfDeck);
        else if (deck.equalsIgnoreCase("g"))
            v.getLocalGameInstance().setTopOfGoldDeck(topOfDeck);

        v.getLocalGameInstance().nextTurn();

        if (v.getState().equals(ViewState.DRAW)) {
            v.setState(ViewState.NOT_TURN);
        }
    }

    /**
     * the sendResults method calculates and shows the final points of each player.
     * @param playerPoints is a map of each player's points.
     * @param playerNumCompletedObjectives is a map of the number of objectives completed by each player.
     */
    @Override
    public void showResults(Map<String, Integer> playerPoints, Map<String, Integer> playerNumCompletedObjectives) throws RemoteException {
        for(ClientSidePlayer p : v.getLocalGameInstance().getPlayers()) {
            p.setObjectivesCompleted(playerNumCompletedObjectives.get(p.getNickname()));
            p.setFinalPoints(playerPoints.get(p.getNickname()));
        }

        for(String name: playerPoints.keySet())
            if (v.getLocalGameInstance().getMe().getNickname().equals(name)) {
                v.getLocalGameInstance().getMe().setFinalPoints(playerPoints.get(name));
                v.getLocalGameInstance().getMe().setObjectivesCompleted(playerNumCompletedObjectives.get(name));
            }

        PropertyChangeEvent evt = new PropertyChangeEvent(
                this,
                "RESULTS",
                null,
                null);
        v.propertyChange(evt);

        v.setState(ViewState.SHOW_RESULTS);
    }

    /**
     * the error message method prints the error message given in the description.
     * @param description is the message sent.
     * @throws RemoteException when the connection is lost.
     */
    @Override
    public void errorMessage(String description) throws RemoteException {
        v.printError(description);
        v.setState(ViewState.ERROR);
    }

    /**
     * the playerDisconnection method sets the status of a player to "disconnected"
     * @throws RemoteException when the connection is lost.
     */
    @Override
    public void playerDisconnection() throws RemoteException {
        v.setState(ViewState.DISCONNECTION);
    }

}
