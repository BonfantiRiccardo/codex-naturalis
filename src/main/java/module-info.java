module it.polimi.ingsw.am37 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;
    requires java.rmi;
    requires java.desktop;


    //exports it.polimi.ingsw.am37;

    exports it.polimi.ingsw.am37.controller;
    exports it.polimi.ingsw.am37.controller.states;
    exports it.polimi.ingsw.am37.controller.virtualview;

    exports it.polimi.ingsw.am37.exceptions;

    exports it.polimi.ingsw.am37.model.cards;
    exports it.polimi.ingsw.am37.model.cards.objective;
    exports it.polimi.ingsw.am37.model.cards.placeable;
    exports it.polimi.ingsw.am37.model.decks;
    exports it.polimi.ingsw.am37.model.game;
    exports it.polimi.ingsw.am37.model.player;
    exports it.polimi.ingsw.am37.model.sides;

    exports it.polimi.ingsw.am37.network.client;
    exports it.polimi.ingsw.am37.network.server;
    exports it.polimi.ingsw.am37.network.messages;
    exports it.polimi.ingsw.am37.network.messages.lobby;
    exports it.polimi.ingsw.am37.network.messages.initialization;
    exports it.polimi.ingsw.am37.network.messages.mixed;
    exports it.polimi.ingsw.am37.network.messages.turns;
    exports it.polimi.ingsw.am37.network.messages.endgame;

    exports it.polimi.ingsw.am37.view;
    exports it.polimi.ingsw.am37.view.clientmodel;
    exports it.polimi.ingsw.am37.view.GUI;
    exports it.polimi.ingsw.am37.view.GUI.controllers;
    exports it.polimi.ingsw.am37.view.TUI;
    exports it.polimi.ingsw.am37.view.virtualserver;


    opens it.polimi.ingsw.am37.model.cards to com.google.gson;
    opens it.polimi.ingsw.am37.model.cards.objective to com.google.gson;
    opens it.polimi.ingsw.am37.model.cards.placeable to com.google.gson;
    opens it.polimi.ingsw.am37.model.sides to com.google.gson;


    opens it.polimi.ingsw.am37 to javafx.fxml;

    opens it.polimi.ingsw.am37.network.client to javafx.fxml;
    opens it.polimi.ingsw.am37.network.server to javafx.fxml;
    opens it.polimi.ingsw.am37.network.messages to javafx.fxml;
    opens it.polimi.ingsw.am37.network.messages.lobby to javafx.fxml;
    opens it.polimi.ingsw.am37.network.messages.initialization to javafx.fxml;
    opens it.polimi.ingsw.am37.network.messages.endgame to javafx.fxml;
    opens it.polimi.ingsw.am37.network.messages.mixed to javafx.fxml;
    opens it.polimi.ingsw.am37.network.messages.turns to javafx.fxml;

    opens it.polimi.ingsw.am37.view to javafx.fxml;
    opens it.polimi.ingsw.am37.view.GUI to javafx.fxml;
    opens it.polimi.ingsw.am37.view.GUI.controllers to javafx.fxml;
    opens it.polimi.ingsw.am37.view.TUI to javafx.fxml;
    opens it.polimi.ingsw.am37.view.clientmodel to javafx.fxml;
    opens it.polimi.ingsw.am37.view.virtualserver to javafx.fxml;

}