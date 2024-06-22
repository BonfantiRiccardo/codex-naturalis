package it.polimi.ingsw.am37.view.GUI;

import it.polimi.ingsw.am37.model.player.Token;
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

    /**
     * This method is used to reset the client model after after a game.
     */
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

    /**
     * This method is used to place the start card. It asks the virtual server to place the start card.
     * @param selectedSide the side of the start card.
     */
    public void chooseStartCard(String selectedSide) {
        virtualServer.placeStartCard(localGameInstance.getMe().getNickname(), localGameInstance.getMyStartCard().getId(), selectedSide, new Position(0,0));
    }

    /**
     * This method is used to choose a token.
     * It asks the virtual server to choose a token.
     * @param token the chosen token.
     */
    public void chooseToken(Token token) {
        virtualServer.chooseToken(localGameInstance.getMe().getNickname(), token);
    }

    /**
     * This method is used to choose a private objective.
     * It asks the virtual server to choose a private objective.
     * @param id the id of the private objective.
     */
    public void choosePrivateObjective(int id) {
        virtualServer.chooseObjective(localGameInstance.getMe().getNickname(), id);
    }

    /**
     * This method is used to draw from a deck.
     * It asks the virtual server to draw a card from the deck.
     * @param deck the code of the deck from which we are drawing.
     */
    public void drawFromDeck(String deck) {
        virtualServer.drawCardFromDeck(localGameInstance.getMe().getNickname(), deck);
    }

    /**
     * This method is used to draw from the available cards.
     * It asks the virtual server to draw a card from the available cards.
     * @param cardId the id of the card to draw.
     */
    public void drawFromAvail(int cardId) {
        virtualServer.drawCardFromAvailable(localGameInstance.getMe().getNickname(), cardId);
    }

    /**
     * This method is used to place a card.
     * It asks the virtual server to place a card.
     * @param cardId the id of the card to place.
     * @param side the side of the card.
     * @param position the position of the card.
     */
    public void placeCard(int cardId, String side, Position position) {
        virtualServer.placeCard(localGameInstance.getMe().getNickname(), cardId, side, position);
    }

    //-------------------------------------------------------------------------------
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
                state,
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

    }
}
