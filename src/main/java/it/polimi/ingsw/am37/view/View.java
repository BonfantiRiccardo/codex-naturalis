package it.polimi.ingsw.am37.view;

import it.polimi.ingsw.am37.view.clientmodel.ClientSideGameModel;
import it.polimi.ingsw.am37.view.virtualserver.VirtualServer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * This class is the abstract class that represents the view of the game.
 * It is used to print the game state to the user.
 */
public abstract class View implements PropertyChangeListener {
    /**
     * The state of the view.
     */
    protected ViewState state;
    /**
     * The virtual server.
     */
    protected VirtualServer virtualServer;
    /**
     * The local game instance.
     */
    protected ClientSideGameModel localGameInstance;

    /**
     * Constructor.
     * @param state The state of the view.
     */
    public View(ViewState state) {
        this.state = state;
        localGameInstance = new ClientSideGameModel();
        localGameInstance.setListener(this);
    }

    /**
     * Getter for the state.
     * @return The state of the view.
     */
    public ViewState getState() {
        return state;
    }

    /**
     * Setter for the state.
     * It also notifies the listener of the change.
     * It also checks if the state is changing from NOT_TURN to PLACE, in which case it notifies the listener of the change of turn.
     * @param state The state of the view.
     */
    public void setState(ViewState state) {
        synchronized (localGameInstance) { //CREATE A LOCK FOR THIS?
            System.out.println("STATE CHANGED: " + this.state + " -> " + state);
            if (this.state == ViewState.NOT_TURN && state == ViewState.PLACE) {
                PropertyChangeEvent evt = new PropertyChangeEvent(
                        this,
                        "CHANGED_TURN",
                        this.state,
                        state);
                localGameInstance.getListener().propertyChange(evt);
            } else {
                PropertyChangeEvent evt = new PropertyChangeEvent(
                        this,
                        "CHANGED_STATE",
                        this.state,
                        state);
                localGameInstance.getListener().propertyChange(evt);
            }
        }

        this.state = state;
    }

    /**
     * Getter for the virtual server.
     * @return The virtual server.
     */
    public VirtualServer getVirtualServer() {
        return virtualServer;
    }

    /**
     * Setter for the virtual server.
     * @param virtualServer The virtual server.
     */
    public void setVirtualServer(VirtualServer virtualServer) {
        this.virtualServer = virtualServer;
    }

    /**
     * Getter for the local game instance.
     * @return The local game instance.
     */
    public ClientSideGameModel getLocalGameInstance() {
        return localGameInstance;
    }

    //---------------------------------------------------------------------------------


    /**
     * This method is used to handle the game and is implemented in the TUIView and GUIView classes.
     * @return true if the game is still going on, false otherwise.
     */
    public abstract boolean handleGame();

    /**
     * This method is called when the game is over and is implemented in the TUIView and GUIView classes.
     * @return true if the game is over, false otherwise.
     */
    public abstract boolean gameOver();



    /**
     * This method is used to print an error message and is implemented in the TUIView and GUIView classes.
     * @param e The error message.
     */
    public abstract void printError(String e);
}
