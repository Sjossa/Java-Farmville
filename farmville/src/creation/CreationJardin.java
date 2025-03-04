package creation;

import controlle.Grown;
import marche.Marche;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class CreationJardin {

    private static final String IMAGE_PATH_1 = "/ressource/image/a.png";
    private static final String IMAGE_PATH_2 = "/ressource/image/b.png";
    private static final String IMAGE_PATH_4 = "/ressource/image/maison.jpg";

    public void createChamps(GridPane zone, int rows, int columns) {
        createGrid(zone, rows, columns, IMAGE_PATH_1, this::fertilizeZone);
    }

    public void createHome(GridPane zone, int rows, int columns) {
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
        Rectangle clickedZone = (Rectangle) event.getSource();
        ImagePattern currentPattern = (clickedZone.getFill() instanceof ImagePattern) ? (ImagePattern) clickedZone.getFill() : null;

        if (currentPattern != null && currentPattern.getImage().getUrl().endsWith(IMAGE_PATH_1)) {
            ImagePattern pattern = loadImagePattern(IMAGE_PATH_2);
            if (pattern != null) {
                clickedZone.setFill(pattern);
                new Grown(clickedZone, IMAGE_PATH_1, IMAGE_PATH_2);
            }
        }
    }

    private ImagePattern loadImagePattern(String path) {
        Image image = loadImage(path);
        return (image != null) ? new ImagePattern(image) : null;
    }

    private Image loadImage(String path) {
        var url = getClass().getResource(path);
        if (url == null) {
            System.err.println("Image non trouvée : " + path);
            return null;
        }
        return new Image(url.toString());
    }
}
