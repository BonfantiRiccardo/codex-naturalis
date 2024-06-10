package it.polimi.ingsw.am37.view.TUI;

import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Kingdom;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.view.clientmodel.ClientSideGameModel;
import it.polimi.ingsw.am37.view.clientmodel.ClientSidePlayer;
import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * This class is the text user interface of the game.
 */
public class TUIView extends View implements PropertyChangeListener {
    /**
     * The scanner for the standard input.
     */
    private final Scanner stdIn;
    /**
     * The list of updates.
     */
    private final List<String> updates;
    /**
     * The boolean that checks if the utf8 encoding is enabled.
     */
    private boolean utf8EncodingEnabled;

    /**
     * Constructor.
     * @param state The state of the view.
     */
    public TUIView(ViewState state) {
        super(state);
        stdIn = new Scanner(System.in);
        updates = new ArrayList<>();
    }

    /**
     * This method handles all the game.
     * it firstly asks the user if the terminal supports utf8 encoding.
     * Then calls the preLobby method.
     * Then prints the lobby.
     * Then waits for all players to join.
     * Then starts a new thread that handles the updates.
     * Then waits for the user to press enter to request an action.
     * Then calls the activeActions method.
     * Then calls the choice method.
     * If the state is SHOW_RESULTS or DISCONNECTION it prints the results or a disconnection message.
     * finally it calls the gameOver method.
     * @return true if the user wants to play again, false otherwise.
     */
    public boolean handleGame() {

        System.out.print("This games uses emojis to better visualize cards. This are some emojis: ⬜🐺📜\n" +
                            "If the terminal correctly displays them type 'y', otherwise type  'n': ");
        utf8EncodingEnabled = !stdIn.nextLine().equals("n");  //equals: if (stdIn.nextLine().equals("n")) utf8EncodingEnabled = false;
                                                                     // else utf8EncodingEnabled = true;

        preLobby();

        printMyLobby();
        if (localGameInstance.getPlayers().size() + 1 < localGameInstance.getNumOfPlayers()) {
            System.out.println();
            System.out.println("Now waiting for all players.");
        }

        //synchronized (this) {
        int players = localGameInstance.getPlayers().size();
            while (state.equals(ViewState.WAIT_IN_LOBBY)) { Thread.onSpinWait();
                /*try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }*/
                if (players != localGameInstance.getPlayers().size()) {
                    printMyLobby();
                    players = localGameInstance.getPlayers().size();
                }
            }
        //}
        new Thread(this::handleUpdates).start();

        while (!state.equals(ViewState.SHOW_RESULTS) && !state.equals(ViewState.DISCONNECTION)) {

            while(!updates.isEmpty()) Thread.onSpinWait();
            //WAITS FOR ALL UPDATES TO PRINT AND REMOVES THEM FROM THE LIST

            System.out.println();
            System.out.println("Press enter to request an ACTION");         //VERY IMPORTANT SO THAT I DON'T HAVE TO BLOCK
            stdIn.nextLine();

            if (state.equals(ViewState.SHOW_RESULTS) || state.equals(ViewState.DISCONNECTION))
                break;

            String inputLine;
            synchronized (this) {
                System.out.println("Choose an action from the list: ");
                activeActions();
                inputLine = stdIn.nextLine();

                choice(inputLine.toLowerCase());
            }
        }

        if (state.equals(ViewState.SHOW_RESULTS))
            printResults();
        else if (state.equals(ViewState.DISCONNECTION))
            System.out.println("\nOne of the player was disconnected from the game, the game is now ending for everyone...\n");

        return gameOver();
    }

