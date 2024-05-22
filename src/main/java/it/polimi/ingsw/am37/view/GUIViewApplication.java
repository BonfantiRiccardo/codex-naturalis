package it.polimi.ingsw.am37.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIViewApplication extends Application{

    @Override
    public void start (Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(GUIViewApplication.class.getResource("/it/polimi/ingsw/am37/view/loginPhase/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        primaryStage.setTitle("Codex Naturalis");
        primaryStage.setScene(scene);
        primaryStage.show();

        /*Parent root = FXMLLoader.load(getClass().getResource("/it/polimi/ingsw/am37/view/loginPhase/login.fxml"));
        primaryStage.setTitle("Codex Naturalis");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();*/
    }

    public static void main (String[] args){
        launch(args);
    }
}