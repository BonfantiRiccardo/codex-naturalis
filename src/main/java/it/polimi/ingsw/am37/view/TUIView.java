package it.polimi.ingsw.am37.view;

import it.polimi.ingsw.am37.model.sides.Position;

import java.util.List;
import java.util.Scanner;

public class TUIView extends View {
    private final Scanner stdIn;

    public TUIView(ViewState state) {
        super(state);
        stdIn = new Scanner(System.in);
    }

    public boolean handleGame() {
        preLobby();


        System.out.println("Now waiting for all players.");
        synchronized (this) {
            while (state.equals(ViewState.WAIT_IN_LOBBY)) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        init();



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

    @Override
    public void init() {
        String inputLine;

        synchronized (this) {
            newMessage = false;

            while (state.equals(ViewState.PLACE_SC)) {
                printStartCard();
                System.out.println("Choose the Start Card Side you want to place by typing 'f' for the Front or 'b' for the Back:");
                inputLine = stdIn.nextLine();

                if (inputLine.equalsIgnoreCase("f") || inputLine.equalsIgnoreCase("b")) {
                    virtualServer.placeStartCard(localGameInstance.getMe().getNickname(), localGameInstance.getMyStartCard().getId(), inputLine, new Position(0,0));

                    while (!newMessage) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    newMessage = false;
                }
               else
                   System.out.println("Error in the action: only type 'f' if you want to place the Front or 'b' if you want to place the Back");

            }
        }

        //wait for everyone else to place   OR SKIP AND WAIT AT THE END
        //for (ClientSidePlayer p: localGameInstance.getPlayers())
        //    while (p.getKingdom() == null) Thread.onSpinWait();

        while (state.equals(ViewState.CHOOSE_TOKEN)) {
            boolean error = false;
            System.out.println("Choose the Token you want (type 'b' for blue, 'y' for yellow, 'r' for red, 'g' for green):");
            inputLine = stdIn.nextLine();

            if (inputLine.equalsIgnoreCase("f"))      //CHANGE
                virtualServer.chooseToken();
            else if (inputLine.equalsIgnoreCase("b"))
                virtualServer.chooseToken();
            else {
                System.out.println("Error in the action: only type 'b', 'y', 'r', 'g'");
                error = true;
            }


            while (!error && localGameInstance.getMe().getKingdom() == null) {      //CHANGE
                try {
                    localGameInstance.getMe().getKingdom().wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        //wait for everyone else to choose


        while (state.equals(ViewState.CHOOSE_OBJECTIVE)) {
            boolean error = false;
            System.out.println("This are your objectives:");
            printPrivateObjectives();
            System.out.println("Choose between them by typing 'first' or 'second':");
            inputLine = stdIn.nextLine();

            if (inputLine.equalsIgnoreCase("first") || inputLine.equalsIgnoreCase("b"))
                virtualServer.chooseObjective();
            else {      //CHANGE
                System.out.println("Error in the action: only type 'f' if you want to place the Front or 'b' if you want to place the Back");
                error = true;
            }


            while (!error && localGameInstance.getMe().getKingdom() == null) {      //CHANGE
                try {
                    localGameInstance.getMe().getKingdom().wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
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
        for(ClientSidePlayer p: localGameInstance.getPlayers())
            System.out.print(p.getNickname());

        System.out.println(" | total players needed for the game to start: " + localGameInstance.getNumOfPlayers());
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
}
