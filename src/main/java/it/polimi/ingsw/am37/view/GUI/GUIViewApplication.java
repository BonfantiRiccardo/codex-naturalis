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

/**
 * This class is the GUI view application of the game.
 * It is used to start the GUI of the game.
 */
public class GUIViewApplication extends Application{
    /**
     * The reference to the GUI view.
     */
    private static GUIView guiReference;

    /**
     * This method is used to start the GUI of the game.
     * It loads the fxml file of the login page and shows it.
     * It sets the GUI reference.
     * @param primaryStage the stage of the GUI.
     * @throws Exception if an error occurs.
     */
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

    /**
     * This method is used to start the GUI of the game.
     * @param args the arguments of the main method.??
     */
    public static void main (String[] args){
        launch(args);
    }

    /**
     * This method is used to set the GUI reference.
     * @param gui the GUI reference.
     */
    public static void setGUI(GUIView gui) {
        guiReference = gui;
    }

}