package it.polimi.ingsw.am37.view.GUI;

import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;
import it.polimi.ingsw.am37.view.clientmodel.ClientSideGameModel;

import java.beans.PropertyChangeEvent;

/**
 * This class is the GUI view of the game. It extends the abstract class View.
 * It is used to handle the GUI of the game.
 */
public class GUIView extends View {
    /**
     * Constructor of the class.
     * @param state the state of the view.
     */
    public GUIView(ViewState state) {
        super(state);
    }

    /**
     * This method is used to handle the game.
     * It sets the GUI of the game and starts the GUI.
     * @return false
     */
    @Override
    public boolean handleGame() {

        GUIViewApplication.setGUI(this);
        GUIViewApplication.main(null);

        return false;
    }

    public void resetAfterDisconnection() {
        localGameInstance = new ClientSideGameModel();
        localGameInstance.setListener(this);
    }

    /**
     * This method is called when the game is over.
     * @return false.
     */
    @Override
    public boolean gameOver() {
        return false;
    }

    //-------------------------------------------------------------------------------

    /**
     * This method is used to get the lobbies.
     * It asks the virtual server to get the lobbies.
     */
    public void getLobbies() {
        virtualServer.askLobbies();
    }

    /**
     * This method is used to join a lobby.
     * It asks the virtual server to join a lobby.
     * @param lobby the lobby to join.
     * @param nickname the nickname of the player.
     */
    public void joinLobby(int lobby, String nickname) { virtualServer.joinLobby(lobby, nickname);}

    /**
     * This method is used to create a lobby.
     * It asks the virtual server to create a lobby.
     * @param nickname the nickname of the player.
     * @param numOfPlayers the number of players in the lobby.
     */
    public void createLobby(String nickname, int numOfPlayers) { virtualServer.createLobby(nickname, numOfPlayers);}

    public void chooseStartCard(String selectedSide) {
        virtualServer.placeStartCard(localGameInstance.getMe().getNickname(), localGameInstance.getMyStartCard().getId(), selectedSide, new Position(0,0));
    }

    //-------------------------------------------------------------------------------
    /**
     * This method is used to print all the lobbies.
     */
    @Override
    public void printLobbies() {

    }

    /**
     * This method is used to print the lobby of the player.
     */
    @Override
    public void printMyLobby() {

    }

    /**
     * This method is used to print the available cards on the field.
     */
    @Override
    public void printAvail() {

    }

    /**
     * This method is used to print the top of the gold deck.
     */
    @Override
    public void printTopOfGoldDeck() {

    }

    /**
     * This method is used to print the top of the resource deck.
     */
    @Override
    public void printTopOfResourceDeck() {

    }

    /**
     * This method is used to print the Starting card of the player.
     */
    @Override
    public void printStartCard() {

    }

    /**
     * This method is used to print the Kingdom of the player.
     */
    @Override
    public void printKingdom() {

    }

    /**
     * This method is used to print the scoreboard of the game.
     */
    @Override
    public void printScoreboard() {

    }

    /**
     * This method is used to print all the information of the player.??
     */
    @Override
    public void printPlayerInfo() {

    }

    /**
     * This method is used to print the hand of the player.
     */
    @Override
    public void printHand() {

    }

    /**
     * This method is used to print the public objectives of the game.
     */
    @Override
    public void printPublicObjectives() {

    }

    /**
     * This method is used to print the private objectives available to choose from.
     */
    @Override
    public void printPrivateObjectives() {

    }

    /**
     * This method is used to print the private objective of the player.
     */
    @Override
    public void printMyPrivateObjective() {

    }

    /**
     * This method is used to print the results of the game.
     */
    @Override
    public void printResults() {

    }

    /**
     * This method is used to print an error message.
     * It sends the error message to the listener.
     * @param e the error message.
     */
    @Override
    public void printError(String e) {
        PropertyChangeEvent evt = new PropertyChangeEvent(
                this,
                "ERROR",
                null,
                e);
        localGameInstance.getListener().propertyChange(evt);
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

            }
            case "CHANGED_KINGDOM": {
                //ADD UPDATE STRING THAT WILL BE PRINTED TO LET THE THREAD KNOW THE KINGDOM HAS BEEN UPDATED

            }
            case "CHANGED_DECK": {
                //SET A VARIABLE TO LET THE THREAD KNOW THE DECK HAS BEEN UPDATED

            }
            case "CHANGED_AVAILABLE": {
                //SET A VARIABLE TO LET THE THREAD KNOW THE HAND HAS BEEN UPDATED

            }
            case "CHANGED_HAND": {
                //SET A VARIABLE TO LET THE THREAD KNOW THE HAND HAS BEEN UPDATED

            }
            case "NEW_TURN": {
                //SET A VARIABLE TO LET THE THREAD KNOW THE HAND HAS BEEN UPDATED

            }
            case "ENDGAME": {
                //SET A VARIABLE TO LET THE THREAD KNOW THE HAND HAS BEEN UPDATED

            }
            case "LAST_TURN": {
                //SET A VARIABLE TO LET THE THREAD KNOW THE HAND HAS BEEN UPDATED

            }
            case "RESULTS": {
                //SET A VARIABLE TO LET THE THREAD KNOW THE HAND HAS BEEN UPDATED

            }
        }
    }
}
