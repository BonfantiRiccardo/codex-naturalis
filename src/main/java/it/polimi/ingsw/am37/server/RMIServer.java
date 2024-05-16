package it.polimi.ingsw.am37.server;

import it.polimi.ingsw.am37.client.RMIClientSkeleton;
import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.controller.MultipleMatchesHandler;
import it.polimi.ingsw.am37.controller.RMIVirtualView;
import it.polimi.ingsw.am37.controller.TCPVirtualView;
import it.polimi.ingsw.am37.exceptions.AlreadyAssignedException;
import it.polimi.ingsw.am37.exceptions.IncorrectUserActionException;
import it.polimi.ingsw.am37.exceptions.NoCardsException;
import it.polimi.ingsw.am37.exceptions.WrongGamePhaseException;
import it.polimi.ingsw.am37.messages.ErrorMessage;
import it.polimi.ingsw.am37.messages.MessageId;
import it.polimi.ingsw.am37.messages.lobby.LobbiesListMessage;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RMIServer extends UnicastRemoteObject implements RMIServerStub {
    private List<RMIClientSkeleton> clients;

    private final MultipleMatchesHandler multipleMatchesHandler;

    public RMIServer(MultipleMatchesHandler multipleMatchesHandler) throws RemoteException {
        clients = new ArrayList<RMIClientSkeleton>();
        this.multipleMatchesHandler = multipleMatchesHandler;
    }

    public synchronized void join(RMIClientSkeleton client) throws RemoteException {
        clients.add(client);
    }

    public synchronized void leave(RMIClientSkeleton client) throws RemoteException {
        clients.remove(client);
    }

    @Override
    public void createGame(RMIClientSkeleton client, String name, int numOfPlayers) throws RemoteException {
        Player p = new Player(name);
        if ((2 <= numOfPlayers) && (numOfPlayers <= 4)) {
            GameController controller = new GameController(p,numOfPlayers);

            multipleMatchesHandler.addClient(client, controller );

            multipleMatchesHandler.addLobby(controller.hashCode(), controller);

            controller.setVirtualView(p, new RMIVirtualView(client));

            controller.getPlayerViews().get(p).updateLobbyView(p, controller.getAddedPlayers(), controller.hashCode(), controller.getNumOfPlayers());

        }
        else
            client.errorMessage("The player number is invalid, game not created." );
    }

    public void availableLobbies(RMIClientSkeleton client) throws RemoteException {
        if(multipleMatchesHandler.getLobbyList().isEmpty()) {
            client.errorMessage("There are no active games.");
        } else {
            client.receiveLobbies(new ArrayList<>(multipleMatchesHandler.getLobbyList().keySet()));
        }
    }

    @Override
    public void joinGame(RMIClientSkeleton client, int controllerHash, String name) throws RemoteException {
        try {

            GameController controller = multipleMatchesHandler.getLobbyList().get(controllerHash);
            if (controller != null) {
                Player p = new Player(name);

                synchronized (controller) {
                    controller.addPlayer(p);

                    controller.setVirtualView(p, new RMIVirtualView(client));
                }

                multipleMatchesHandler.addClient(client, controller);

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
            client.errorMessage("Messaggio di errore");
        }
    }

    @Override
    public void placeCard(RMIClientSkeleton client, String player, int cardId, String side, Position pos) throws RemoteException {
        boolean placed = false;
        GameController c = multipleMatchesHandler.getmapRMI().get(client);

        for (Player p: c.getGameInstance().getParticipants()) {
            if (p.getNickname().equalsIgnoreCase(player)) {
                if (86<=cardId && cardId>=81) {
                    if (side.equalsIgnoreCase("f") && cardId == p.getStartCard().getId()) {
                        try {
                            c.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getFront());
                            System.out.println("Correctly placed start card of player: " + player);
                            placed = true;
                        } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                                 AlreadyAssignedException e) {
                            client.errorMessage("Messaggio errore");
                        }
                    } else if (side.equalsIgnoreCase("b") && cardId == p.getStartCard().getId()){
                        try {
                            c.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getBack());
                            System.out.println("Correctly placed start card of player: " + player);
                            placed = true;
                        } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                                 AlreadyAssignedException e) {
                            client.errorMessage("Messaggio di errore");
                        }
                    }

                } else if (cardId<=81 && cardId>=1) {
                    for (StandardCard card: p.getHand()) {
                        if (card.getId() == cardId) {
                            if (side.equalsIgnoreCase("f")) {
                                try {
                                    c.playerPlacesCard(p, card, card.getFront(), pos);
                                    placed = true;
                                } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                                         AlreadyAssignedException e) {
                                    client.errorMessage("messaggio errore");
                                }
                            } else if (side.equalsIgnoreCase("b")) {
                                try {
                                    c.playerPlacesCard(p, card, card.getBack(), pos);
                                    placed = true;
                                } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                                         AlreadyAssignedException e) {
                                    client.errorMessage("messaggio errore");
                                }
                            }
                            break;
                        }
                    }

                }


                if(!placed) {
                    client.errorMessage("Couldn't place the card because message is corrupted.");
                } else {
                    for (Player pl: c.getGameInstance().getParticipants()) {
                        if (pl.equals(p)) {
                            if (86<=cardId && cardId>=81)
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
    }

    @Override
    public void chooseToken(RMIClientSkeleton client, String player, Token token) throws RemoteException {
        GameController c = multipleMatchesHandler.getmapRMI().get(client);
        for (Player p: c.getGameInstance().getParticipants()) {
            if (p.getNickname().equalsIgnoreCase(player)) {
                try {
                    c.playerChoosesToken(p, token);
                } catch (AlreadyAssignedException | IncorrectUserActionException | WrongGamePhaseException |
                         NoCardsException e) {
                    client.errorMessage("messaggio errore");
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
    public void chooseObjective(RMIClientSkeleton client, String player, int cardId) throws RemoteException {
        GameController c = multipleMatchesHandler.getmapRMI().get(client);
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
                            client.errorMessage("messaggio errore");
                            return;
                        }

                        c.getPlayerViews().get(p).acknowledgePlayer(p, "objective ok");

                        if (c.getGameInstance().getCurrentTurn() != null)
                            c.getPlayerViews().get(c.getGameInstance().getCurrentTurn()).notifyTurn(c.getGameInstance().getCurrentTurn());
                    }
                }

                if(!chosen)
                    client.errorMessage("Couldn't choose the card because message is corrupted.");

                break;
            }
        }
    }


    @Override
    public void drawCardFromDeck(RMIClientSkeleton client, String player, String  deck) throws RemoteException {
        GameController c = multipleMatchesHandler.getmapRMI().get(client);
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
                        client.errorMessage("messaggio errore");
                        return;
                    }
                } else if (deck.equalsIgnoreCase("g")) {
                    try {
                        c.playerDrawsCardFromDeck(p, c.getGameInstance().getGDeck());
                        System.out.println("Correctly drawn card for player: " + player);
                        drawn = true;
                    } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                             AlreadyAssignedException e) {
                        client.errorMessage("messaggio errore");
                        return;
                    }
                }


                if(!drawn) {
                client.errorMessage("Couldn't draw the card because message is corrupted.");
                } else {
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
                        client.errorMessage("the card was drawn but something went wrong");

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
    public void drawCardFromAvailable(RMIClientSkeleton client,String p, int cardId) throws RemoteException {
        GameController c = multipleMatchesHandler.getmapRMI().get(client);

    }

}

