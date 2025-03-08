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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Nouvelle fenêtre"); // Optionnel : titre de la fenêtre
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
