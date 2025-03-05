package controlle;

import creation.CreationJardin;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.Node;

public class JardinageController {

    @FXML private GridPane plateau;
    @FXML private Button recolterButton;

    private CreationJardin jardin;

    @FXML
    public void initialize() {
        jardin = new CreationJardin(this);

        // Création des zones
        creerZones();

        // Initialisation du bouton de récolte
        recolterButton.setDisable(true);
        recolterButton.setOnAction(event -> recolterPlantes());
    }

    private void creerZones() {
        jardin.creerZone(plateau, "zone1", 1, 4);
        jardin.creerZone(plateau, "zone2", 1, 5);
        jardin.creerZone(plateau, "zone3", 2, 4);
        jardin.creerZone(plateau, "marche", 0, 0);
    }

    private void recolterPlantes() {
        for (Node node : plateau.getChildren()) {
            if (node instanceof GridPane zone) {
                recolterDansZone(zone);
            }
        }
        recolterButton.setDisable(true);
    }

    private void recolterDansZone(GridPane zone) {
        for (Node child : zone.getChildren()) {
            if (child instanceof Rectangle rectangle) {
                changerImageSiMature(rectangle);
            }
        }
    }

    private void changerImageSiMature(Rectangle rectangle) {
        if (rectangle.getFill() instanceof ImagePattern pattern) {
            String imageUrl = pattern.getImage().getUrl();
            if (imageUrl.endsWith("f.png")) {
                rectangle.setFill(new ImagePattern(loadImage("/ressource/image/a.png")));
            }
        }
    }

    private Image loadImage(String path) {
        var url = getClass().getResource(path);
        if (url == null) {
            System.err.println("Image non trouvée : " + path);
            return null;
        }
        return new Image(url.toString());
    }

    public void activerBoutonRecolte() {
        recolterButton.setDisable(false);
    }
}
