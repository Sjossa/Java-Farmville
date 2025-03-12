package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    private static Stage primaryStage;
    private static String sceneDeRetour;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void setSceneDeRetour(String scene) {
        sceneDeRetour = scene;
    }

    public static void ouvrirNouvelleFenetre(String fxmlPath, String titre) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setTitle(titre);
            newStage.setScene(new Scene(root, 800, 600));

            // Quand la fenêtre se ferme, revenir sur Ferme.fxml
            newStage.setOnHidden(event -> changerScene(sceneDeRetour));

            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changerScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root, 800, 600));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
