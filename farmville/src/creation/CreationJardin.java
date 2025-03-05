package creation;

import controlle.Grown;
import controlle.JardinageController;
import marche.Marche;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Region;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class CreationJardin {

    private static final String IMAGE_PATH_1 = "/ressource/image/a.png";
    private static final String IMAGE_PATH_2 = "/ressource/image/b.png";
    private static final String IMAGE_PATH_4 = "/ressource/image/maison.jpg";

    private final JardinageController controller;

    public CreationJardin(JardinageController controller) {
        this.controller = controller;
    }

    private static final Map<String, BiConsumer<GridPane, CreationJardin>> zoneCreators = new HashMap<>();

    static {
        zoneCreators.put("zone1", (zone, instance) -> instance.createChamps(zone, 3, 3));
        zoneCreators.put("zone2", (zone, instance) -> instance.createChamps(zone, 3, 3));
        zoneCreators.put("zone3", (zone, instance) -> instance.createChamps(zone, 3, 3));
        zoneCreators.put("marche", (zone, instance) -> instance.createHome(zone, 1, 1));
    }

    public void creerZone(GridPane plateau, String idZone, int row, int column) {
        GridPane zone = new GridPane();
        zone.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        plateau.add(zone, column, row);

        if (zoneCreators.containsKey(idZone)) {
            zoneCreators.get(idZone).accept(zone, this);
        }
    }

    private void createChamps(GridPane zone, int rows, int columns) {
        createGrid(zone, rows, columns, IMAGE_PATH_1, this::fertilizeZone);
    }

    private void createHome(GridPane zone, int rows, int columns) {
        createGrid(zone, rows, columns, IMAGE_PATH_4, event -> new Marche().ouvrirMarche(event));
    }

    private void createGrid(GridPane zone, int rows, int columns, String imagePath, javafx.event.EventHandler<MouseEvent> eventHandler) {
        zone.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            zone.heightProperty().addListener((obs2, oldHeight, newHeight) -> {
                double cellWidth = newWidth.doubleValue() / columns;
                double cellHeight = newHeight.doubleValue() / rows;

                zone.getChildren().clear();

                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < columns; col++) {
                        Rectangle cell = new Rectangle(cellWidth, cellHeight);
                        ImagePattern pattern = loadImagePattern(imagePath);
                        if (pattern != null) {
                            cell.setFill(pattern);
                        }
                        cell.setOnMouseClicked(eventHandler);
                        zone.add(cell, col, row);
                    }
                }
            });
        });
    }

    private void fertilizeZone(MouseEvent event) {
        if (!(event.getSource() instanceof Rectangle clickedZone)) {
            return;
        }

        ImagePattern currentPattern = (clickedZone.getFill() instanceof ImagePattern) ? (ImagePattern) clickedZone.getFill() : null;

        if (currentPattern != null && currentPattern.getImage().getUrl().endsWith(IMAGE_PATH_1)) {
            ImagePattern pattern = loadImagePattern(IMAGE_PATH_2);
            if (pattern != null) {
                clickedZone.setFill(pattern);
                new Grown(clickedZone, IMAGE_PATH_1, IMAGE_PATH_2, controller);
                controller.activerBoutonRecolte();
            }
        }
    }

    private static ImagePattern loadImagePattern(String path) {
        Image image = loadImage(path);
        return (image != null) ? new ImagePattern(image) : null;
    }

    private static Image loadImage(String path) {
        var url = CreationJardin.class.getResource(path);
        if (url == null) {
            System.err.println("Image non trouvée : " + path);
            return null;
        }
        return new Image(url.toString());
    }
}
