package it.polimi.ingsw.am37.controller;

import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.decks.Deck;
import it.polimi.ingsw.am37.model.exceptions.*;
import it.polimi.ingsw.am37.model.game.*;
import it.polimi.ingsw.am37.model.player.*;
import it.polimi.ingsw.am37.model.sides.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameController {   //THE CLASS IS ONLY A PROTOTYPE
    private final List<Player> addedPlayers;
    private final int numOfPlayers;
    private GameModel gameInstance;

    public GameController(Player creator, int numOfPlayers) {
        addedPlayers = new ArrayList<>();
        addedPlayers.add(creator);

        this.numOfPlayers = numOfPlayers;
        //lobbyPhase();
    }

    public List<Player> getAddedPlayers() {
        return addedPlayers;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public GameModel getGameInstance() {
        return gameInstance;
    }

    private void lobbyPhase() { //STUB METHOD
        Scanner in = new Scanner(System.in);
        while (addedPlayers.size() < numOfPlayers) {
            System.out.println("Add players to fill the lobby.");
            System.out.println("Now joined: " + addedPlayers.size() + "/" + numOfPlayers);
            System.out.println("New player Nickname:");
            String name = in.nextLine();
            boolean check = false;
            for (Player p: addedPlayers)
                if (p.getNickname().equals(name)) {
                    check = true;
                    break;
                }

            if (!check) {
                addedPlayers.add(new Player(name));
            } else
                System.out.println("Nickname already in use.");

            //INDEFINITELY WAITS FOR PLAYERS TO JOIN
        }

        try {
            setGameInstance();
        } catch (AlreadyAssignedException e) {
            throw new RuntimeException(e);
        }

        gamePhaseHandler();
    }

    public void addPlayer(Player newPlayer) {
        if (addedPlayers.size() < numOfPlayers)
            addedPlayers.add(newPlayer);
        else
            System.out.println("The game is full");
    }

    public void setGameInstance() throws AlreadyAssignedException {
        if (this.gameInstance != null)
            throw new AlreadyAssignedException("The game has already been created");
        else
            this.gameInstance = new GameModel(addedPlayers/*, this*/);
    }

    public void gamePhaseHandler() {
        boolean prep = false;   boolean play = false;   boolean end = false;
        while (!gameInstance.getCurrentPhase().equals(GamePhase.FINISHED)) {
            if (gameInstance.getCurrentPhase().equals(GamePhase.PREPARATION) && !prep) {
                try {
                    gameInstance.preparationPhase();
                    prep = true;
                } catch (NoCardsException | AlreadyAssignedException e) {
                    throw new RuntimeException(e);
                }
            }

            if (gameInstance.getCurrentPhase().equals(GamePhase.PLAYING) && !play) {
                gameInstance.playingPhase();
                play = true;
            }

            if (gameInstance.getCurrentPhase().equals(GamePhase.ENDGAME) && !end) {
                gameInstance.endGamePhase();
                end = true;
            }
        }

    }

    //------------------------------------------------------------------------------------------------

    public void playerHasToChooseStartCardSide(Player p, StartCard c) throws WrongGamePhaseException {
        if (gameInstance.getCurrentPhase() == GamePhase.PREPARATION) {
            System.out.println("The player " + p.getNickname() + " has to choose one of the two Side of the StartCard given to him.");
            System.out.println(c.getFront().toString());
            System.out.println(c.getBack().toString());
            //Calls the method on the client with RMI or communicates with Socket TCP
            //IMPLEMENTING A STUB TO TEST THE PLAYER METHODS:
            playerChoosesStartCardSide(p, c, c.getFront());
        } else
            throw new WrongGamePhaseException("Method invoked in the wrong GamePhase");

    }

    public void playerChoosesStartCardSide(Player p, StartCard c, Side s) {     //THIS METHOD IS REMOTELY CALLED BY THE CLIENT
        /*Checks if the player has already chosen a Side.*/
        if (p.getMyKingdom() != null) {
            System.out.println("The player has already chosen the StartCard side.");
        } else {
            try {
                p.instantiateMyKingdom(c, s);
            } catch (AlreadyAssignedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void playerHasToChooseToken(Player p) throws AlreadyAssignedException {
        for (Token t: Token.values()) {
            boolean check = true;
            for (Player pl: gameInstance.getParticipants()) {
                if (pl.getToken().equals(t)) {
                    check = false;
                    break;
                }
            }
            if (check)
                p.setToken(t);
        }
        /*Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("Player " + p.getNickname() + "choose your token: (Type B for Blue, R for Red, Y for Yellow, G for Green)");
            String token = in.nextLine();
            Token toAssign = switch (token.toLowerCase()) {
                case "b" -> Token.BLUE;
                case "r" -> Token.RED;
                case "y" -> Token.YELLOW;
                case "g" -> Token.GREEN;
                default -> Token.BLUE;
            };

            if (toAssign.equals(Token.BLUE) && !token.equals("b"))
                System.out.println("Wrong token declaration");
            else {
                boolean check = false;
                for (Player pl: gameInstance.getParticipants())
                    if (pl.getToken() != null && toAssign.equals(p.getToken())) {
                        check = true;
                        break;
                    }

                if (!check) {
                    playerChoosesToken(p, toAssign);
                    break;
                } else
                    System.out.println("Token already chosen by someone else");
            }
        }*/
    }

    public void playerChoosesToken (Player p, Token t) throws AlreadyAssignedException {
        p.setToken(t);
    }

    public void playerHasToChooseObjective(Player p, ObjectiveCard[] cArray) throws WrongGamePhaseException { //STUB FOR TESTING
        if (gameInstance.getCurrentPhase() == GamePhase.PREPARATION) {
            System.out.println("The player " + p.getNickname() + " has to choose one of the two objective card given to him as his private objective.");
            System.out.println(cArray[0].toString());
            System.out.println(cArray[1].toString());
            //Calls the method on the client with RMI or communicates with Socket TCP
            //IMPLEMENTING A STUB TO TEST THE PLAYER METHODS:
            playerChoosesObjective(p, cArray[0]);
        } else
            throw new WrongGamePhaseException("Method invoked in the wrong GamePhase");
    }

    public void playerChoosesObjective(Player p, ObjectiveCard c) {     //THIS METHOD IS REMOTELY CALLED BY THE CLIENT
        /*Checks if the player has already chosen a card.*/
        if (p.getPrivateObjective() != null) {
            System.out.println("The player has already chosen his private objective.");
        } else {
            try {
                p.setPrivateObjective(c);
            } catch (AlreadyAssignedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean checkCurrentTurn(Player p) {
        return gameInstance.getCurrentTurn().equals(p);
    }

    public void notifyTurn(Player p) {
        /*
        * Notify the player that it is now his turn.
        *
        * Wait for that player to place a Card.
        *
        * Wait for that player to draw a card.
        *
        * */
        if (!gameInstance.getCurrentPhase().equals(GamePhase.PLAYING))
            return;
        if (!checkCurrentTurn(p)) {
            System.out.println("Wait for your turn, " + gameInstance.getCurrentTurn().getNickname() + " is currently playing");
            return;
        }

        Scanner in = new Scanner(System.in);
        System.out.println("Player " + p.getNickname() + " it is now your turn.");
        System.out.println("Your hand:");
        for (StandardCard c: p.getHand()) {
            System.out.println(c);
        }

        boolean placed = false;
        while (!placed) {
            System.out.println("Write the id of the Card you want to place: ");
            int id = in.nextInt();
            System.out.println("Write the Side of the Card you want to place (F for Front, B for Back): ");
            String s = in.nextLine();
            System.out.println("Write the x coordinate of the position of the Card you want to place: ");
            int x = in.nextInt();
            System.out.println("Write the y coordinate of the position of the Card you want to place: ");
            int y = in.nextInt();
            StandardCard sc = null;
            for (StandardCard c: p.getHand()) {
                if (c.getId() == id)
                    sc = c;
            }
            if (sc != null) {
                Side side = null;
                if (s.equalsIgnoreCase("f"))
                    side = sc.getFront();
                else if (s.equalsIgnoreCase("b"))
                    side = sc.getBack();

                if (side != null) {
                    for (Position pos: p.getMyKingdom().getActivePositions()) {
                        if (pos.getX() == x && pos.getY() == y) {
                            p.placeCard(sc, side, new Position(x, y));
                            placed = true;
                            break;
                        }
                    }
                    if (!placed) {
                        System.out.println("Choose a valid position from the following ones: ");
                        for (Position active: p.getMyKingdom().getActivePositions()) {
                            System.out.println(active);
                        }
                    }
                } else
                    System.out.println("Choose a valid side, write F or B.");
            } else
                System.out.println("The id you chose is not valid.");
        }

        System.out.println("Successfully placed the card.");

        System.out.println("Now draw a card. You can choose between the gold deck (gd), the resource deck (rd), " +
                "the available gold cards (agc) and the available resource cards (arc).");
        System.out.println("Gold deck first card colour: " + gameInstance.getGDeck().firstBack().getMainResource());
        System.out.println("Resource deck first card colour: " + gameInstance.getRDeck().firstBack().getMainResource());
        System.out.println("Available gold cards: ");
        System.out.println(gameInstance.getAvailableGCards().get(0));
        System.out.println(gameInstance.getAvailableGCards().get(1));
        System.out.println("Available resource cards: ");
        System.out.println(gameInstance.getAvailableRCards().get(0));
        System.out.println(gameInstance.getAvailableRCards().get(1));
        System.out.println("Make your choice:");

        boolean drawn = false;
        while (!drawn) {
            String choice = in.nextLine();
            switch (choice.toLowerCase()) {
                case "gd": {
                    try {
                        p.drawCardFromDeck(gameInstance.getGDeck()); drawn = true; break;
                    } catch (NoCardsException e) {
                        System.out.println("The Gold deck is empty.");
                    }
                }
                case "rd": {
                    try {
                        p.drawCardFromDeck(gameInstance.getRDeck()); drawn = true; break;
                    } catch (NoCardsException e) {
                        System.out.println("The Resource deck is empty.");
                    }
                }
                case "agc": {
                    try {
                        System.out.println("Now choose the first (1) or second (2) card.");
                        int i = in.nextInt();
                        if (i == 1) {
                            p.drawCardFromAvailable(gameInstance.getAvailableGCards().get(0));
                            drawn = true;
                            break;
                        } else if (i == 2) {
                            p.drawCardFromAvailable(gameInstance.getAvailableGCards().get(1));
                            drawn = true;
                            break;
                        } else
                            System.out.println("You must type either 1 or 2: ");
                    } catch (NoCardsException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "arc": {
                    try {
                        System.out.println("Now choose the first (1) or second (2) card.");
                        int i = in.nextInt();
                        if (i == 1) {
                            p.drawCardFromAvailable(gameInstance.getAvailableRCards().get(0)); drawn = true; break;
                        } else if (i == 2) {
                            p.drawCardFromAvailable(gameInstance.getAvailableRCards().get(1)); drawn = true; break;
                        } else
                            System.out.println("You must type either 1 or 2:");
                    } catch (NoCardsException e) {
                        System.out.println("The Resource deck is empty.");
                    }
                }
                default:
                    System.out.println("Wrong command. Remember to type one of the following: gd, rd, agc, arc: ");
            }
        }

        System.out.println("Player " + p.getNickname() + " your turn is ending.");
    }

    public void playerDrawsCardFromDeck(Player p, Deck d) { /*Checks if it is the player turn, checks if the player has already drawn a card.*/ }

    public void updatesDeckView(Deck d, Side s) {
        //Side toShow = d.firstBack();
    }

    public void updatePlayerHandView(Player p, StandardCard c) {}

    public void playerDrawsCardFromAvailable(Player p, StandardCard c) { /*Checks if it is the player turn, checks if the player has already drawn a card.*/ }

    public void updatesCardView(List<StandardCard> cList, StandardCard c) {}

    public void playerPlacesCard(Player p, StandardCard c, Side s, Position pos) { /*Checks if it is the player turn, checks if the player has already placed a card.*/ }

    public void updatesPlayerKingdomView(Player p, Kingdom k) {}

    public boolean tryReconnection(Player p) {return true;}

    public boolean checkConnection(Player p) {return true;}

    public void actionNotPermittedMessaging(Player p, String errorMessage) {}

}
