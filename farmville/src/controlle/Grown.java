package controlle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Grown {
    private final Image image1, image2, image3, image4, image5;
    private final Rectangle rectangle;
    private final JardinageController controller;

    public Grown(Rectangle rectangle, String imagePath1, String imagePath2, JardinageController controller) {
        this.rectangle = rectangle;
        this.controller = controller;

        this.image1 = loadImage(imagePath1);
        this.image2 = loadImage(imagePath2);
        this.image3 = loadImage("/ressource/image/c.png");
        this.image4 = loadImage("/ressource/image/d.png");
        this.image5 = loadImage("/ressource/image/f.png");

        rectangle.setFill(new ImagePattern(image2));

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> rectangle.setFill(new ImagePattern(image3))),
                new KeyFrame(Duration.seconds(2), e -> rectangle.setFill(new ImagePattern(image4))),
                new KeyFrame(Duration.seconds(3), e -> {
                    rectangle.setFill(new ImagePattern(image5));
                    controller.activerBoutonRecolte(); // Appel à la méthode pour activer le bouton
                })
        );
        timeline.setCycleCount(1);
        timeline.play();
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
