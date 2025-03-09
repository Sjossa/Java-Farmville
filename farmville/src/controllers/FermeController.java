package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import models.ProduitReserve;
import models.Stock;

import java.util.List;
import java.util.Optional;

public class FermeController {

    @FXML
    private Button boutonMarche;

    @FXML
    private Button boutonReserve;

    @FXML
    private GridPane grilleChamps;

    private GraineController graineController;

    @FXML
    public void initialize() {
        // Initialisation du GraineController et mise à jour des graines
        graineController = new GraineController();
        initialiserGrilleChamps(); // Initialiser la grille des champs
        mettreAJourGraines(); // Mettre à jour les graines disponibles
    }

    @FXML
    private void ouvrirMarche() {
        changerScene("/views/Marche.fxml");
    }

    @FXML
    private void ouvrirReserve() {
        changerScene("/views/Reserve.fxml");
    }

    // Méthode pour initialiser la grille des champs avec des images d'herbe
    private void initialiserGrilleChamps() {
        for (int ligne = 1; ligne < 10; ligne++) { // 10 lignes (index 1 à 9)
            for (int colonne = 6; colonne < 10; colonne++) { // 4 colonnes (index 6 à 9)
                ImageView champImage = chargerImageHerbe();
                champImage.setFitWidth(50);
                champImage.setFitHeight(50);
                champImage.setOnMouseClicked(event -> handleImageClick(champImage)); // Ajouter un clic sur l'image
                grilleChamps.add(champImage, colonne, ligne); // Ajouter l'image dans la grille
            }
        }
    }

    private void handleImageClick(ImageView champImage) {
        // Ouvrir un dialogue pour choisir une graine
        ouvrirDialogueChoixGraine(champImage);
    }

    // Méthode pour ouvrir un ChoiceDialog avec les graines disponibles
    private void ouvrirDialogueChoixGraine(ImageView champImage) {
        List<String> grainesDisponibles = graineController.getListeNomsGraines();

        if (grainesDisponibles.isEmpty()) {
            afficherAlerte("Aucune graine disponible", "Il n'y a pas de graines disponibles dans la réserve.");
            return;
        }

        // Ouvrir le dialogue de choix de graine
        ChoiceDialog<String> dialog = new ChoiceDialog<>(grainesDisponibles.get(0), grainesDisponibles);
        dialog.setTitle("Choisir une graine");
        dialog.setHeaderText("Sélectionnez une graine à planter.");
        dialog.setContentText("Graine :");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(graineChoisie -> planterGraine(graineChoisie, champImage));
    }

    // Méthode pour planter la graine choisie
    private void planterGraine(String graineChoisie, ImageView champImage) {
        ProduitReserve graine = graineController.getGraineParNom(graineChoisie);
        if (graine != null) {
            // Définir les images et le produit final
            String[] imagesCroissance = {"/ressource/image/graine1.png", "/ressource/image/graine2.png"};
            String produitFinal = graine.getNom();

            // Planter la graine sur l'image du champ
            graineController.planterGraine(graine, champImage, imagesCroissance, produitFinal);
        } else {
            afficherAlerte("Erreur", "Graine non disponible !");
        }
    }

    // Afficher une alerte
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour changer de scène
    private void changerScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Nouvelle fenêtre");
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour charger l'image d'herbe depuis le classpath
    private ImageView chargerImageHerbe() {
        ImageView champImage = new ImageView();
        Image herbeImage = new Image(getClass().getResourceAsStream("/ressource/image/herbes.png"));

        if (herbeImage != null) {
            champImage.setImage(herbeImage);
        } else {
            System.err.println("Erreur : Image non trouvée - /ressource/image/herbes.png");
        }

        return champImage;
    }

    // Méthode pour mettre à jour les graines disponibles dans GraineController
    public void mettreAJourGraines() {
        graineController.setGrainesDisponibles(Stock.getInstance().getProduitsReserve());
    }
}
