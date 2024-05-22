package it.polimi.ingsw.am37.view.GUITest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class CA {

    /*@FXML
    private void handleButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("bbb.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }*/

    @FXML
    public void createButtonClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("bbb.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 500));
        stage.show();

        /*try {
            URL resource = getClass().getResource("/it/polimi/ingsw/am37/view/GUITest/bbb.fxml");
            if (resource == null) {
                System.out.println("Resource /ddd.fxml not found!");
                return;
            } else {
                System.out.println("Resource found: " + resource.toString());
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Failed to load FXML file: " + e.getMessage());
            e.printStackTrace();
        }*/
    }

    @FXML
    public void joinButtonClicked(ActionEvent actionEvent) {
    }
}
