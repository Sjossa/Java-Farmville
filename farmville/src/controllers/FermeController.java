package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ChoiceDialog;
import models.ProduitReserve;
import models.Stock;
import utils.AlertManager;
import utils.ImageLoader;
import utils.SceneManager;

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
        graineController = new GraineController();
        initialiserGrilleChamps();
        mettreAJourGraines();
    }

    @FXML
    private void ouvrirMarche() {
        SceneManager.changerScene("/views/Marche.fxml", boutonMarche);
    }

    @FXML
    private void ouvrirReserve() {
        SceneManager.changerScene("/views/Reserve.fxml", boutonReserve);
    }

    private void initialiserGrilleChamps() {
        for (int ligne = 1; ligne < 10; ligne++) {
            for (int colonne = 6; colonne < 10; colonne++) {
                ImageView champImage = ImageLoader.chargerImage("/ressource/image/herbes.png", 50, 50);
                champImage.setOnMouseClicked(event -> ouvrirDialogueChoixGraine(champImage));
                grilleChamps.add(champImage, colonne, ligne);
            }
        }
    }

    private void ouvrirDialogueChoixGraine(ImageView champImage) {
        List<String> grainesDisponibles = graineController.getListeNomsGraines();

        if (grainesDisponibles.isEmpty()) {
            AlertManager.afficherAlerte("Aucune graine disponible", "Il n'y a pas de graines disponibles.");
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(grainesDisponibles.get(0), grainesDisponibles);
        dialog.setTitle("Choisir une graine");
        dialog.setHeaderText("Sélectionnez une graine à planter.");
        dialog.setContentText("Graine :");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(graineChoisie -> planterGraine(graineChoisie, champImage));
    }

    private void planterGraine(String graineChoisie, ImageView champImage) {
        ProduitReserve graine = graineController.getGraineParNom(graineChoisie);
        if (graine != null) {
            graineController.planterGraine(graine, champImage);
        } else {
            AlertManager.afficherAlerte("Erreur", "Graine non disponible !");
        }
    }

    public void mettreAJourGraines() {
        graineController.setGrainesDisponibles(Stock.getInstance().getProduitsReserve());
    }
}
