package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;

public class SceneManager {

    public static void changerScene(String fxmlFile, Control sourceControl) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlFile));
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setTitle("new");
            newStage.setScene(new Scene(root));
            newStage.show();
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}




