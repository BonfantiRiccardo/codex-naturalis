module it.polimi.ingsw.am37 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;

    opens it.polimi.ingsw.am37 to javafx.fxml;
    exports it.polimi.ingsw.am37;

    exports it.polimi.ingsw.am37.model.cards;
    exports it.polimi.ingsw.am37.model.cards.objective;
    exports it.polimi.ingsw.am37.model.cards.placeable;
    exports it.polimi.ingsw.am37.model.decks;
    exports it.polimi.ingsw.am37.model.exceptions;
    exports it.polimi.ingsw.am37.model.game;
    exports it.polimi.ingsw.am37.model.player;
    exports it.polimi.ingsw.am37.model.sides;
    exports it.polimi.ingsw.am37.controller;

    opens it.polimi.ingsw.am37.model.cards to com.google.gson;
    opens it.polimi.ingsw.am37.model.cards.objective to com.google.gson;
    opens it.polimi.ingsw.am37.model.cards.placeable to com.google.gson;
    opens it.polimi.ingsw.am37.model.sides to com.google.gson;
    exports it.polimi.ingsw.am37.controller.states;
}