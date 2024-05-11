package it.polimi.ingsw.am37.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.List;

public abstract class View implements PropertyChangeListener {

    protected ViewState state;
    protected VirtualServer virtualServer;
    protected final ClientSideGameModel localGameInstance;

    public View(ViewState state) {
        this.state = state;
        localGameInstance = new ClientSideGameModel();
        localGameInstance.setListener(this);
    }

    public ViewState getState() {
        return state;
    }

    public void setState(ViewState state) {
        if (this.state == ViewState.NOT_TURN && state == ViewState.PLACE) { //ASTRARRE CON UN EVENTO CHANGED_STATE CHE POI VIENE GESTITO IN
            PropertyChangeEvent evt = new PropertyChangeEvent(              //propertyChange(evt); PERCHè mi serve anche per fase iniziale
                    this,
                    "CHANGED_TURN",
                    this.state,
                    state);
            propertyChange(evt);
        }

        this.state = state;
    }

    public VirtualServer getVirtualServer() {
        return virtualServer;
    }

    public void setVirtualServer(VirtualServer virtualServer) {
        this.virtualServer = virtualServer;
    }

    public ClientSideGameModel getLocalGameInstance() {
        return localGameInstance;
    }

    //---------------------------------------------------------------------------------

    public abstract boolean handleGame();

    public abstract void preLobby();

    //public abstract void init();



    public abstract boolean gameOver();

    public abstract void printLobbies(List<Integer> lobbies);

    public abstract void printMyLobby();

    public abstract void printAvail();

    public abstract void printTopOfGoldDeck();

    public abstract void printTopOfResourceDeck();

    public abstract void printStartCard();

    public abstract void printKingdom();

    public abstract void printToken();

    public abstract void printHand();

    public abstract void printPublicObjectives();

    public abstract void printPrivateObjectives();

    public abstract void printMyPrivateObjective();

    public abstract void printError(String e);
}
