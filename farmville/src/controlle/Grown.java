package controlle;

import creation.CreationJardin;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Grown {

    private final Image image1;
    private final Image image2;
    private final Image image3; // Début
    private final Image image4; // Milieu
    private final Image image5;

    private static final String IMAGE_PATH_3 = "/ressource/image/c.png";
    private static final String IMAGE_PATH_4 = "/ressource/image/d.png";
    private static final String IMAGE_PATH_5 = "/ressource/image/f.png";

    private final Rectangle rectangle;
    private final Timeline timeline1;
    private final Timeline timeline2;
    private final Timeline timeline3;
    private final Timeline timeline4;

    public Grown(Rectangle rectangle, String imagePath1, String imagePath2) {
        this.rectangle = rectangle;

        this.image1 = loadImage(imagePath1);
        this.image2 = loadImage(imagePath2);
        this.image3 = loadImage(IMAGE_PATH_3);
        this.image4 = loadImage(IMAGE_PATH_4);
        this.image5 = loadImage(IMAGE_PATH_5);

        if (this.image2 != null) {
            this.rectangle.setFill(new ImagePattern(image2));
        }


        timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), event ->
                Platform.runLater(() -> rectangle.setFill(new ImagePattern(image3)))
        ));
        timeline1.setCycleCount(1);
        timeline1.play();


        timeline2 = new Timeline(new KeyFrame(Duration.seconds(1), event ->
                Platform.runLater(() -> {
                    if (rectangle.getFill() instanceof ImagePattern &&
                            ((ImagePattern) rectangle.getFill()).getImage() == image3) {
                        rectangle.setFill(new ImagePattern(image4));
                    }
                })
        ));
        timeline2.setCycleCount(1);

        timeline3 = new Timeline(new KeyFrame(Duration.seconds(1), event ->
                Platform.runLater(() -> {
                    if (rectangle.getFill() instanceof ImagePattern &&
                            ((ImagePattern) rectangle.getFill()).getImage() == image4) {
                        rectangle.setFill(new ImagePattern(image5));
                    }
                })
        ));
        timeline3.setCycleCount(1);

        timeline4 = new Timeline(new KeyFrame(Duration.seconds(2), event ->
                Platform.runLater(() -> {
                    if (rectangle.getFill() instanceof ImagePattern &&
                            ((ImagePattern) rectangle.getFill()).getImage() == image5) {
                        rectangle.setFill(new ImagePattern(image1));
                    }
                })
        ));timeline4.setCycleCount(1);


        timeline1.setOnFinished(event -> timeline2.play());
        timeline2.setOnFinished(event -> timeline3.play());
        timeline3.setOnFinished(event -> timeline4.play());



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
