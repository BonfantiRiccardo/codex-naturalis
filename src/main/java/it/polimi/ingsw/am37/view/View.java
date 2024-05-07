package it.polimi.ingsw.am37.view;

import it.polimi.ingsw.am37.controller.Observable;

import java.util.List;

public abstract class View implements Observable {

    protected ViewState state;
    protected VirtualServer virtualServer;
    protected final ClientSideGameModel localGameInstance;
    protected volatile boolean newMessage;

    public View(ViewState state) {
        this.state = state;
        localGameInstance = new ClientSideGameModel();
    }

    public ViewState getState() {
        return state;
    }

    public void setState(ViewState state) {
        this.state = state;
    }

    public VirtualServer getVirtualServer() {
        return virtualServer;
    }

    public void setVirtualServer(VirtualServer virtualServer) {
        this.virtualServer = virtualServer;
    }

    public boolean getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(boolean newMessage) {
        this.newMessage = newMessage;
    }

    public ClientSideGameModel getLocalGameInstance() {
        return localGameInstance;
    }

    //---------------------------------------------------------------------------------

    public abstract boolean handleGame();

    public abstract void preLobby();

    public abstract void init();



    public abstract boolean gameOver();

    public abstract void printLobbies(List<Integer> lobbies);

    public abstract void printMyLobby();

    public abstract void printAvail();

    public abstract void printStartCard();

    public abstract void printKingdom();

    public abstract void printToken();

    public abstract void printHand();

    public abstract void printPublicObjectives();

    public abstract void printPrivateObjectives();

    public abstract void printMyPrivateObjective();

    public abstract void printError(String e);
}
