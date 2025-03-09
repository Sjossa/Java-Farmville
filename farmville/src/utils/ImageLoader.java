package utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageLoader {

    public static ImageView chargerImage(String chemin, double largeur, double hauteur) {
        ImageView imageView = new ImageView();
        try {
            Image image = new Image(ImageLoader.class.getResourceAsStream(chemin));
            imageView.setImage(image);
            imageView.setFitWidth(largeur);
            imageView.setFitHeight(hauteur);
        } catch (Exception e) {
            System.err.println("Erreur : Impossible de charger l'image " + chemin);
        }
        return imageView;
    }
}
