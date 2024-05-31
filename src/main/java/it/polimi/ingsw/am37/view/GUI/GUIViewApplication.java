package it.polimi.ingsw.am37.view.GUI;

import it.polimi.ingsw.am37.view.GUI.GUIView;
import it.polimi.ingsw.am37.view.GUI.controllers.GUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.*;

import java.util.Objects;


public class GUIViewApplication extends Application{
    private static GUIView guiReference;

    @Override
    public void start (Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/am37/view/GUI/fxml/login.fxml"));
        Parent root = fxmlLoader.load();

        GUIController.setGuiReference(guiReference);

        primaryStage.setTitle("Codex Naturalis");

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am37/view/GUI/logo.png")));
        primaryStage.getIcons().add(icon);

        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }

    public static void main (String[] args){
        launch(args);
    }

    public static void setGUI(GUIView gui) {
        guiReference = gui;
    }

}