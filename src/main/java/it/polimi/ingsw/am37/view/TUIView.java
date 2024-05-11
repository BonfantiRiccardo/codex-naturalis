package it.polimi.ingsw.am37.view;

import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.player.Kingdom;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TUIView extends View implements PropertyChangeListener {
    private final Scanner stdIn;
    private final List<String> updates;

    public TUIView(ViewState state) {
        super(state);
        stdIn = new Scanner(System.in);
        updates = new ArrayList<>();
    }

    public boolean handleGame() {
        new Thread(this::handleUpdates).start();

        preLobby();

        printMyLobby();
        if (localGameInstance.getPlayers().size() + 1 < localGameInstance.getNumOfPlayers()) {
            System.out.println();
            System.out.println("Now waiting for all players.");
        }

        synchronized (this) {
            while (state.equals(ViewState.WAIT_IN_LOBBY)) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (state.equals(ViewState.WAIT_IN_LOBBY))
                    printMyLobby();
            }
            //System.out.println();
            //System.out.println("Printing game initialization updates...");      //DOESN'T PRINT ON TOP ALL THE TIMES
        }

        //while(!updates.isEmpty()) Thread.onSpinWait();      //SHOULD BE OVERKILL

        while (!state.equals(ViewState.SHOW_RESULTS)) {
            //print: This changed while you requested the action:
            while(!updates.isEmpty()) Thread.onSpinWait();
            //WAITS FOR ALL UPDATES TO PRINT AND REMOVES THEM FROM THE LIST

            System.out.println();
            System.out.println("Press enter to request an ACTION");         //VERY IMPORTANT SO THAT I DON'T HAVE TO BLOCK
            stdIn.nextLine();

            String inputLine;
            synchronized (this) {
                System.out.println("Choose an action from the list: ");
                activeActions();
                inputLine = stdIn.nextLine();

                choice(inputLine.toLowerCase());
            }
        }



        return gameOver();
    }

    @Override
    public synchronized void preLobby() {
        boolean error = false;

        while (state.equals(ViewState.CREATE_JOIN)) {
            String inputLine;
            System.out.println("create or join? ");
            inputLine = stdIn.nextLine();

            if (inputLine.equalsIgnoreCase("join"))
                joinQueries();
            else if (inputLine.equalsIgnoreCase("create"))
                creationQueries();
            else {
                System.out.println("Error in the request: only type 'create' if you want to create a lobby or 'join' if you want to join an existing one");
                error = true;
            }

            while (!error && state.equals(ViewState.CREATE_JOIN)) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            error = false;

            if (state.equals(ViewState.ERROR))
                state = ViewState.CREATE_JOIN;

        }

    }       //SHOULDN'T BE PUBLIC AND SHOULDN'T OVERRIDE

    private void creationQueries() {
        System.out.print("Nickname: ");
        final String inputLine2 = stdIn.nextLine();

        System.out.print("Num: ");
        final String inputNumStr = stdIn.nextLine();
        final int inputNum = Integer.parseInt(inputNumStr);

        virtualServer.createLobby(inputLine2, inputNum);
    }

    private void joinQueries() {
        virtualServer.askLobbies();

        while (state.equals(ViewState.CREATE_JOIN)) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (state.equals(ViewState.ERROR))
            return;

        printLobbies(localGameInstance.getListOfLobbies());

        System.out.print("Choose ID of the game you want to join: ");
        final String inputNumStr = stdIn.nextLine();
        final int inputNum = Integer.parseInt(inputNumStr);

        System.out.print("Nickname: ");
        final String inputLine = stdIn.nextLine();

        state = ViewState.CREATE_JOIN;

        virtualServer.joinLobby(inputNum, inputLine);
    }

    private void activeActions() {
        switch (state) {
            case PLACE_SC -> System.out.println("Place Start Card, Print Available, Chat, Cancel"); //DO I NEED TO BREAK?
            case CHOOSE_TOKEN -> System.out.println("Choose Token, Print Available, Chat, Cancel"); //DO I NEED TO BREAK?
            case CHOOSE_OBJECTIVE -> System.out.println("Choose Objective, Print Hand, Print Public Objectives, Print Decks, Print Available, Chat, Cancel"); //DO I NEED TO BREAK?
            case NOT_TURN -> System.out.println("Print Kingdom, Print Hand, Print Public Objectives, Print Private Objective, " +
                    "Print Decks, Print Available, Print Information, Chat, Cancel"); //DO I NEED TO BREAK?
            case PLACE -> System.out.println("Place, Print Kingdom, Print Hand, Print Public Objectives, Print Private Objective, " +
                    "Print Decks, Print Available, Print Information, Chat, Cancel");
            case DRAW -> System.out.println("Draw, Print Kingdom, Print Hand, Print Public Objectives, Print Private Objective, " +
                    "Print Decks, Print Available, Print Player Info, Chat, Cancel");
        }
    }

    private void choice(String s) {
        switch (s) {
            case "place start card": {
                if (state.equals(ViewState.PLACE_SC))
                    startCardQueries();
                else
                    System.out.println("The action you chose is not valid");
                break;
            }
            case "choose token": {
                if (state.equals(ViewState.CHOOSE_TOKEN))
                    tokenQueries();
                else
                    System.out.println("The action you chose is not valid");
                break;
            }
            case "choose objective": {
                if (state.equals(ViewState.CHOOSE_OBJECTIVE))
                    objectiveQueries();
                else
                    System.out.println("The action you chose is not valid");
                break;
            }
            case "place": {
                if (state.equals(ViewState.PLACE))
                    placeQueries();
                else
                    System.out.println("The action you chose is not valid");
                break;
            }
            case "draw": {
                if (state.equals(ViewState.DRAW));
                    //drawQueries();
                else
                    System.out.println("The action you chose is not valid");
                break;
            }
            case "print kingdom": {
                //CHECK THE STATE
                printKingdom();
                break;
            }
            case "print hand": {
                //CHECK THE STATE
                printHand();
                break;
            }
            case "print public objectives": {
                //CHECK THE STATE
                printPublicObjectives();
                break;
            }
            case "print private objective": {
                //CHECK THE STATE
                printMyPrivateObjective();
                break;
            }
            case "print decks": {
                //CHECK THE STATE
                printTopOfResourceDeck();
                printTopOfGoldDeck();
                break;
            }
            case "print available": {
                //CHECK THE STATE
                printAvail();
                break;
            }
            case "print player info": {
                //CHECK THE STATE
                //ask which player, ask which info, print it
                break;
            }
            case "chat": {
                //ask receiver, ask message, print message
                break;
            }
            case "cancel": {
                break;
            }
            default: {
                System.out.println("Wrong request");
                break;
            }
        }
    }

    private void startCardQueries() {
        String inputLine;

            while (state.equals(ViewState.PLACE_SC)) {
                printStartCard();
                System.out.print("Choose the Start Card Side you want to place by typing 'f' for the Front or 'b' for the Back: ");
                inputLine = stdIn.nextLine();

                if (inputLine.equalsIgnoreCase("f") || inputLine.equalsIgnoreCase("b")) {
                    virtualServer.placeStartCard(localGameInstance.getMe().getNickname(), localGameInstance.getMyStartCard().getId(), inputLine, new Position(0,0));

                    while (state.equals(ViewState.PLACE_SC)) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }


                    if (state.equals(ViewState.ERROR))
                        state = ViewState.PLACE_SC;
                    else {
                        if (inputLine.equalsIgnoreCase("f"))
                            localGameInstance.getMe().setKingdom(new Kingdom(localGameInstance.getMyStartCard(),
                                    localGameInstance.getMyStartCard().getFront()));
                        else if (inputLine.equalsIgnoreCase("b"))
                            localGameInstance.getMe().setKingdom(new Kingdom(localGameInstance.getMyStartCard(),
                                    localGameInstance.getMyStartCard().getBack()));
                    }

                } else
                    System.out.println("Error in the action: only type 'f' if you want to place the Front or 'b' if you want to place the Back");

            }

        //wait for everyone else to choose
        for (ClientSidePlayer p: localGameInstance.getPlayers())
            if (p.getKingdom() == null) {
                System.out.println("Wait for everyone to place their card, " + p.getNickname() + " has yet to play");
            }

        for (ClientSidePlayer p: localGameInstance.getPlayers())
            while (p.getKingdom() == null) Thread.onSpinWait();
    }

    private void tokenQueries() {
        String inputLine;
        while (state.equals(ViewState.CHOOSE_TOKEN)) {
            boolean error = false;
            System.out.println("Currently available tokens: " + localGameInstance.getTokens());
            System.out.print("Choose the Token you want (type 'b' for blue, 'y' for yellow, 'r' for red, 'g' for green): ");
            inputLine = stdIn.nextLine();

            if (inputLine.equalsIgnoreCase("b"))      //CHANGE
                virtualServer.chooseToken(localGameInstance.getMe().getNickname(), Token.BLUE);
            else if (inputLine.equalsIgnoreCase("y"))
                virtualServer.chooseToken(localGameInstance.getMe().getNickname(), Token.YELLOW);
            else if (inputLine.equalsIgnoreCase("r"))
                virtualServer.chooseToken(localGameInstance.getMe().getNickname(), Token.RED);
            else if (inputLine.equalsIgnoreCase("g"))
                virtualServer.chooseToken(localGameInstance.getMe().getNickname(), Token.GREEN);
            else {
                System.out.println("Error in the action: only type 'b', 'y', 'r', 'g'");
                error = true;
            }


            while (!error && state.equals(ViewState.CHOOSE_TOKEN)) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            if (state.equals(ViewState.ERROR))
                state = ViewState.CHOOSE_TOKEN;
            else {
                if (inputLine.equalsIgnoreCase("b"))
                    localGameInstance.getMe().setToken(Token.BLUE);
                else if (inputLine.equalsIgnoreCase("y"))
                    localGameInstance.getMe().setToken(Token.YELLOW);
                else if (inputLine.equalsIgnoreCase("r"))
                    localGameInstance.getMe().setToken(Token.RED);
                else if (inputLine.equalsIgnoreCase("g"))
                    localGameInstance.getMe().setToken(Token.GREEN);
            }
        }

        //wait for everyone else to choose
        for (ClientSidePlayer p: localGameInstance.getPlayers())
            if (p.getToken() == null) {
                System.out.println("Wait for everyone to choose their token, " + p.getNickname() + " has yet to play");
            }

        for (ClientSidePlayer p: localGameInstance.getPlayers())
            while (p.getToken() == null) Thread.onSpinWait();

    }

    private void objectiveQueries() {
        String inputLine;
        while (state.equals(ViewState.CHOOSE_OBJECTIVE)) {

            System.out.println("These are your objectives:");
            printPrivateObjectives();
            System.out.print("Choose between them by typing the id: ");
            inputLine = stdIn.nextLine();
            int inputNum = Integer.parseInt(inputLine);

            if (inputNum == localGameInstance.getPrivateObjectives().get(0).getId() || inputNum == localGameInstance.getPrivateObjectives().get(1).getId()) {
                virtualServer.chooseObjective(localGameInstance.getMe().getNickname(), inputNum);

                while (state.equals(ViewState.CHOOSE_OBJECTIVE)) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (state.equals(ViewState.ERROR))
                    state = ViewState.CHOOSE_OBJECTIVE;
                else {
                    if (inputNum == localGameInstance.getPrivateObjectives().get(0).getId())
                        localGameInstance.setMyPrivateObjective(localGameInstance.getPrivateObjectives().get(0));
                    else if (inputNum == localGameInstance.getPrivateObjectives().get(1).getId())
                        localGameInstance.setMyPrivateObjective(localGameInstance.getPrivateObjectives().get(1));
                }

            } else
                System.out.println("Error in the action: type the id of the objective card you want to select.");

        }


    }

    private void placeQueries() {
        String inputLine;

        //This is your hand
        printHand();
        //This is your kingdom
        printKingdom();
        //These are your resources
        //These are the active positions

        while (state.equals(ViewState.PLACE)) {
            //Choose a card by typing the id
            System.out.print("Choose a card by typing the id: ");
            inputLine = stdIn.nextLine();

            int inputNum = Integer.parseInt(inputLine);
            //print the card
            boolean choiceOk = false;
            for (StandardCard c: localGameInstance.getMyHand())
                if (c.getId() == inputNum)
                    choiceOk = true;


            if (choiceOk) {
                //Choose a side by typing f or b (type r for return to the id)
                String side;
                System.out.print("Choose the side of the card you want to place by typing 'f' for front and 'b' for back, or type 'r' to return to the id choice: ");
                side = stdIn.nextLine();

                while (true) {
                    if (side.equals("f") || side.equals("b")) {
                        //Choose the position by typing x coordinate |space| y coordinate (or r for return)
                        String pos;
                        while (true) {
                            System.out.print("Choose the position where you want to place the card by typing 'x y', or type 'r' to return to the id choice: ");
                            pos = stdIn.nextLine();

                            String[] coord = pos.split(" ");
                            if (coord.length == 2) {
                                try {
                                    int x = Integer.parseInt(coord[0]);
                                    int y = Integer.parseInt(coord[1]);

                                    virtualServer.placeCard(localGameInstance.getMe().getNickname(), inputNum, side, new Position(x, y));


                                    while (state.equals(ViewState.PLACE)) {
                                        try {
                                            this.wait();
                                        } catch (InterruptedException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }

                                    if (state.equals(ViewState.ERROR))
                                        state = ViewState.PLACE;
                                    else
                                        localGameInstance.placeCard(localGameInstance.getMe().getNickname(), inputNum, side, new Position(x, y));

                                    break;

                                } catch (NumberFormatException e) {
                                    System.out.println("You must write two number separated by a space");
                                }

                            } else if (coord.length == 1 && coord[0].equals("r"))
                                break;
                            else
                                System.out.println("Type two integer separated by a space");
                        }

                        break;
                    } else
                        System.out.println("Only type 'f' for front and 'b' for back");
                }

            } else
                System.out.println("Choose the id of a card you have in your hand.");
        }
    }

    private void drawQueries() {}

    public boolean gameOver() {
        System.out.println("Play again? ('yes' or 'no')");
        while (true) {
            final String inputLine = stdIn.nextLine();

            if (inputLine.equalsIgnoreCase("yes")) {
                state = ViewState.CREATE_JOIN;
                return true;
            } else if (inputLine.equalsIgnoreCase("no"))
                return false;
            else
                System.out.println("Wrong command, only type 'yes' or 'no'");
        }
    }



    @Override
    public synchronized void printLobbies(List<Integer> lobbies) {
        System.out.println("Active lobbies: " + lobbies);
    }

    @Override
    public synchronized void printMyLobby() {
        System.out.println();
        System.out.println("Num of lobby: " + localGameInstance.getNumOfLobby() + " | you: " + localGameInstance.getMe().getNickname());
        if (!localGameInstance.getPlayers().isEmpty()) {
            System.out.print("These players have joined: ");
            int count = localGameInstance.getPlayers().size();
            for(ClientSidePlayer p: localGameInstance.getPlayers()) {
                System.out.print(p.getNickname());
                if (count > 1) {
                    System.out.print(", ");
                    count--;
                }
            }
            System.out.println();

        } else
            System.out.println("No player joined yet");

        System.out.println("Total players needed for the game to start: " + (localGameInstance.getNumOfPlayers()
                                                                                - 1 - localGameInstance.getPlayers().size()));
    }

    @Override
    public synchronized void printAvail() {
        System.out.println("Available resource cards: ");
        System.out.println(localGameInstance.getAvailableResourceCards().get(0));
        System.out.println(localGameInstance.getAvailableResourceCards().get(1));
        System.out.println("\nAvailable gold cards: ");
        System.out.println(localGameInstance.getAvailableGoldCards().get(0));
        System.out.println(localGameInstance.getAvailableGoldCards().get(1));
    }

    @Override
    public synchronized void printTopOfGoldDeck() {
        System.out.println("Card at the top of the gold deck is of resource: " + localGameInstance.getTopOfGoldDeck());
    }

    @Override
    public synchronized void printTopOfResourceDeck() {
        System.out.println("Card at the top of the resource deck is of resource: " + localGameInstance.getTopOfResourceDeck());
    }

    @Override
    public synchronized void printStartCard() {
        System.out.println("Your start card: ");
        System.out.println(localGameInstance.getMyStartCard());
    }

    @Override
    public synchronized void printKingdom() {
        System.out.println("Your kingdom: " + localGameInstance.getMe().getKingdom());
        System.out.println("Your resources: " + localGameInstance.getMe().getKingdom().getOnFieldResources());
        System.out.println("Your active positions: " + localGameInstance.getMe().getKingdom().getActivePositions());
    }

    @Override
    public synchronized void printToken() {
        System.out.println("Your token: " + localGameInstance.getMe().getToken());
    }

    @Override
    public synchronized void printHand() {
        System.out.println("Your hand: ");
        System.out.println(localGameInstance.getMyHand().get(0));
        System.out.println(localGameInstance.getMyHand().get(1));
        System.out.println(localGameInstance.getMyHand().get(2));
    }

    @Override
    public synchronized void printPublicObjectives() {
        System.out.println("The public objectives are: ");
        System.out.println(localGameInstance.getPublicObjectives().get(0));
        System.out.println(localGameInstance.getPublicObjectives().get(1));
    }

    @Override
    public synchronized void printPrivateObjectives() {
        System.out.println(localGameInstance.getPrivateObjectives().get(0));
        System.out.println(localGameInstance.getPrivateObjectives().get(1));
    }

    @Override
    public synchronized void printMyPrivateObjective() {
        System.out.println("Your private objective:");
        System.out.println(localGameInstance.getMyPrivateObjective());
    }

    @Override
    public synchronized void printError(String e) {
        System.out.println("Error message: " + e);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "CHANGED_TURN": {
                //ADD UPDATE STRING THAT WILL BE PRINTED TO LET THE THREAD KNOW THE KINGDOM HAS BEEN UPDATED
                synchronized (updates) {
                    updates.add("It is now your turn, press enter");
                }
                break;
            }
            case "CHANGED_KINGDOM": {
                //ADD UPDATE STRING THAT WILL BE PRINTED TO LET THE THREAD KNOW THE KINGDOM HAS BEEN UPDATED
                synchronized (updates) {
                    updates.add("One of the kingdom has changed");
                }
                break;
            }
            case "CHANGED_DECK": {
                //SET A VARIABLE TO LET THE THREAD KNOW THE DECK HAS BEEN UPDATED
                synchronized (updates) {
                    updates.add("One of the decks has changed");
                }
                break;
            }
            case "CHANGED_AVAILABLE": {
                //SET A VARIABLE TO LET THE THREAD KNOW THE HAND HAS BEEN UPDATED
                synchronized (updates) {
                    updates.add("One of the available cards has changed");
                }
                break;
            }
            case "CHANGED_HAND": {
                //SET A VARIABLE TO LET THE THREAD KNOW THE HAND HAS BEEN UPDATED
                synchronized (updates) {
                    updates.add("Your hand has changed");
                }
                break;
            }
            //ECC
        }
    }

    private void handleUpdates() {
        while (true) {
            while (updates.isEmpty()) Thread.onSpinWait();

            if(state.equals(ViewState.SHOW_RESULTS))
                break;
            else {
                synchronized (this) {
                    synchronized (updates) {
                        System.out.println("\nYou received updates from the server: ");
                        for (String s: updates)
                            System.out.println(s);

                        updates.clear();
                    }
                }
            }
        }
    }

}
