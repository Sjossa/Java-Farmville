package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class FermeController {

    @FXML
    private Button boutonMarche;

    // Méthode d'initialisation
    @FXML
    public void initialize() {
        // Si nécessaire, ajoute de la logique d'initialisation
    }

    // Méthode appelée lors du clic sur le bouton "Marché"
    @FXML
    private void ouvrirMarche() throws IOException {
        // Charger le FXML du marché
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Marché.fxml"));
        Scene marcheScene = new Scene(loader.load());

        // Créer une nouvelle fenêtre pour le marché
        Stage stage = new Stage();
        stage.setTitle("Marché");
        stage.setScene(marcheScene);
        stage.show();
    }
}