    /**
     * This method asks the player to choose between creating or joining a lobby.
     * If the player chooses to join a lobby it calls the joinQueries method.
     * If the player chooses to create a lobby it calls the creationQueries method.
     * If the player types something different it prints an error message.
     * If the state is ERROR it prints an error message.
     * If the state is different from CREATE_JOIN it waits for the player to choose again.
     * If the state is ERROR it sets the state to CREATE_JOIN.
     */
    public void preLobby() {
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
                synchronized(this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            error = false;

            if (state.equals(ViewState.ERROR))
                state = ViewState.CREATE_JOIN;

        }

    }       //SHOULDN'T BE PUBLIC AND SHOULDN'T OVERRIDE

    /**
     * This method asks for the nickname and the number of players during the creation of the lobby.
     * If the nickname is empty it prints an error message.
     * If the number of players is not a number it prints an error message.
     * If the number of players is a number it calls the createLobby method.
     *
     */
    private void creationQueries() {
        String inputLine2;
        do {
            System.out.print("Nickname: ");
            inputLine2 = stdIn.nextLine();
            if (inputLine2.isEmpty())
                System.out.println("Empty nickname is not valid!");
        } while (inputLine2.isEmpty());

        boolean ok = false;
        int inputNum;
        do {
            System.out.print("Num: ");
            final String inputNumStr = stdIn.nextLine();

            try {
                inputNum = Integer.parseInt(inputNumStr);
                ok = true;

                virtualServer.createLobby(inputLine2, inputNum);
            } catch (NumberFormatException e) {
                System.out.println("You should ONLY write numbers!");
            }
        } while (!ok);
    }

