import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Plateau.fxml"));
            GridPane root = loader.load();  // Charger le contenu du FXML dans un GridPane

            // Créer la scène avec le contenu chargé
            Scene scene = new Scene(root, 600, 400);

            // Définir la scène et afficher la fenêtre
            primaryStage.setTitle("Application JavaFX");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
