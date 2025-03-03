import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.GridPane;

public class JardinageController {

    @FXML
    private GridPane zone1;

    @FXML
    private GridPane zone2;

    // Chemin vers l'image que vous souhaitez utiliser pour la fertilisation
    private final String imagePath = "image/a.png";
    private final String imagePath2 = "image/b.png";
    
    @FXML
    public void initialize() {
        // Ajouter dynamiquement les petites cases dans zone1 et zone2
        createZone(zone1, 4, 6);  // zone1 avec 4 lignes et 6 colonnes
        createZone(zone2, 4, 6);  // zone2 avec 4 lignes et 6 colonnes
    }

    // Créer une zone dynamique (ajoute des rectangles dans la grille)
    private void createZone(GridPane zone, int rows, int columns) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                // Créer un petit rectangle pour chaque case
                Rectangle smallRectangle = new Rectangle(25.0, 25.0);

                // Appliquer l'image de fertilisation comme fond de chaque case
                smallRectangle.setFill(new ImagePattern(new Image(imagePath)));

                // Ajouter un gestionnaire d'événements pour changer l'image lors du clic
                smallRectangle.setOnMouseClicked(this::fertilizeZone);

                // Ajouter le rectangle à la grille de la zone
                zone.add(smallRectangle, col, row);
            }
        }
    }

    @FXML
    private void fertilizeZone(MouseEvent event) {
        // Récupérer la case cliquée
        Rectangle clickedZone = (Rectangle) event.getSource();

        // Appliquer l'image de fertilisation lorsqu'on clique sur la case
        clickedZone.setFill(new ImagePattern(new Image(imagePath2)));
    }

    private void grandirPlante(timelineevent event){
        
    }
}
