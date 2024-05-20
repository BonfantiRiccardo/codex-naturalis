package it.polimi.ingsw.am37.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIViewApplication extends Application{

    @Override
    public void start (Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(GUIViewApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        primaryStage.setTitle("Codex Naturalis");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main (String[] args){
        launch(args);
    }
}