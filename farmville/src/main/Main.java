package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger le fichier FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Ferme.fxml"));
        Parent root = fxmlLoader.load();

        // Configurer la scène et afficher la fenêtre
        primaryStage.setTitle("Farm my Farm");
        primaryStage.setScene(new Scene(root, 800, 600));  // Taille de la fenêtre
        primaryStage.show();  // Afficher la fenêtre
    }

    public static void main(String[] args) {
        launch(args);  // Démarrer l'application
    }
}
