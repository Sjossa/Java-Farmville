package marche;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class Marche {

    public void ouvrirMarche(javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Marché.fxml"));
            BorderPane root = fxmlLoader.load();

            Stage newStage = new Stage();
            newStage.setTitle("Marché");
            newStage.setScene(new Scene(root, 600, 400)); // Taille ajustée
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
