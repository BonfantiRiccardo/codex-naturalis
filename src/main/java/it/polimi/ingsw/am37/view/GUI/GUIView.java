package it.polimi.ingsw.am37.view.GUI;

import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;

import java.beans.PropertyChangeEvent;

public class GUIView extends View {
    public GUIView(ViewState state) {
        super(state);
    }

    @Override
    public boolean handleGame() {

        GUIViewApplication.setGUI(this);
        GUIViewApplication.main(null);

        return false;
    }


    @Override
    public boolean gameOver() {
        return false;
    }

    //-------------------------------------------------------------------------------

    public void getLobbies() {
        virtualServer.askLobbies();
    }

    public void joinLobby(int lobby, String nickname) { virtualServer.joinLobby(lobby, nickname);}

    public void createLobby(String nickname, int numOfPlayers) { virtualServer.createLobby(nickname, numOfPlayers);}

    //-------------------------------------------------------------------------------
    @Override
    public void printLobbies() {

    }

    @Override
    public void printMyLobby() {

    }

    @Override
    public void printAvail() {

    }

    @Override
    public void printTopOfGoldDeck() {

    }

    @Override
    public void printTopOfResourceDeck() {

    }

    @Override
    public void printStartCard() {

    }

    @Override
    public void printKingdom() {

    }

    @Override
    public void printScoreboard() {

    }

    @Override
    public void printPlayerInfo() {

    }

    @Override
    public void printHand() {

    }

    @Override
    public void printPublicObjectives() {

    }

    @Override
    public void printPrivateObjectives() {

    }

    @Override
    public void printMyPrivateObjective() {

    }

    @Override
    public void printResults() {

    }

    @Override
    public void printError(String e) {
        PropertyChangeEvent evt = new PropertyChangeEvent(
                this,
                "ERROR",
                null,
                e);
        localGameInstance.getListener().propertyChange(evt);
    }

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
