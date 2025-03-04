import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Screen;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Plateau.fxml"));
            GridPane root = loader.load();  // Charger le contenu du FXML dans un GridPane

            // Récupérer la taille de l'écran principal
            Screen screen = Screen.getPrimary();
            double screenWidth = screen.getVisualBounds().getWidth();
            double screenHeight = screen.getVisualBounds().getHeight();

            // Créer la scène avec la taille de l'écran
            Scene scene = new Scene(root, screenWidth, screenHeight);

            // Définir la scène et afficher la fenêtre
            primaryStage.setTitle("farmville");
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