    /**
     * This method shows the lobbies available and asks the player to choose one.
     * If the player types something different it prints an error message.
     * If the player types a number it asks for the player's nickname and calls the joinLobby method.
     *
     */
    private void joinQueries() {
        virtualServer.askLobbies();

        while (state.equals(ViewState.CREATE_JOIN)) {
            synchronized(this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (state.equals(ViewState.ERROR))
            return;

        printLobbies();

        boolean ok = false;
        int inputNum = 0;
        do {
            System.out.print("Choose ID of the game you want to join: ");
            final String inputNumStr = stdIn.nextLine();

            try {
                inputNum = Integer.parseInt(inputNumStr);
                ok = true;

            } catch (NumberFormatException e) {
                System.out.println("You should ONLY write numbers!");
            }
        } while (!ok);


        String inputLine;
        do {
            System.out.print("Nickname: ");
            inputLine = stdIn.nextLine();
            if (inputLine.isEmpty())
                System.out.println("Empty nickname is not valid!");
        } while (inputLine.isEmpty());

        state = ViewState.CREATE_JOIN;

        virtualServer.joinLobby(inputNum, inputLine);
    }

    /**
     * This method handles the active actions.
     * It prints the actions available based on the state.
     */
    private void activeActions() {
        switch (state) {
            case PLACE_SC -> System.out.println("Place Start Card (psc), Print Available (pa), Cancel (c)");

            case CHOOSE_TOKEN -> System.out.println("Choose Token (ct), Print Available (pa), Cancel (c)");

            case CHOOSE_OBJECTIVE -> System.out.println("Choose Objective (co), Print Hand (ph), Print Public Objectives (ppo), Print Decks (pd), " +
                                                        "Print Available (pa), Cancel (c)");

            case NOT_TURN -> System.out.println("Print Kingdom (pk), Print Hand (ph), Print Public Objectives (ppo), Print Private Objective (pro), " +
                                        "Print Decks (pd), Print Available (pa),\nPrint Scoreboard (ps), Print Player Info (ppi), Cancel (c)");

            case PLACE -> System.out.println("Place (p), Print Kingdom (pk), Print Hand (ph), Print Public Objectives (ppo), " +
                                        "Print Private Objective (pro), Print Decks (pd), Print Available (pa),\nPrint Scoreboard (ps), " +
                                        "Print Player Info (ppi), Cancel (c)");

            case DRAW -> System.out.println("Draw (d), Print Kingdom (pk), Print Hand (ph), Print Public Objectives (ppo), " +
                                        "Print Private Objective (pro), Print Decks (pd), Print Available (pa),\nPrint Scoreboard (ps), " +
                                        "Print Player Info (ppi), Cancel (c)");
        }
    }

    /**
     * This method handles the choices of the player.
     * if the player wants to place the start card it calls the startCardQueries method.
     * if the player wants to choose a token it calls the tokenQueries method.
     * if the player wants to choose an objective it calls the objectiveQueries method.
     * if the player wants to place a card it calls the placeQueries method.
     * if the player wants to draw a card it calls the drawQueries method.
     * if the player wants to print the kingdom it calls the printKingdom method.
     * if the player wants to print the hand it calls the printHand method.
     * if the player wants to print the public objectives it calls the printPublicObjectives method.
     * if the player wants to print the private objective it calls the printMyPrivateObjective method.
     * if the player wants to print the top of the decks it calls the printTopOfResourceDeck and printTopOfGoldDeck methods.
     * if the player wants to print the scoreboard it calls the printScoreboard method.
     * if the player wants to print the player info it calls the printPlayerInfo method.
     * if the player wants to cancel it prints a message.
     * if the player types something different it prints an error message.
     * @param s The string that represents the choice of the player.
     */
    private void choice(String s) {
        switch (s) {
            case "psc": {
                if (state.equals(ViewState.PLACE_SC))
                    startCardQueries();
                else
                    System.out.println("The action you chose is not valid");
                break;
            }
            case "ct": {
                if (state.equals(ViewState.CHOOSE_TOKEN))
                    tokenQueries();
                else
                    System.out.println("The action you chose is not valid");
                break;
            }
            case "co": {
                if (state.equals(ViewState.CHOOSE_OBJECTIVE))
                    objectiveQueries();
                else
                    System.out.println("The action you chose is not valid");
                break;
            }
            case "p": {
                if (state.equals(ViewState.PLACE))
                    placeQueries();
                else
                    System.out.println("The action you chose is not valid");
                break;
            }
            case "d": {
                if (state.equals(ViewState.DRAW))
                    drawQueries();
                else
                    System.out.println("The action you chose is not valid");
                break;
            }
            case "pk": {
                if (state.equals(ViewState.NOT_TURN) || state.equals(ViewState.PLACE) || state.equals(ViewState.DRAW))
                    printKingdom();
                else
                    System.out.println("The action you chose is not valid");
                break;
            }
            case "ph": {
                if (!state.equals(ViewState.PLACE_SC) && !state.equals(ViewState.CHOOSE_TOKEN))
                    printHand();
                else
                    System.out.println("The action you chose is not valid");
                break;
            }
            case "ppo": {
                if (!state.equals(ViewState.PLACE_SC) && !state.equals(ViewState.CHOOSE_TOKEN))
                    printPublicObjectives();
                else
                    System.out.println("The action you chose is not valid");
                break;
            }
            case "pro": {
                if (state.equals(ViewState.NOT_TURN) || state.equals(ViewState.PLACE) || state.equals(ViewState.DRAW))
                    printMyPrivateObjective();
                else
                    System.out.println("The action you chose is not valid");
                break;
            }
            case "pd": {
                if (!state.equals(ViewState.PLACE_SC) && !state.equals(ViewState.CHOOSE_TOKEN)) {
                    printTopOfResourceDeck();
                    printTopOfGoldDeck();
                } else
                    System.out.println("The action you chose is not valid");
                break;
            }
            case "pa": {
                printAvail();
                break;
            }
            case "ps": {
                if (state.equals(ViewState.NOT_TURN) || state.equals(ViewState.PLACE) || state.equals(ViewState.DRAW)) {
                    printScoreboard();
                } else
                    System.out.println("The action you chose is not valid");
                break;
            }
            case "ppi": {
                if (state.equals(ViewState.NOT_TURN) || state.equals(ViewState.PLACE) || state.equals(ViewState.DRAW)) {
                    printPlayerInfo();
                } else
                    System.out.println("The action you chose is not valid");
                break;
            }
            case "c": {
                System.out.println("Action canceled");
                break;
            }
            default: {
                System.out.println("Wrong request");
                break;
            }
        }
    }

    /**
     * This method handles the choice of the start card.
     * It asks the player to choose the side of the start card.
     * If the player types something different it prints an error message.
     * If the player types a valid side it calls the placeStartCard method.
     * If the state is ERROR it sets the state to PLACE_SC.
     * If the player chooses the front side it sets the kingdom of the player to the front side of the start card.
     * If the player chooses the back side it sets the kingdom of the player to the back side of the start card.
     */
    private void startCardQueries() {
        String inputLine;
        printStartCard();

        while (state.equals(ViewState.PLACE_SC)) {
            System.out.print("Choose the Start Card Side you want to place by typing 'f' for the Front or 'b' for the Back: ");
            inputLine = stdIn.nextLine();

            if (inputLine.equalsIgnoreCase("f") || inputLine.equalsIgnoreCase("b")) {
                virtualServer.placeStartCard(localGameInstance.getMe().getNickname(), localGameInstance.getMyStartCard().getId(), inputLine, new Position(0,0));

                while (state.equals(ViewState.PLACE_SC)) { //Thread.onSpinWait();
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
            while (p.getKingdom() == null && !state.equals(ViewState.DISCONNECTION)) Thread.onSpinWait();
    }

    /**
     * This method handles the choice of the token.
     * It asks the player to choose a token.
     * If the player types something different it prints an error message.
     * If the player types a valid token it calls the chooseToken method.
     * If the state is ERROR it sets the state to CHOOSE_TOKEN.
     * Then waits for all players to choose a token.
     */
    private void tokenQueries() {
        String inputLine;
        while (state.equals(ViewState.CHOOSE_TOKEN)) {
            boolean error = false;
            System.out.println("Currently available tokens: " + localGameInstance.getTokens());
            System.out.print("Choose the Token you want (type 'b' for blue, 'y' for yellow, 'r' for red, 'g' for green): ");
            inputLine = stdIn.nextLine();

            if (inputLine.equalsIgnoreCase("b"))
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
            while (p.getToken() == null && !state.equals(ViewState.DISCONNECTION)) Thread.onSpinWait();

    }

    /**
     * This method handles the choice of the objective.
     * It asks the player to choose an objective.
     * If the player types something different it prints an error message.
     * If the player types a valid objective it calls the chooseObjective method.
     * If the state is ERROR it sets the state to CHOOSE_OBJECTIVE.
     * If the player chooses the first objective it sets the private objective of the player to the first objective.
     * If the player chooses the second objective it sets the private objective of the player to the second objective.
     * Then waits for all players to choose an objective.
     */
    private void objectiveQueries() {
        String inputLine;
        while (state.equals(ViewState.CHOOSE_OBJECTIVE)) {

            System.out.println("These are your objectives:");
            printPrivateObjectives();

            boolean ok = false;
            int inputNum = 0;
            do {
                System.out.print("Choose between them by typing the id: ");
                inputLine = stdIn.nextLine();

                try {
                    inputNum = Integer.parseInt(inputLine);
                    ok = true;
                } catch (NumberFormatException e) {
                    System.out.println("You should ONLY write numbers!");
                }
            } while (!ok);

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

    /**
     * This method handles the placement of the cards.
     * It asks the player to choose a card from the hand.
     * If the player types something different it prints an error message.
     * If the player types a valid card it asks the player to choose a side.
     * Then asks the player to choose the side of the card and the position.
     * If the player types something different it prints an error message.
     * If the player types a valid position it calls the placeCard method and informs the player the card has been placed.
     * If the state is ERROR it sets the state to PLACE.
     * If the state is different from PLACE it waits for the player to choose again.
     */
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
            boolean ok = false;
            int inputNum = 0;
            do {
                System.out.print("Choose a card by typing the id: ");
                inputLine = stdIn.nextLine();

                try {
                    inputNum = Integer.parseInt(inputLine);
                    ok = true;
                } catch (NumberFormatException e) {
                    System.out.println("You should ONLY write numbers!");
                }
            } while (!ok);



            boolean choiceOk = false;
            for (StandardCard c: localGameInstance.getMyHand())
                if (c.getId() == inputNum) {
                    choiceOk = true;
                    break;
                }

            if (choiceOk) {
                //Choose a side by typing f or b (type r for return to the id)
                String side;
                System.out.print("Choose the side of the card you want to place by typing 'f' for front and 'b' for back, or type 'r' to return to the id choice: ");

                while (true) {
                    side = stdIn.nextLine();

                    if (side.equalsIgnoreCase("f") || side.equalsIgnoreCase("b")) {
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
                                    else {
                                        localGameInstance.placeCard(localGameInstance.getMe().getNickname(), inputNum, side, new Position(x, y));
                                        System.out.println("Correctly placed card: " + inputLine + ", side: " + side + ", in position: " + x + " " + y);
                                    }

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
                    } else if (side.equalsIgnoreCase("r")) {
                        break;
                    } else
                        System.out.print("Only type 'f' for front or 'b' for back: ");
                }

            } else
                System.out.println("Choose the id of a card you have in your hand.");
        }
    }

    /**
     * This method handles the drawing of the cards.
     * It asks the player to choose between drawing from the deck or from the available cards.
     * If the player types something different it prints an error message.
     * If the player chooses to draw from the deck it asks the player to choose between the resource deck and the gold deck.
     * If the player types something different it prints an error message.
     * If the player chooses to draw from the resource deck it calls the drawCardFromDeck method.
     * If the player chooses to draw from the gold deck it calls the drawCardFromDeck method.
     * If the state is ERROR it sets the state to DRAW.
     * If the player chooses to draw from the available cards it asks the player to choose a card by typing the id.
     * If the player types something different it prints an error message.
     * If the player types a valid card it calls the drawCardFromAvailable method.
     * If the state is ERROR it sets the state to DRAW.
     * If the player types 'b' it goes back to the previous choice.
     * If the state is different from DRAW it waits for the player to choose again.
     */
    private void drawQueries() {
        //these are the top of the decks
        printTopOfResourceDeck();
        printTopOfGoldDeck();

        System.out.println();
        //these are the available cards
        printAvail();

        //choose deck or available
        String inputLine;
        while (state.equals(ViewState.DRAW)) {
            //Choose a card by typing the id
            System.out.print("Choose if you want to draw from the deck (d) or from the available cards (a): ");
            inputLine = stdIn.nextLine();

            if (inputLine.equalsIgnoreCase("d")) {
                //Choose a deck
                String deck;
                System.out.print("Do you want to draw from the resource deck (r) or the gold deck (g), you can also go back to the previous choice (b): ");

                while (true) {
                    deck = stdIn.nextLine();

                    if (deck.equalsIgnoreCase("r") || deck.equalsIgnoreCase("g")) {
                        virtualServer.drawCardFromDeck(localGameInstance.getMe().getNickname(), deck);

                        while (state.equals(ViewState.DRAW)) {
                            try {
                                this.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        if (state.equals(ViewState.ERROR))
                            state = ViewState.DRAW;
                        else
                            System.out.println("Correctly drawn card");

                        break;
                    } else if (deck.equalsIgnoreCase("b"))
                        break;
                    else
                        System.out.print("Only type 'r' for resource deck or 'g' for gold deck (or 'b' to go back): ");
                }

            } else if (inputLine.equalsIgnoreCase("a")) {

                String inputNum;
                boolean ok = false;
                do {
                    System.out.print("Choose a card by typing the id, or type 'b' to go back: ");

                    try {
                        while (true) {
                            inputNum = stdIn.nextLine();

                            if (inputNum.equalsIgnoreCase("b"))
                                break;


                            int cardId = Integer.parseInt(inputNum);

                            StandardCard toDraw = null;

                            for (StandardCard sc: localGameInstance.getAvailableGoldCards())
                                if (sc.getId() == cardId) {
                                    toDraw = sc;
                                }

                            for (StandardCard sc: localGameInstance.getAvailableResourceCards())
                                if (sc.getId() == cardId) {
                                    toDraw = sc;
                                }

                            if (toDraw != null) {
                                virtualServer.drawCardFromAvailable(localGameInstance.getMe().getNickname(), cardId);

                                while (state.equals(ViewState.DRAW)) {
                                    try {
                                        this.wait();
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                                if (state.equals(ViewState.ERROR))
                                    state = ViewState.DRAW;
                                else {
                                    localGameInstance.getMyHand().add(toDraw);
                                    System.out.println("Correctly drawn card");
                                }

                                break;
                            } else
                                System.out.print("Type the id of an available card (or 'b' to go back): ");
                        }

                        ok = true;
                    } catch (NumberFormatException e) {
                        System.out.println("You should ONLY write numbers!");
                    }



                } while (!ok);



            } else
                System.out.println("Just write 'd' for deck and 'a' for available");
        }

    }

    /**
     * This method handles the end of the game.
     * It asks the player if he wants to play again.
     * If the player types something different it prints an error message.
     * If the player types 'yes' it sets the state to CREATE_JOIN and resets the client model.
     * If the player types 'no' it returns false.
     * @return true if the player wants to play again, false otherwise.
     */
    public boolean gameOver() {

        System.out.println("Play again? ('yes' or 'no')");
        while (true) {
            final String inputLine = stdIn.nextLine();

            if (inputLine.equalsIgnoreCase("yes")) {
                state = ViewState.CREATE_JOIN;
                //RESETS THE CLIENT MODEL
                localGameInstance = new ClientSideGameModel();
                localGameInstance.setListener(this);
                updates.clear();

                return true;
            } else if (inputLine.equalsIgnoreCase("no"))
                return false;
            else
                System.out.println("Wrong command, only type 'yes' or 'no'");
        }
    }


    //------------------------------------------------------------------------------------------------------------

    /**
     * This method prints the active lobbies.
     */
    @Override
    public synchronized void printLobbies() {
        System.out.println("Active lobbies: " + localGameInstance.getListOfLobbies());
    }

    /**
     * This method prints the infos of the lobby requested.
     */
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

    /**
     * This method prints the available resource and gold cards.
     */
    @Override
    public synchronized void printAvail() {
        System.out.println("Available resource cards: ");
        System.out.println("id: " + localGameInstance.getAvailableResourceCards().get(0).getId());
        System.out.println(localGameInstance.getAvailableResourceCards().get(0).toString(utf8EncodingEnabled));
        System.out.println("id: " + localGameInstance.getAvailableResourceCards().get(1).getId());
        System.out.println(localGameInstance.getAvailableResourceCards().get(1).toString(utf8EncodingEnabled));
        System.out.println("\nAvailable gold cards: ");
        System.out.println("id: " + localGameInstance.getAvailableGoldCards().get(0).getId());
        System.out.println(localGameInstance.getAvailableGoldCards().get(0).toString(utf8EncodingEnabled));
        System.out.println("id: " + localGameInstance.getAvailableGoldCards().get(1).getId());
        System.out.println(localGameInstance.getAvailableGoldCards().get(1).toString(utf8EncodingEnabled));
    }

    /**
     * This method prints the top card of the gold deck.
     */
    @Override
    public synchronized void printTopOfGoldDeck() {
        System.out.println("Card at the top of the gold deck is: " + localGameInstance.getTopOfGoldDeck());
    }

    /**
     * This method prints the top card of the resource deck.
     */
    @Override
    public synchronized void printTopOfResourceDeck() {
        System.out.println("Card at the top of the resource deck is: " + localGameInstance.getTopOfResourceDeck());
    }

    /**
     * This method prints the start card of the player.
     */
    @Override
    public synchronized void printStartCard() {
        System.out.println("Your start card: ");
        System.out.println(localGameInstance.getMyStartCard().toString(utf8EncodingEnabled));
    }

    /**
     * This method prints the token, the kingdom, the resources and the active positions in the kingdom of the player.
     */
    @Override
    public synchronized void printKingdom() {
        System.out.println("Your token: " + localGameInstance.getMe().getToken()+ (localGameInstance.getMe().hasBlackToken()? (" and " + Token.BLACK) : "" ));
        System.out.println("Your kingdom: ");
        String[][] field = localGameInstance.getMe().getKingdom().getVisual(utf8EncodingEnabled);

        for (String[] strings : field) {
            for (int j = 0; j < field[0].length; j++)
                System.out.print(strings[j]);
            System.out.println();
        }

        System.out.print("Your resources: ");
        for (Resource r: Resource.values()) {
            if (localGameInstance.getMe().getKingdom().getOnFieldResources().get(r) != null && !r.equals(Resource.EMPTY)) {
                System.out.print(r + ": " + localGameInstance.getMe().getKingdom().getOnFieldResources().get(r));

                if (!r.equals(Resource.QUILL))
                    System.out.print(",  ");
            }
        }
        System.out.println();

        System.out.print("Your active positions: ");
        int newLine = 0;
        for (Position p: localGameInstance.getMe().getKingdom().getActivePositions()) {
            if (newLine == 8) {
                System.out.println();
                newLine = 0;
            }

            System.out.print(p);
            if (!p.equals(localGameInstance.getMe().getKingdom().getActivePositions().getLast()))
                System.out.print(" | ");

            newLine++;
        }
        System.out.println();

    }

    /**
     * This method prints the points the player and the scoreboard of the game.
     */
    @Override
    public synchronized void printScoreboard() {
        System.out.println("Your points: " + localGameInstance.getMe().getPoints());

        for (ClientSidePlayer p: localGameInstance.getPlayers())
            System.out.println(p.getNickname() + "'s points: " + p.getPoints());
    }

    /**
     * This method prints the info of the player requested.
     * If the nickname of the player requested does not exist it prints an error message.
     * If the nickname of the player requested exists it prints the token, the kingdom, the resources of the player requested.
     */
    @Override
    public synchronized void printPlayerInfo() {
        String inputLine;
        System.out.print("Write the name of the player you want to request info: ");
        inputLine = stdIn.nextLine();

        boolean check = false;
        for (ClientSidePlayer p: localGameInstance.getPlayers()) {
            if (p.getNickname().equalsIgnoreCase(inputLine)) {
                System.out.println(p.getNickname() + "'s token: " + p.getToken() + (p.hasBlackToken()? (" and " + Token.BLACK) : "" ));
                System.out.println(p.getNickname() + "'s kingdom: ");
                String[][] field = p.getKingdom().getVisual(utf8EncodingEnabled);

                for (String[] strings : field) {
                    for (int j = 0; j < field[0].length; j++)
                        System.out.print(strings[j]);
                    System.out.println();
                }


                System.out.print(p.getNickname() + "'s resources: ");
                for (Resource r: Resource.values())
                    if (p.getKingdom().getOnFieldResources().get(r) != null && !r.equals(Resource.EMPTY)) {
                        System.out.print(r + ": " + p.getKingdom().getOnFieldResources().get(r));

                        if (!r.equals(Resource.QUILL))
                            System.out.print(",  ");
                    }
                check = true;
            }
        }

        if (!check)
            System.out.println("Error in the request, nickname of player might not exists");

    }

    /**
     * This method prints the hand of the player.
     */
    @Override
    public synchronized void printHand() {
        System.out.println("Your hand: ");
        for (StandardCard sc: localGameInstance.getMyHand()) {
            System.out.println("id: " + sc.getId());
            System.out.println(sc.toString(utf8EncodingEnabled));
        }
    }

    /**
     * This method prints the public objectives of the game.
     */
    @Override
    public synchronized void printPublicObjectives() {
        System.out.println("The public objectives are: ");
        System.out.println(localGameInstance.getPublicObjectives().get(0).toString(utf8EncodingEnabled));
        System.out.println(localGameInstance.getPublicObjectives().get(1).toString(utf8EncodingEnabled));
    }

    /**
     * This method prints the private objectives available to the player.
     */
    @Override
    public synchronized void printPrivateObjectives() {
        System.out.println(localGameInstance.getPrivateObjectives().get(0).toString(utf8EncodingEnabled));
        System.out.println(localGameInstance.getPrivateObjectives().get(1).toString(utf8EncodingEnabled));
    }

    /**
     * This method prints the private objective of the player.
     */
    @Override
    public synchronized void printMyPrivateObjective() {
        System.out.println("Your private objective:");
        System.out.println(localGameInstance.getMyPrivateObjective().toString(utf8EncodingEnabled));
    }

    /**
     * This method prints the results of the game.
     * It prints the winner of the game and the points and the objectives completed of each player.
     */
    @Override
    public synchronized void printResults(){
        List<ClientSidePlayer> players = new ArrayList<>(getLocalGameInstance().getPlayers());
        List<ClientSidePlayer> playerTable = new ArrayList<>(getLocalGameInstance().getPlayers());
        int max;

        players.add(getLocalGameInstance().getMe());
        playerTable.add(getLocalGameInstance().getMe());
        for(int i = 0; i < players.size(); i++) {
            max = players.getFirst().getPoints();
            for (ClientSidePlayer p : players) {
                if (max < p.getPoints())
                    playerTable.set(i, p);
            }
            players.remove(playerTable.get(i));
        }

        System.out.println();
        System.out.println("THE WINNER IS: " + playerTable.getFirst().getNickname());

        for(ClientSidePlayer p : playerTable)
            System.out.println(p.getNickname() + " has achieved " + p.getPoints() + " points and completed " + p.getObjectivesCompleted() + " objectives");

        System.out.println();
    }

    /**
     * This method prints the error message.
     * @param e The error message to print.
     */
    @Override
    public void printError(String e) {
        System.out.println("Error message: " + e);
    }

    /**
     * This method is used to notify the player of a new event in the game.
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
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
            case "NEW_TURN": {
                //SET A VARIABLE TO LET THE THREAD KNOW THE HAND HAS BEEN UPDATED
                synchronized (updates) {
                    if (!localGameInstance.getMe().getNickname().equals(evt.getNewValue()))
                        updates.add("The turn has changed, it is now " + evt.getNewValue() + "'s turn");
                }
                break;
            }
            case "ENDGAME": {
                //SET A VARIABLE TO LET THE THREAD KNOW THE HAND HAS BEEN UPDATED
                synchronized (updates) {
                   updates.add("One of the players reached 20 points, we are now in the ENDGAME");
                }
                break;
            }
            case "LAST_TURN": {
                //SET A VARIABLE TO LET THE THREAD KNOW THE HAND HAS BEEN UPDATED
                synchronized (updates) {
                    updates.add("This will be the last turn");
                }
                break;
            }
            case "RESULTS": {
                //SET A VARIABLE TO LET THE THREAD KNOW THE HAND HAS BEEN UPDATED
                synchronized (updates) {
                    updates.add("The game is OVER, press ENTER to check the results");
                }
                break;
            }
        }
    }

    /**
     * This method is used to handle the updates received from the server.
     * If the state is SHOW_RESULTS it breaks the loop.
     */
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
