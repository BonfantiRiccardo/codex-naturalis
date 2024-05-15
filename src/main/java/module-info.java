module it.polimi.ingsw.am37 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;
    requires java.rmi;
    requires java.desktop;

    opens it.polimi.ingsw.am37 to javafx.fxml;
    exports it.polimi.ingsw.am37;

    exports it.polimi.ingsw.am37.model.cards;
    exports it.polimi.ingsw.am37.model.cards.objective;
    exports it.polimi.ingsw.am37.model.cards.placeable;
    exports it.polimi.ingsw.am37.model.decks;
    exports it.polimi.ingsw.am37.exceptions;
    exports it.polimi.ingsw.am37.model.game;
    exports it.polimi.ingsw.am37.model.player;
    exports it.polimi.ingsw.am37.model.sides;
    exports it.polimi.ingsw.am37.controller;
    exports it.polimi.ingsw.am37.controller.states;
    exports it.polimi.ingsw.am37.server;
    exports it.polimi.ingsw.am37.messages;
    exports it.polimi.ingsw.am37.client;
    exports it.polimi.ingsw.am37.view;
    exports it.polimi.ingsw.am37.messages.lobby;
    exports it.polimi.ingsw.am37.messages.initialization;

    opens it.polimi.ingsw.am37.model.cards to com.google.gson;
    opens it.polimi.ingsw.am37.model.cards.objective to com.google.gson;
    opens it.polimi.ingsw.am37.model.cards.placeable to com.google.gson;
    opens it.polimi.ingsw.am37.model.sides to com.google.gson;
    opens it.polimi.ingsw.am37.view to com.google.gson;

    opens it.polimi.ingsw.am37.server to javafx.fxml;
    opens it.polimi.ingsw.am37.messages to javafx.fxml;
    opens it.polimi.ingsw.am37.messages.lobby to javafx.fxml;
    opens it.polimi.ingsw.am37.messages.initialization to javafx.fxml;
    exports it.polimi.ingsw.am37.messages.endgame;
    opens it.polimi.ingsw.am37.messages.endgame to javafx.fxml;

}