package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class FermeController {

    @FXML
    private Button boutonMarche;

    @FXML
    private Button boutonReserve;

    @FXML
    private void ouvrirMarche() {
        changerScene("/views/Marche.fxml");
    }

    @FXML
    private void ouvrirReserve() {
        changerScene("/views/Reserve.fxml");
    }

    private void changerScene(String fxmlFile) {
        try {
            Stage stage = (Stage) boutonMarche.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
