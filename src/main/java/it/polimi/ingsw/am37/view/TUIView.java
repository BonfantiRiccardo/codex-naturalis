package it.polimi.ingsw.am37.view;

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
        preLobby();

        printMyLobby();
        if (state.equals(ViewState.WAIT_IN_LOBBY))
            System.out.println("Now waiting for all players.");
        synchronized (this) {
            while (state.equals(ViewState.WAIT_IN_LOBBY)) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                printMyLobby();
            }
        }

        init();

        while (!state.equals(ViewState.SHOW_RESULTS)) {
            //PRINT UPDATES AND REMOVE THEM FROM THE LIST
            System.out.println("Press enter to request an ACTION");         //VERY IMPORTANT SO THAT I DON'T HAVE TO BLOCK


            String inputLine;
            synchronized (this) {
                System.out.println("Choose an action (list of actions): ");
                inputLine = stdIn.nextLine();
            }

            choice(inputLine);
        }



        return gameOver();
    }

    @Override
    public synchronized void preLobby() {
        boolean error = false;

        while (state.equals(ViewState.CREATE_JOIN)) {
            String inputLine;
            System.out.println("create or join?");
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

    }

    private void creationQueries() {
        System.out.println("Nickname: ");
        final String inputLine2 = stdIn.nextLine();

        System.out.println("Num: ");
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

        System.out.println("Choose ID of the game you want to join: ");
        final String inputNumStr = stdIn.nextLine();
        final int inputNum = Integer.parseInt(inputNumStr);

        System.out.println("Nickname: ");
        final String inputLine = stdIn.nextLine();

        state = ViewState.CREATE_JOIN;

        virtualServer.joinLobby(inputNum, inputLine);
    }

    private void choice(String s) {
        switch (s) {
            case "place card": {

            }
            case "see kingdom": {

            }
            //ECC
        }
    }

    @Override
    public void init() {
        String inputLine;

        synchronized (this) {

            while (state.equals(ViewState.PLACE_SC)) {
                printStartCard();
                System.out.println("Choose the Start Card Side you want to place by typing 'f' for the Front or 'b' for the Back:");
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
        }

        //wait for everyone else to choose
        for (ClientSidePlayer p: localGameInstance.getPlayers())
            if (p.getKingdom() == null) {
                System.out.println("Wait for everyone to place their card, " + p.getNickname() + " has yet to play");
            }

        for (ClientSidePlayer p: localGameInstance.getPlayers())
            while (p.getKingdom() == null) Thread.onSpinWait();


        //TOKEN PHASE ------------------------------------------------------------------------------------------------

        synchronized (this) {
            while (state.equals(ViewState.CHOOSE_TOKEN)) {
                boolean error = false;
                System.out.println("Currently available tokens: " + localGameInstance.getTokens());
                System.out.println("Choose the Token you want (type 'b' for blue, 'y' for yellow, 'r' for red, 'g' for green):");
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
        }

        //wait for everyone else to choose
        for (ClientSidePlayer p: localGameInstance.getPlayers())
            if (p.getToken() == null) {
                System.out.println("Wait for everyone to choose their token, " + p.getNickname() + " has yet to play");
            }

        for (ClientSidePlayer p: localGameInstance.getPlayers())
            while (p.getToken() == null) Thread.onSpinWait();


        //OBJECTIVE PHASE ----------------------------------------------------------------------------------------------

        synchronized (this) {
            while (state.equals(ViewState.CHOOSE_OBJECTIVE)) {

                System.out.println("These are your objectives:");
                printPrivateObjectives();
                System.out.println("Choose between them by typing the id:");
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


        //do not wait for everyone else to choose and go straight to "open" request phase
    }

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
    public void printLobbies(List<Integer> lobbies) {
        System.out.println("Active lobbies: " + lobbies);
    }

    @Override
    public void printMyLobby() {
        System.out.println("Num of lobby: " + localGameInstance.getNumOfLobby() + " | you: " + localGameInstance.getMe().getNickname());
        System.out.print("These players have joined: ");
        int count = localGameInstance.getPlayers().size();
        for(ClientSidePlayer p: localGameInstance.getPlayers()) {
            System.out.print(p.getNickname());
            if(count > 1) {
                System.out.print(", ");
                count--;
            }
        }

        System.out.println();
        System.out.println("Total players needed for the game to start: " + localGameInstance.getNumOfPlayers());
    }

    @Override
    public void printAvail() {
        System.out.println("Available resource cards: " + localGameInstance.getAvailableResourceCards());
        System.out.println("Available gold cards: " + localGameInstance.getAvailableGoldCards());
    }

    @Override
    public void printStartCard() {
        System.out.println("Your start card: " + localGameInstance.getMyStartCard());
    }

    @Override
    public void printKingdom() {
        System.out.println("Your kingdom: " + localGameInstance.getMe().getKingdom());
    }

    @Override
    public void printToken() {
        System.out.println("Your token: " + localGameInstance.getMe().getToken());
    }

    @Override
    public void printHand() {
        System.out.println("Your hand: " + localGameInstance.getMyHand());
    }

    @Override
    public void printPublicObjectives() {
        System.out.println("The public objectives are: " + localGameInstance.getPublicObjectives());
    }

    @Override
    public void printPrivateObjectives() {
        System.out.println(localGameInstance.getPrivateObjectives());
    }

    @Override
    public void printMyPrivateObjective() {
        System.out.println("Your private objective:" + localGameInstance.getMyPrivateObjective());
    }

    @Override
    public void printError(String e) {
        System.out.println("Error message: " + e);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "CHANGED_KINGDOM": {
                //ADD UPDATE STRING THAT WILL BE PRINTED TO LET THE THREAD KNOW THE KINGDOM HAS BEEN UPDATED
            }
            case "CHANGED_DECK": {
                //SET A VARIABLE TO LET THE THREAD KNOW THE DECK HAS BEEN UPDATED
            }
            case "CHANGED_HAND": {
                //SET A VARIABLE TO LET THE THREAD KNOW THE HAND HAS BEEN UPDATED
            }
            //ECC
        }
    }
}
