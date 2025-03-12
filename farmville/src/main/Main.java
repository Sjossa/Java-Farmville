package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.SceneManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Définir le stage principal dans SceneManager
        SceneManager.setPrimaryStage(primaryStage);

        // Charger la scène principale (Ferme.fxml)
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Ferme.fxml"));
        Parent root = fxmlLoader.load();

        // Configurer la scène principale
        primaryStage.setTitle("Farm my Farm");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
