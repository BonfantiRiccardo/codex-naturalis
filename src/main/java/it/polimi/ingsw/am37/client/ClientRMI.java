package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.GoldCard;
import it.polimi.ingsw.am37.model.cards.placeable.ResourceCard;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Kingdom;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.server.RMIServerStub;
import it.polimi.ingsw.am37.view.ClientSidePlayer;
import it.polimi.ingsw.am37.view.RMIVirtualServer;
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

public class ClientRMI extends UnicastRemoteObject implements RMIClientSkeleton, ClientConnectionInterface {
    private final String ip;
    private final int port;
    private final View v;

    public ClientRMI(String ip, int port, View v) throws RemoteException {
        this.ip = ip;
        this.port = port;
        this.v = v;
    }

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

    @Override
    public void receiveLobbies(ArrayList<Integer> Lobbies) throws RemoteException {
        v.getLocalGameInstance().setListOfLobbies(Lobbies);
        v.setState(ViewState.CHOOSE_LOBBY);
    }

    @Override
    public void playerAdded(String player) throws RemoteException {
        v.getLocalGameInstance().addPlayer(new ClientSidePlayer(player));
        synchronized (v){
            v.notify();
        }
    }

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

    @Override
    public void sendNowUnavailableToken(String player, Token token) throws RemoteException {
        for (ClientSidePlayer p : v.getLocalGameInstance().getPlayers())
            if (p.getNickname().equals(player))
                p.setToken(token);

        v.getLocalGameInstance().removeToken(token);
    }

    @Override
    public void sendPlayersInOrder(List<String> playersInOrder) {
        v.getLocalGameInstance().setPlayersInOrder(playersInOrder);

        if (v.getLocalGameInstance().getMe().getNickname().equals(playersInOrder.getFirst()))
            v.getLocalGameInstance().getMe().setHasBlackToken(true);

        for (ClientSidePlayer p: v.getLocalGameInstance().getPlayers())
            if (p.getNickname().equals(playersInOrder.getFirst()))
                p.setHasBlackToken(true);

    }


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
        }
    }


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


    @Override
    public void updateDeckView(String deck, Resource resource) throws RemoteException {
        if (deck.equalsIgnoreCase("r"))
            v.getLocalGameInstance().setTopOfResourceDeck(resource);
        else if (deck.equalsIgnoreCase("g"))
            v.getLocalGameInstance().setTopOfGoldDeck(resource);

        v.getLocalGameInstance().nextTurn();
    }


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


        v.setState(ViewState.SHOW_RESULTS);
    }

    @Override
    public void errorMessage(String description) throws RemoteException {
        v.printError(description);
        v.setState(ViewState.ERROR);
    }

    @Override
    public void playerDisconnection() throws RemoteException {
        v.setState(ViewState.DISCONNECTION);
    }

}
